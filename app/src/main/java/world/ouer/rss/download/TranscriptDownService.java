package world.ouer.rss.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import world.ouer.rss.RssApplication;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.DataQueryTools;


/**
 * Created by pc on 2019/3/26.
 */
public class TranscriptDownService extends Service {

    private final String TAG = "TranscriptDownService";
    private final IBinder binder = new LocalBinder();
    private DaoSession session;
    private DataQueryTools dqt;
    private RssApplication app;
    TranscriptDownLoader down;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate start service: ");
        app = (RssApplication) getApplication();
        this.session = app.daoSession();
        dqt = new DataQueryTools(this.session);
        down= new TranscriptDownLoader(dqt);
        down.start();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public TranscriptDownService getService() {
            // Return this instance of BgDownloadService so clients can call public methods
            return TranscriptDownService.this;
        }
    }

    public String debugTxt(){
        return  down.debugStr();
    }


    public String downloadStata(){
        return down.downloadStata();
    }

}
