package world.ouer.rss;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

/**
 * Created by pc on 2019/3/22.
 */

public class SentificAmeraHtmlExtractor  extends HtmlExtractor{

    public SentificAmeraHtmlExtractor(URL url) {
        super(url);
    }

    @Override
    void extractHead(Document doc) {

        Elements eles = doc.select("div.article-text");
        Elements pEles = eles.get(0).children();

        for (Element ele : pEles) {
            if (ele.hasText()) {
                sb.append(ele.text());
            }
        }
    }

    @Override
    void extracBody(Document doc) {
        Elements eles = doc.select("div#transcripts-body");
        Elements pEles = eles.get(0).children();
        for (Element pe : pEles) {
            if (pe.hasText()) {
                sb.append(pe.text()).append("\n");
            }
        }
    }


}
