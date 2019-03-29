package world.ouer.rss.play;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

import world.ouer.rss.dao.Subtitle;
import world.ouer.rss.splithtml.HtmlExtractor;
import world.ouer.rss.splithtml.SentificAmeraHtmlExtractor;

/**
 * Created by pc on 2019/3/28.
 * <p>
 * 播放 60-second Science
 */

public class SAmPlayAudioAt extends PlayAudioActivity implements HtmlExtractor.LoadFinishListener{
    @Override
    protected void onStart() {
        super.onStart();
        loadAudioFromNotEmptyEnclosure();
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
        } else {
            //not download transcript yet.
            URL url = new URL(item.getLink());
            mTextDebug.append(String.format("load transcript from [url]\n"));
            extractor = new SentificAmeraHtmlExtractor(url);
            extractor.extractAsyn();
            mTextDebug.append(String.format("loading url"));
            extractor.setLoadFinishListener(this);
        }
    }

    @Override
    public void notifyLoadOk() {
        mHandler.obtainMessage(MSG_LOGGING, "load =Transcript= finish!!!\n").sendToTarget();
        String mTranscriptTv = extractor.obtainSubtitle();
        String audioUrl = extractor.obtainAudioUrl();
        long rssItemId = item.getId();
        dqt.saveSubtitle(mTranscriptTv, audioUrl, rssItemId);
        item.setHasLocalTranscript(true);
        dqt.updateRssItem(item);
        mHandler.obtainMessage(MSG_TRANSCRIPT, mTranscriptTv).sendToTarget();
    }
}
