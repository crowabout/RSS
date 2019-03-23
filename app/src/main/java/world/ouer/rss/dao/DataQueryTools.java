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
    private final int PAGE_SIZE=40;
    public DataQueryTools(DaoSession session){
        this.session=session;
        sidao=session.getSourceItemDao();
        ridao=session.getRssItemDao();
        avidao=session.getAudioVideoItemDao();
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
}
