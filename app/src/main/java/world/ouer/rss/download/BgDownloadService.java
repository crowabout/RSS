package world.ouer.rss.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import world.ouer.rss.RssApplication;

public class BgDownloadService extends Service {
    private static final String TAG = "BgDownloadService";
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    private RssApplication RssApp;
    private String stat;

    private RssFileDownTask thread;
    public BgDownloadService() {
        stat="init";
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public BgDownloadService getService() {
            // Return this instance of BgDownloadService so clients can call public methods
            return BgDownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RssApp = (RssApplication) getApplication();
        thread =new RssFileDownTask(RssApp.daoSession());
        Log.d(TAG, "onStartCommand: "+thread.curDwnlodStat());
        thread.start();
    }


    /**
     * @return
     */
    public String curDwnlodStat(){
        return thread.curDwnlodStat() ;
    }
    /**
     * already downloaded file amount
     * @return
     */
    public int curDwnlodNum(){
        return thread.curDwnlodNum();
    }
    /**
     * undownloaded files
     * @return
     */
    public long curUnDwnlodNum(){
        return thread.curUnDwnlodNum();
    }

    public long curErrorNum(){
        return thread.curErrorNum();
    }
}
