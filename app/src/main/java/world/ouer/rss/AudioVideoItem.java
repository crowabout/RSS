package world.ouer.rss;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pc on 2019/3/7.
 */


@Entity(nameInDb = "avioitem")
public class AudioVideoItem {

    @Id(autoincrement = true)
    Long id;
    /**
     *  file name
     */
    String name;
    /**
     * Audio or Video
     */
    String fileType;
    /**
     * file store path
     */
    String storePlace;
    /**
     * file size length
     */
    String length;

    /**
     * has read
     */
    boolean isRead;

    /**
     *  which category the file belong to.
     */
    String category;

    @Generated(hash = 1249510891)
    public AudioVideoItem(Long id, String name, String fileType, String storePlace,
            String length, boolean isRead, String category) {
        this.id = id;
        this.name = name;
        this.fileType = fileType;
        this.storePlace = storePlace;
        this.length = length;
        this.isRead = isRead;
        this.category = category;
    }

    @Generated(hash = 246076218)
    public AudioVideoItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getStorePlace() {
        return storePlace;
    }

    public void setStorePlace(String storePlace) {
        this.storePlace = storePlace;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
