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

public class TranscriptDownLoader implements Runnable {


    public final static String TAG = "TranscriptDownLoader";
    private DataQueryTools dqt;
    private long curPageIndex = 0;
    private final int pageSize = 10;
    //(1/2)*60*1000;
    private final int sleep_time = 5 * 1000;
    /**
     * 总共的数量
     */
    private long mTotalAllNum = 0;
    /**
     * 完成数量
     */
    private volatile long mFinishedNum = 0;
    /**
     * 一次加载的数量
     */
//    private volatile long mOnceLoadedNum=0;
    private volatile int mErrorNum = 0;

    private boolean mIsContinue = true;

    public TranscriptDownLoader(DataQueryTools dqt) {
        this.dqt = dqt;
        mTotalAllNum = dqt.countTranscriptUndownload();
    }

    @Override
    public void run() {
        while (mIsContinue && (mFinishedNum + mErrorNum) < mTotalAllNum) {

            Log.d(TAG, String.format("in_WHILE mTotalNum:%d  mFinishNum:%d mErrorNum:%d curPageIndex(%d)*size:%d \n",
                    mTotalAllNum,
                    mFinishedNum,
                    mErrorNum,
                    curPageIndex,
                    curPageIndex * pageSize

            ));

            if ((mFinishedNum + mErrorNum) >= curPageIndex * pageSize) {
                List<RssItem> source = dqt.queryManyRssItem(curPageIndex, pageSize);
                if(source.size()==0){
                    break;
                }
                for (int i = 0; i < source.size(); i++) {
                    try {
                        RssItem item = source.get(i);
                        consume(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "run: load(" + source.size() + ")");
                curPageIndex++;
            }
            try {
                Log.d(TAG, "sleeping !!!");
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume(final RssItem item) throws IOException {
        final String url = item.getLink();
        final String channel = item.getChannel();
        final URL mURL = new URL(url);

        NetClient.newInstance().asynRun(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure" + e.getMessage());
                mFinishedNum++;
                mErrorNum++;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                    mFinishedNum++;
                } else {
                    mErrorNum++;
                }
                Log.d(TAG, "onResponse:  mFinshNum:"+mFinishedNum);
            }
        });
    }

    public long getmTotalAllNum() {
        return mTotalAllNum;
    }

    public long getmFinishedNum() {
        return mFinishedNum;
    }

    public int getmErrorNum() {
        return mErrorNum;
    }
}
