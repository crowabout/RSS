package world.ouer.rss;

/**
 * Created by pc on 2019/3/27.
 */

public enum ChannelType {

    /**
     * Science
     */
    CHANNEL_TYPE_SCI("Science"),
    /**
     * 60-second Science
     */
    CHANNEL_TYPE_SAM("60-Second Science");

    private String channel;
    private ChannelType(String channel){
        this.channel=channel;
    }
}
