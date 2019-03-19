package world.ouer.rss.net;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import world.ouer.rss.RssFeed;
import world.ouer.rss.RssReader;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.dao.RssItemDao;
import world.ouer.rss.dao.SourceItem;
import world.ouer.rss.dao.SourceItemDao;

public class RssAsyncService extends AsyncTask<List<SourceItem>, Integer, String> {
    private static final String TAG = "RssAsyncService";
    private RssItemDao rDao;
    private NetClient client;
    private DaoSession session;
    private SourceItemDao sDao;
    private Handler homeHandler;
    public static final int MESSAGE_UPDATE_NUM=1;
    public RssAsyncService(DaoSession session, Handler handler) {
        this.session=session;
        homeHandler=handler;
        client = NetClient.newInstance();
        rDao=session.getRssItemDao();
        sDao=session.getSourceItemDao();
    }

    @Override
    protected String doInBackground(List<SourceItem>... urls) {
        for (final SourceItem item:
                urls[0]) {
            try {
                client.asynRun(item.getUrl(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        homeHandler.obtainMessage(MESSAGE_UPDATE_NUM,e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            InputStream stream = response.body().byteStream();

                            insertRecorderRssItem(stream,item.getId());
                            updateLastAccessTimeInSourceItem(item);

                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "doInBackground: sync complete");
        return "sync complete";
    }

    private void updateLastAccessTimeInSourceItem(SourceItem sItem){
        StringBuilder sb =new StringBuilder(" where sid=");
        sb.append(sItem.getId()).append(" order by strftime('%Y-%m-%d %H:%M:%S',substr(pub_date,0,length(pub_date)-5)) limit 1");
        RssItem rItem=rDao.queryRaw(sb.toString()).get(0);
        sItem.setLastTimeAccess(rItem.getPubDate());
        sDao.update(sItem);
    }

    private void insertRecorderRssItem(InputStream stream,long id){

        try {
            RssFeed feed = RssReader.read(stream);
            ArrayList<RssItem> rssItems = feed.getRssItems();

            //关联RSS 和 SourceItem 表
            Iterator<RssItem> iterator =rssItems.iterator();
            while(iterator.hasNext()){
                iterator.next().setSid(id);
            }
            rDao.saveInTx(rssItems);
            homeHandler.obtainMessage(MESSAGE_UPDATE_NUM,"store__"+rssItems.size());
            Log.i(TAG, "store__:"+rssItems.size());

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
