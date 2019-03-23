package world.ouer.rss.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pc on 2019/3/7.
 *
 *  this table store audio or video information has downloaded from url.
 *
 */
@Entity(nameInDb = "avioitem")
public class AudioVideoItem {

    @Id(autoincrement = true)
    Long id;
    /**
     *  file name
     */
    String originName;
    /**
     * Audio or Video
     */
    String fileType;
    /**
     * file store path
     *
     *  /mnt/sdcard/${channel}/${date}/
     */
    String storePath;
    /**
     *  which channel the file belong to.
     */
    String channel;

    String date;

    boolean isRead;
    /**
     *
     * file name without special characters(?_$...)
     */
    String canonicalName;
    @Generated(hash = 1706191448)
    public AudioVideoItem(Long id, String originName, String fileType,
            String storePath, String channel, String date, boolean isRead,
            String canonicalName) {
        this.id = id;
        this.originName = originName;
        this.fileType = fileType;
        this.storePath = storePath;
        this.channel = channel;
        this.date = date;
        this.isRead = isRead;
        this.canonicalName = canonicalName;
    }
    @Generated(hash = 246076218)
    public AudioVideoItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOriginName() {
        return this.originName;
    }
    public void setOriginName(String originName) {
        this.originName = originName;
    }
    public String getFileType() {
        return this.fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getStorePath() {
        return this.storePath;
    }
    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public String getCanonicalName() {
        return this.canonicalName;
    }
    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }





}
