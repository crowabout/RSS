package world.ouer.rss.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import world.ouer.rss.RssFeed;
import world.ouer.rss.RssReader;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.dao.RssItemDao;
public class RssAsyncService extends AsyncTask<URL, Integer, String> {
    private static final String TAG = "RssAsyncService";
    private RssItemDao rDao;
    private Context mCtx;
    private NetClient client;
    public RssAsyncService(RssItemDao rDao, Context mCtx) {
        this.mCtx = mCtx;
        this.rDao = rDao;
        client = NetClient.newInstance();
    }

    @Override
    protected String doInBackground(URL... urls) {
        for (URL url :
                urls) {
            try {
                client.asynRun(url.toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            InputStream stream = response.body().byteStream();
                            try {
                                RssFeed feed = RssReader.read(stream);
                                ArrayList<RssItem> rssItems = feed.getRssItems();
                                rDao.saveInTx(rssItems);
                                Log.i(TAG, "store__:"+rssItems.size());
                            } catch (SAXException e) {
                                e.printStackTrace();
                            }

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(mCtx, s, Toast.LENGTH_SHORT).show();
    }


}
