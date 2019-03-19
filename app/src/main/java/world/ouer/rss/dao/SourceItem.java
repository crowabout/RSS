package world.ouer.rss.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by pc on 2019/3/8.
 *
 * this table store the RSS source information ;
 */

@Entity(nameInDb = "sources")
public class SourceItem {

    @Id(autoincrement = true)
    Long id;
    
    protected String url;
    protected String channel;
    protected Date addDate;
    @Generated(hash = 2091960795)
    /**
     * used to distinguish different url.
     */
    protected long hashcode;
    private String lastTimeAccess;

    @Generated(hash = 1507562486)
    public SourceItem(Long id, String url, String channel, Date addDate,
            long hashcode, String lastTimeAccess) {
        this.id = id;
        this.url = url;
        this.channel = channel;
        this.addDate = addDate;
        this.hashcode = hashcode;
        this.lastTimeAccess = lastTimeAccess;
    }
    @Generated(hash = 2098034463)
    public SourceItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public Date getAddDate() {
        return this.addDate;
    }
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
    public long getHashcode() {
        return this.hashcode;
    }
    public void setHashcode(long hashcode) {
        this.hashcode = hashcode;
    }
    public String getLastTimeAccess() {
        return this.lastTimeAccess;
    }
    public void setLastTimeAccess(String lastTimeAccess) {
        this.lastTimeAccess = lastTimeAccess;
    }


}

