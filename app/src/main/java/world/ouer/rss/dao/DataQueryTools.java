package world.ouer.rss.dao;

import android.util.Log;

import java.util.List;

/**
 * Created by pc on 2019/3/23.
 */

public class DataQueryTools {


    private static final String TAG ="DataQueryTools" ;
    private DaoSession session;
    private SourceItemDao sidao;
    private AudioVideoItemDao avidao;
    private RssItemDao ridao;
    private SubtitleDao stDao;
    private final int PAGE_SIZE=40;
    public DataQueryTools(DaoSession session){
        this.session=session;
        sidao=session.getSourceItemDao();
        ridao=session.getRssItemDao();
        avidao=session.getAudioVideoItemDao();
        stDao =session.getSubtitleDao();
    }




    public List<RssItem> queryRssItem(int index){
        int offset =index*PAGE_SIZE;
        Log.i(TAG, String.format("index:%d offset:%d",index,offset));
        return ridao.queryBuilder().limit(PAGE_SIZE).offset(offset).list();
    }

    public List<SourceItem> queryAllSourceItem(){
        return sidao.queryBuilder().list();
    }
    public SourceItem querySourceItemLastAccessTime(){
        return sidao.queryBuilder().where(SourceItemDao.Properties.Id.eq(1)).unique();
    }

    public AudioVideoItem findAvPathByUrl(String url){
        return avidao.queryBuilder().where(AudioVideoItemDao.Properties.OriginUrl.eq(url)).unique();
    }


    public List<RssItem> queryManyRssItem(long index,int size){
        int curPageSize=10;
        if(size!=0){
            curPageSize=size;
        }
        int offset = (int) (index*curPageSize);
        Log.i(TAG, String.format("queryOneRssItem__index:%d offset:%d",index,offset));
        return ridao.queryBuilder().where(RssItemDao.Properties.HasLocalTranscript.eq(false)).limit(size).offset(offset).list();
    }


    public  Subtitle findSubtitleByRssId(long id){
        return stDao.queryBuilder().where(SubtitleDao.Properties.RSSItemID.eq(id)).unique();
    }


    public  void saveSubtitle(String transcript,String audioUrl,long rssItemId){
        Subtitle title =new Subtitle(null,transcript,audioUrl,rssItemId);
        stDao.save(title);
    }


    public void updateRssItem(RssItem item){
        ridao.update(item);
    }


    public long countTranscriptUndownload(){
        return ridao.queryBuilder()
                .where(RssItemDao.Properties.HasLocalTranscript.eq(false),RssItemDao.Properties.Channel.in("Science","60-Second Science")).count();
    }

    public long alreadyDownloadNumTransctipt(){
        return stDao.count();
    }

}
