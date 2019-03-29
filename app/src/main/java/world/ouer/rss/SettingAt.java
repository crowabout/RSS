package world.ouer.rss;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.SubtitleDao;

public class SettingAt extends AppCompatActivity {

    @BindView(R.id.delete)
    TextView tv;

    private SubtitleDao sbdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_at);
        ButterKnife.bind(this);
        DaoSession session =((RssApplication)getApplication()).daoSession();
        sbdao=session.getSubtitleDao();

    }

    public void deleteSubtable(View view){
        sbdao.deleteAll();
        Toast.makeText(this, "delete all!!", Toast.LENGTH_SHORT).show();
    }
}
