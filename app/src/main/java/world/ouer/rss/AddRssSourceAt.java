package world.ouer.rss;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import world.ouer.rss.splithtml.HtmlExtractor;
import world.ouer.rss.splithtml.NTPHtmlExtractor;

public class AddRssSourceAt extends AppCompatActivity {
    private static final String TAG = "AddRssSourceAt";
    String link = "https://www.npr.org/2019/03/24/706295341/a-very-important-study-on-cheese-and-hip-hop?utm_medium=RSS&utm_campaign=science";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rss_source_at);
        setTitle(getString(R.string.addSource));

        tv = findViewById(R.id.transcript);

        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final HtmlExtractor extractor = new NTPHtmlExtractor(url);
        extractor.setLoadFinishListener(new HtmlExtractor.LoadFinishListener() {
            @Override
            public void notifyLoadOk() {
                String audioUrl = extractor.obtainAudioUrl();
                String subtitle = extractor.obtainSubtitle();
                handler.obtainMessage(1, String.format("%s\n\n%s", audioUrl, subtitle)).sendToTarget();
                Log.d(TAG, String.format("%s\n\n%s", audioUrl, subtitle));
            }
        });
        extractor.extract();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv.append((String) msg.obj);
        }
    };
}
