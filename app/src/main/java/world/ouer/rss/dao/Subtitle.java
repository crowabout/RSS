package world.ouer.rss.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pc on 2019/3/21.
 */

@Entity(nameInDb = "subtitle")
public class Subtitle {

    @Id(autoincrement = true)
    public Long id;
    public String subtitle;
    public long urlHashcode;
    public long refID;
    @Generated(hash = 1182970823)
    public Subtitle(Long id, String subtitle, long urlHashcode, long refID) {
        this.id = id;
        this.subtitle = subtitle;
        this.urlHashcode = urlHashcode;
        this.refID = refID;
    }
    @Generated(hash = 1071476920)
    public Subtitle() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSubtitle() {
        return this.subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public long getUrlHashcode() {
        return this.urlHashcode;
    }
    public void setUrlHashcode(long urlHashcode) {
        this.urlHashcode = urlHashcode;
    }
    public long getRefID() {
        return this.refID;
    }
    public void setRefID(long refID) {
        this.refID = refID;
    }
    


}
