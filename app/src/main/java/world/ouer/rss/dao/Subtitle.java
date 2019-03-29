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

    public String transcript;
    public String audioUrl;
    public long RSSItemID;
    @Generated(hash = 444301413)
    public Subtitle(Long id, String transcript, String audioUrl, long RSSItemID) {
        this.id = id;
        this.transcript = transcript;
        this.audioUrl = audioUrl;
        this.RSSItemID = RSSItemID;
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
    public String getTranscript() {
        return this.transcript;
    }
    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
    public String getAudioUrl() {
        return this.audioUrl;
    }
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    public long getRSSItemID() {
        return this.RSSItemID;
    }
    public void setRSSItemID(long RSSItemID) {
        this.RSSItemID = RSSItemID;
    }
    


}
