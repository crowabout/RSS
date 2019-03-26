package world.ouer.rss.splithtml;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

/**
 * Created by pc on 2019/3/22.
 */

public class SentificAmeraHtmlExtractor extends HtmlExtractor implements IExtractor {

    public SentificAmeraHtmlExtractor(URL url) {
        super(url);
        setIExtractor(this);
    }

    @Override
    public String extractAudio(Document docs) {
        return null;
    }

    @Override
    public String extractSubtitle(Document docs) {
        StringBuilder sb =new StringBuilder();
        sb.append(extractHead(docs))
                .append(extracBody(docs));
        return sb.toString();
    }

    private  String extractHead(Document doc) {
        Elements eles = doc.select("div.article-text");
        Elements pEles = eles.get(0).children();

        StringBuilder sb =new StringBuilder();
        for (Element ele : pEles) {
            if (ele.hasText()) {
                sb.append(ele.text());
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    private  String extracBody(Document doc) {
        Elements eles = doc.select("div#transcripts-body");
        Elements pEles = eles.get(0).children();
        StringBuilder sb =new StringBuilder();
        for (Element pe : pEles) {
            if (pe.hasText()) {
                sb.append(pe.text()).append("\n\n");
            }
        }
        return sb.toString();
    }


}
