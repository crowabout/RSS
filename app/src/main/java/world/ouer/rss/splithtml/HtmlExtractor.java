package world.ouer.rss.splithtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pc on 2019/3/21.
 */

public abstract class HtmlExtractor{

    private final int TIME_OUT_IN_MILLSEC = 1 * 60 * 1000;
    private URL url;
    private IExtractor mExtractor;
    private Map<String,String> mResultContainer;
    private static ExecutorService service;
    private final static String AUDIO_URL="audio_url";
    private final static String AUDIO_TRANSCRIPT="transcript";

    private LoadFinishListener listener;
    private Document docs;

    public HtmlExtractor(URL url) {
        this.url = url;
        mResultContainer=new HashMap<>();
        service= Executors.newFixedThreadPool(1);
    }


    public HtmlExtractor(InputStream in,URL url){
        this.url = url;
        try {
            mResultContainer=new HashMap<>();
            docs =Jsoup.parse(in,"UTF-8",url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public URL obtainUrl(){
        return url;
    }


    protected void setIExtractor(IExtractor extractor){
        mExtractor=extractor;
    }

    public String obtainAudioUrl(){
        return mResultContainer.get(AUDIO_URL);
    }
    public String obtainSubtitle(){
        return mResultContainer.get(AUDIO_TRANSCRIPT);
    }


    public void setLoadFinishListener(LoadFinishListener listener){
        this.listener=listener;
    }

    /**
     * extract audio url or transcript from html.
     *
     * @throws java.util.concurrent.TimeoutException
     * @throws java.net.MalformedURLException
     * @throws org.jsoup.UnsupportedMimeTypeException
     * @throws java.net.SocketTimeoutException
     */
    public void extractAsyn() {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Document docs = null;
                    try {
                        docs = Jsoup.parse(url, TIME_OUT_IN_MILLSEC);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mResultContainer.put(AUDIO_URL,mExtractor.extractAudio(docs));
                    mResultContainer.put(AUDIO_TRANSCRIPT,mExtractor.extractSubtitle(docs));
                    listener.notifyLoadOk();
                }
            });
    }

    /**
     *
     */
    public void extracSyn(){
        mResultContainer.put(AUDIO_URL,mExtractor.extractAudio(docs));
        mResultContainer.put(AUDIO_TRANSCRIPT,mExtractor.extractSubtitle(docs));
    }

    public interface LoadFinishListener{
        /**
         * @note run on workThread
         */
        void notifyLoadOk();
    }

}
