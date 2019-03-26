package world.ouer.rss.splithtml;

import org.jsoup.nodes.Document;

/**
 * Created by pc on 2019/3/25.
 */

public interface IExtractor {

    String extractAudio(Document docs);
    String extractSubtitle(Document docs);


}
