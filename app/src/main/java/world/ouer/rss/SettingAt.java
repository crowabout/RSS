package world.ouer.rss;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.DataQueryTools;
import world.ouer.rss.dao.SubtitleDao;

public class SettingAt extends AppCompatActivity {

    @BindView(R.id.delSubtitle)
    Button tv;

    private SubtitleDao sbdao;

    @BindView(R.id.delCNN)
     Button delAllCNN;

    DataQueryTools dqt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_at);
        ButterKnife.bind(this);
        DaoSession session =((RssApplication)getApplication()).daoSession();
        sbdao=session.getSubtitleDao();

        dqt=new DataQueryTools(session);
    }


    @OnClick(R.id.delSubtitle)
    public void deleteSubtable(View view){
        sbdao.deleteAll();
        Toast.makeText(this, "delete all subtitle record!!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.delCNN)
    public void deleteAllCNN(){
        dqt.deleAllCNNRecord();
        Toast.makeText(this, "already delete all CNN record!!", Toast.LENGTH_SHORT).show();
    }
}
