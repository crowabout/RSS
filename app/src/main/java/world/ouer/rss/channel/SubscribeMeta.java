package world.ouer.rss.channel;

/**
 * Created by pc on 2019/3/7.
 */

public class SubscribeMeta {
    public String title;
    public int  updateItems=0;

    public SubscribeMeta() {
    }

    public SubscribeMeta(String title) {
        this.title = title;
    }

    public SubscribeMeta(String title, int updateItems) {
        this(title);
        this.updateItems = updateItems;
    }
}
