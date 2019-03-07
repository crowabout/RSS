package world.ouer.rss;

import android.app.Application;
import org.greenrobot.greendao.database.Database;

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
    }





}
