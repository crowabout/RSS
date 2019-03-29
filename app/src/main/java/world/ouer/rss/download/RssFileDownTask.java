package world.ouer.rss.download;

import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import world.ouer.rss.RssUtils;
import world.ouer.rss.dao.AudioVideoItem;
import world.ouer.rss.dao.AudioVideoItemDao;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.dao.RssItemDao;

/**
 * Created by pc on 2019/3/11.
 */

public class RssFileDownTask extends Thread {

    private static final int SLEEP_TIME=1*60*1000;
    private static final String TAG = "RssFileDownTask";
    private AudioVideoItemDao avDao;
    private RssItemDao rssDao;
    private final int query_size = 4;
    private int downloadFinishNum=0;
    private int downloadErrorNum=0;
    private long totalNumInDb;
    private int page=0;
    private String stat="";
    private final int LENGTH=1;

    public RssFileDownTask(DaoSession session) {
        avDao = session.getAudioVideoItemDao();
        rssDao = session.getRssItemDao();
        initVair();
    }

    private long alreadyDwnlodNum(){
       return avDao.count();
    }

    private void initVair(){
        totalNumInDb=size();
        downloadFinishNum = (int) alreadyDwnlodNum();
        page=downloadFinishNum/query_size;
    }

    private FileDownloadListener dListener =new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            task.setTag(LENGTH,RssUtils.toMb(totalBytes));
            Log.w(TAG, "[pending]: "+task.getFilename());
        }
        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            stat=String.format("[progress] %d  %d/%s %.2f\n",task.getId(),soFarBytes,RssUtils.toMb(totalBytes),(float)soFarBytes/totalBytes);
            Log.i(TAG,stat);
        }
        @Override
        protected void completed(BaseDownloadTask task) {
            downloadFinishNum++;
            record(task);
            Log.i(TAG, String.format("[completed] %d %d %s %s",task.getId(),downloadFinishNum,task.getFilename(),task.getPath()));
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            downloadErrorNum++;
            Log.w(TAG,String.format("error msg:%s id:%d",e.getMessage(),task.getId()));
        }
        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };

    private ArrayList<RssItem> items() {
        page++;
        return (ArrayList<RssItem>) rssDao.queryBuilder()
                .where(RssItemDao.Properties.Enclosure.isNotNull())
                .limit(query_size)
                .offset((page-1)*query_size).orderAsc(RssItemDao.Properties.Id)
                .list();
    }

    private long size() {
        return rssDao.queryBuilder().where(RssItemDao.Properties.Enclosure.isNotNull()).count();
    }

    public void execute() {
        long all=0;
        while(all<totalNumInDb){
            all=downloadFinishNum+downloadErrorNum;
            Log.d(TAG, String.format("dealAmount:%d totalNumInDb:%d page*page_size:%d",all,totalNumInDb,page*query_size));
            if(all>=page*query_size){
                Log.d(TAG, "execute: should be feed.");
                custom(items());
            }else{
                try {
                    Log.w(TAG, "execute: !!!!sleep!!!");
                    Thread.currentThread().sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void custom(List<RssItem> objectx ) {

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(dListener);

        final List<BaseDownloadTask> tasks = new ArrayList<>();

        for (RssItem item :
                objectx) {
            Log.d(TAG, String.format("task.add(%s",item.getId()));
            tasks.add(FileDownloader.getImpl().create(item.getEnclosure()).setPath(path(item.getChannel(),
                    RssUtils.sha1(item.getEnclosure()))));

        }
        // do not want each task's download progress's callback,
//        queueSet.disableCallbackProgressTimes();
        // auto retry 1 time if download fail
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }

    @Override
    public void run() {
        execute();
    }

    private void record(BaseDownloadTask task){
        AudioVideoItem av =new AudioVideoItem();

        String path =task.getPath();
        String url =task.getUrl();
        av.setStorePath(path);
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyyMMddHHmm");
        av.setDate(sdf.format(System.currentTimeMillis()));
        av.setOriginUrl(task.getUrl());
        av.setUrlSha1Digest(task.getFilename());
        av.setChannel(channelInPath(path));
        av.setFileType(RssUtils.extractFileTypeFromUrl(url));
        avDao.insertOrReplace(av);
        updateIsDownloadedColumsInRss(url);
    }

    /**
     * update isDownload columns which the url is @param url in RSS table.
     * @param url
     */
    private void updateIsDownloadedColumsInRss(String url){
        RssItem items =rssDao.queryBuilder().where(RssItemDao.Properties.Enclosure.eq(url)).unique();
        items.setIsDownloaded(true);
        rssDao.update(items);
    }

    private String channelInPath(String path){
        int firstSlashFromEnd =path.lastIndexOf("/");
        int secSlashFromEnd =path.lastIndexOf("/",firstSlashFromEnd-1);
        return path.substring(secSlashFromEnd+1,firstSlashFromEnd);
    }


    private String path(String suffixDir,String name){
        if(TextUtils.isEmpty(suffixDir)){
            return defPath ;
        }
        //replace all non-word characters.
        suffixDir =suffixDir.replaceAll("[^\\w]","");
        return String.format(AV_PATH,suffixDir,name);
    }

    private static final String AV_PATH="/mnt/sdcard/ouer.world.rss/%s/%s";

    public String curDwnlodStat(){
        return stat ;
    }
    /**
     * already downloaded file amount
     * @return
     */
    public int curDwnlodNum(){
        return downloadFinishNum;
    }
    /**
     * undownloaded files
     * @return
     */
    public long curUnDwnlodNum(){
        return totalNumInDb-downloadFinishNum;
    }


    private static final String defPath="RSS";

    public long curErrorNum(){
        return downloadErrorNum;
    }



}
