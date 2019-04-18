package world.ouer.rss;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.greendao.database.Database;

import world.ouer.rss.dao.DaoMaster;
import world.ouer.rss.dao.DaoSession;

/**
 * Created by pc on 2019/3/7.
 */

public class RssApplication extends Application {

    private DaoSession daoSession;
    public static String schema="RSS";
    @Override
    public void onCreate() {
        super.onCreate();
        RssOpenHelper helper = new RssOpenHelper(this, schema);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        FileDownloader.setup(this);

    }


    public DaoSession daoSession(){
        return daoSession;
    }





}
