package world.ouer.rss.splithtml;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

/**
 * Created by pc on 2019/3/25.
 */
public class NTPHtmlExtractor extends HtmlExtractor implements IExtractor {
    
    public static final String TAG="NTPHtmlExtractor";
    public NTPHtmlExtractor(URL url) {
        super(url);
        setIExtractor(this);
    }

    @Override
    public String extractAudio(Document docs) {
        Elements li = docs.select("li.audio-tool-download");
        if(li.size()==0){
            return "audio url not found.can't find node <li> with class equals 'audio-tool-download'";
        }
        Element a = li.get(0).child(0);
        if (a.hasAttr("href")) {
            return a.attr("href");
        }
        return "audio url not found";
    }

    @Override
    public String extractSubtitle(Document docs) {

        StringBuilder sb = new StringBuilder();
        Elements div = docs.select("div.transcript");
        Elements ps = div.get(0).children();
        for (Element ptag : ps) {
            if (ptag.hasText() && (!ptag.hasAttr("class")))
                sb.append(ptag.text()).append("\n\n");
        }
        return sb.toString();
    }

}
