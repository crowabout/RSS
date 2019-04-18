package world.ouer.rss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import world.ouer.rss.channel.IEmbedRss;
import world.ouer.rss.dao.DaoMaster;


/**
 * Created by pc on 2017/1/10.
 */
public class RssOpenHelper
        extends
        DaoMaster.DevOpenHelper {
    private static final String TAG ="RssOpenHelper" ;

    public RssOpenHelper(Context context, String name) {
        super(context, name);
    }

    public RssOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);


        String datetime="strftime(\'%Y%m%d%H%M\',\'now\')";

        String cnn="insert into sources values (null,\'"+ IEmbedRss.RSS_CNN+"\','CNN',"+datetime+","+IEmbedRss.RSS_CNN.hashCode()+",\'\')";
        String sc6="insert into sources values (null,\'"+ IEmbedRss.RSS_SCIENTIFIC_AMERICAN+"\','ScientificAmerican',"+datetime+","+IEmbedRss.RSS_SCIENTIFIC_AMERICAN.hashCode()+ ",\'\')";
        String ntp="insert into sources values (null,\'"+ IEmbedRss.RSS_NPR+"\','NPR',"+datetime+","+IEmbedRss.RSS_NPR.hashCode()+",\'\')";
        String reuters="insert into sources values (null,\'"+ IEmbedRss.RSS_REUTERS+"\','REUTERS',"+datetime+","+IEmbedRss.RSS_REUTERS.hashCode()+",\'\')";

        Log.d(TAG,String.format("cnn:%s\nsc6:%s\nntp:%s\nretu:%s",cnn,sc6,ntp,reuters));

        db.execSQL(cnn);
        db.execSQL(sc6);
        db.execSQL(ntp);
        db.execSQL(reuters);

    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //TODO 后续数据库升级，其中逻辑操作，就写在这里。
        //修改了模块build.gradle文件中的 greendao.schemaVersion 的值，会开始升级。
        //super.onUpgrade方法里，只是进行了删除表和重建表的操作。若是进行备份旧版本中表的数据
        //就要在调用super.onUpgrade 之前操作



        super.onUpgrade(db, oldVersion, newVersion);

    }
}
