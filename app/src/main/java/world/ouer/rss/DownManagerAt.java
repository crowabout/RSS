package world.ouer.rss;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import world.ouer.rss.download.BgDownloadService;

public class DownManagerAt extends AppCompatActivity {
    @BindView(R.id.stat)
    TextView stat;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.update)
    Button update;

    BgDownloadService mService;
    boolean mBound = false;

    @BindView(R.id.scrollV)
    ScrollView scrollView;

    private boolean autoScroll2Bottom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        initVari();
        Intent intent = new Intent(this, BgDownloadService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private final int PERMISSION_REQUEST=2;
    private void checkPermission(){
        if(PackageManager.PERMISSION_GRANTED
                != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PERMISSION_REQUEST&&requestCode== Activity.RESULT_OK){

            RssUtils.creatStorePlace();


        }
    }

    private void initVari() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    updateDisplay();
                } else {
                    Toast.makeText(DownManagerAt.this, "mBound==false", Toast.LENGTH_SHORT).show();
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
        stat.setText(String.format(getString(R.string.stat),
                mService.curErrorNum(),
                mService.curDwnlodNum(),
                mService.curUnDwnlodNum()
        ));
        info.append(mService.curDwnlodStat());
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
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }



    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BgDownloadService.LocalBinder binder = (BgDownloadService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
