package world.ouer.rss.play;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.BindView;
import butterknife.ButterKnife;
import world.ouer.rss.R;

public class PlayVideoAt extends AppCompatActivity {

    @BindView(R.id.subtitleTv)
    TextView subtitle;

    @BindView(R.id.videoView)
    VideoView vv;

    @BindView(R.id.videoLy)
    LinearLayout videLy;

    @BindView(R.id.progress)
    ProgressBar pb;
    String url;
    private MediaController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_av_with_subtitle_at);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        url = getIntent().getStringExtra("url");
        controller = new MediaController(this);
        vv.setMediaController(controller);
        vv.setVideoURI(Uri.parse(url));
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vv=null;
    }
}
