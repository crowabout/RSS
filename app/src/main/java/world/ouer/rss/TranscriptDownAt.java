package world.ouer.rss;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import world.ouer.rss.download.TranscriptDownService;

public class TranscriptDownAt extends AppCompatActivity {

    @BindView(R.id.stat)
    TextView stat;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.scrollV)
    ScrollView scrollView;
    private boolean autoScroll2Bottom = true;

    TranscriptDownService mService;
    boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initVari();
    }

    private void initVari() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    updateDisplay();
                } else {
                    Toast.makeText(mService, "mBound==false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    autoScroll2Bottom = false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    autoScroll2Bottom=true;
                }

                return false;
            }
        });
    }

    private void updateDisplay() {
        stat.setText(mService.downloadStata());
        info.setText(mService.debugTxt());
        if (autoScroll2Bottom) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    if (scrollView != null) {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, TranscriptDownService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(connection);
            mBound = false;
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TranscriptDownService.LocalBinder binder = (TranscriptDownService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
