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
     *  file url
     */
    String originUrl;
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

    Long rssItmeId;

    String urlSha1Digest;

    @Generated(hash = 1879709352)
    public AudioVideoItem(Long id, String originUrl, String fileType,
            String storePath, String channel, String date, Long rssItmeId,
            String urlSha1Digest) {
        this.id = id;
        this.originUrl = originUrl;
        this.fileType = fileType;
        this.storePath = storePath;
        this.channel = channel;
        this.date = date;
        this.rssItmeId = rssItmeId;
        this.urlSha1Digest = urlSha1Digest;
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

    public String getOriginUrl() {
        return this.originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getRssItmeId() {
        return this.rssItmeId;
    }

    public void setRssItmeId(Long rssItmeId) {
        this.rssItmeId = rssItmeId;
    }

    public String getUrlSha1Digest() {
        return this.urlSha1Digest;
    }

    public void setUrlSha1Digest(String urlSha1Digest) {
        this.urlSha1Digest = urlSha1Digest;
    }





}
