package world.ouer.rss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * Created by pc on 2019/3/21.
 */

public abstract  class HtmlExtractor {

     StringBuilder sb;
    private final int TIME_OUT_IN_MILLSEC = 1 * 60 * 1000;
    private URL url;

    public HtmlExtractor(URL url) {
        sb = new StringBuilder();
        this.url = url;
    }

    public String parse() {
        try {
            Document docs = Jsoup.parse(url, TIME_OUT_IN_MILLSEC);
            extractHead(docs);
            extracBody(docs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    void extractHead(Document docs){};
    void extracBody(Document docs){};

}
