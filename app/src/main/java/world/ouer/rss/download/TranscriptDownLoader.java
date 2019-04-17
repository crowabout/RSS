package world.ouer.rss.download;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import world.ouer.rss.RssUtils;
import world.ouer.rss.dao.DataQueryTools;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.net.NetClient;
import world.ouer.rss.splithtml.HtmlExtractor;
import world.ouer.rss.splithtml.NTPHtmlExtractor;
import world.ouer.rss.splithtml.SentificAmeraHtmlExtractor;

/**
 * Created by pc on 2019/3/26.
 */
public class TranscriptDownLoader extends Thread {
    public final static String TAG = "TranscriptDownLoader";
    private DataQueryTools dqt;
    private final int sleep_time = 20 * 1000;
    private StringBuilder sb;
    /**
     * 总共的数量
     */
    private long mNeededDownloadNum = 0;
    /**
     * 完成数量
     */
    private volatile long mTotalFinishNum = 0;
    private int pageSize = 30;
    private volatile int mFinishNumOneTime= 0;
    private int loadNum=0;
    private int index;

    public TranscriptDownLoader(DataQueryTools dqt) {
        this.dqt = dqt;
        sb =new StringBuilder();
        mNeededDownloadNum = dqt.countTranscriptUndownload();
        Log.d(TAG, "mNeededDownloadNum: "+mNeededDownloadNum);
    }

    @Override
    public void run() {

        while (mTotalFinishNum < mNeededDownloadNum) {


            if (mFinishNumOneTime == loadNum) {
                mTotalFinishNum+=mFinishNumOneTime;
                mFinishNumOneTime=0;

                Log.d(TAG, "run: mTotalFinishNum:" + mTotalFinishNum);
                List<RssItem> preDownloaded = dqt.queryManyRssItem(index, pageSize);
                loadNum=preDownloaded.size();
                if(loadNum==0){
                    sb.append("...download Finish...\n");
                    break;
                }

                index++;

                for (RssItem item : preDownloaded) {
                    try {
                        consume(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if(mFinishNumOneTime<loadNum){
                try {
                    Log.d(TAG, "run: sleep  ");
                    sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        sb.append("...download Finish...\n");
    }


    private void consume(final RssItem item) throws IOException {
        final String url = item.getLink();
        final String channel = item.getChannel();
        final URL mURL = new URL(url);

        NetClient.newInstance().asynRun(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure" + e.getMessage());
                mFinishNumOneTime++;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG, String.format("thread_id:%d\n", Thread.currentThread().getId()));

                if (response.code() == 200) {
                    InputStream in = response.body().byteStream();
                    HtmlExtractor extractor = null;
                    String simplyfyChannel = RssUtils.channelSimplify(channel);

                    if (simplyfyChannel.equalsIgnoreCase("SAm")) {
                        extractor = new SentificAmeraHtmlExtractor(in, mURL);
                    } else if (simplyfyChannel.equalsIgnoreCase("Sci")) {
                        extractor = new NTPHtmlExtractor(in, mURL);
                    } else {
                        Log.d(TAG, "_channel_: " + channel + " simplefy:" + simplyfyChannel);
                        return;
                    }
                    extractor.extracSyn();
                    String audioUrl = extractor.obtainAudioUrl();
                    String subtitle = extractor.obtainSubtitle();
                    dqt.saveSubtitle(subtitle, audioUrl, item.getId());
                    item.setHasLocalTranscript(true);
                    dqt.updateRssItem(item);
                }

                mFinishNumOneTime++;
                Log.d(TAG, "onResponse:  mFinishNumOneTime:" + mFinishNumOneTime);
            }
        });
    }

    public String downloadStata() {
        return String.format("%d/%d",dqt.countAlreadyDownLoadTranscript(),dqt.rssItemCount());
    }

    public String debugStr(){
        return sb.toString();
    }

}
