package world.ouer.rss.play;

import android.text.TextUtils;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import world.ouer.rss.dao.Subtitle;
import world.ouer.rss.splithtml.HtmlExtractor;
import world.ouer.rss.splithtml.NTPHtmlExtractor;

/**
 * Created by pc on 2019/3/28.
 * 播放 science NTP的音频
 */

public class SciPlayAudioAt  extends PlayAudioActivity implements HtmlExtractor.LoadFinishListener{
    @Override
    protected void onStart() {
        super.onStart();
        try {
            loadSubtitle();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void loadSubtitle() throws MalformedURLException {
        Subtitle subtitle = dqt.findSubtitleByRssId(item.getId());
        if (subtitle != null && !TextUtils.isEmpty(subtitle.getTranscript())) {
            //already download transcript
            mTextDebug.append("load mTranscriptTv from [local]\n");
            mTranscriptTv.setText(subtitle.getTranscript());
            Log.d(TAG, "loadSubtitle: "+item.getId()+" \n"+subtitle.getTranscript());


            String enclosure =item.getEnclosure();
            if(!TextUtils.isEmpty(enclosure) && enclosure.startsWith("http")){
                loadAudioFromNotEmptyEnclosure();
            }else {
                mTextDebug.append("no video resource");
            }

        } else {
            //not download transcript yet.
            URL url = new URL(item.getLink());
            mTextDebug.append(String.format("load transcript from \nNetwork\n"));
            extractor = new NTPHtmlExtractor(url);
            extractor.extractAsyn();
            mTextDebug.append(String.format("loading url:%s\n",url));
            extractor.setLoadFinishListener(this);
        }
    }


    @Override
    public void notifyLoadOk() {
        String mTranscriptTv = extractor.obtainSubtitle();
        String audioUrl = extractor.obtainAudioUrl();
        long rssItemId = item.getId();
        dqt.saveSubtitle(mTranscriptTv, audioUrl, rssItemId);
        if (!TextUtils.isEmpty(item.getEnclosure())&& audioUrl.startsWith("http")) {
            item.setEnclosure(audioUrl);
        }
        item.setHasLocalTranscript(true);
        dqt.updateRssItem(item);
        mHandler.obtainMessage(MSG_TRANSCRIPT, mTranscriptTv).sendToTarget();
        mHandler.obtainMessage(MSG_LOADAUDIO,audioUrl).sendToTarget();
    }
}
