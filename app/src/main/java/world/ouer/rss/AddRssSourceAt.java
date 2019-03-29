package world.ouer.rss;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AddRssSourceAt extends AppCompatActivity {
    private static final String TAG = "AddRssSourceAt";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rss_source_at);
        setTitle(getString(R.string.addSource));


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv.append((String) msg.obj);
        }
    };
}
