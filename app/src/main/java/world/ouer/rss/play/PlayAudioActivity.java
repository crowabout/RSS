/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package world.ouer.rss.play;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import world.ouer.rss.ChannelType;
import world.ouer.rss.R;
import world.ouer.rss.RssApplication;
import world.ouer.rss.dao.AudioVideoItem;
import world.ouer.rss.dao.DataQueryTools;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.splithtml.HtmlExtractor;

/**
 * Allows playback of a single MP3 file via the UI. It contains a {@link MediaPlayerHolder}
 * which implements the {@link PlayerAdapter} interface that the activity uses to control
 * audio playback.
 */
public  class PlayAudioActivity extends AppCompatActivity {

    public static final String TAG = "PlayAudioActivity";
    public String MEDIA_RES_ID_PATH;

    protected TextView mTextDebug;
    protected SeekBar mSeekbarAudio;
    protected ScrollView mScrollContainer;
    protected PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    protected DataQueryTools dqt;
    protected RssItem item;
    protected TextView mTranscriptTv;
    protected HtmlExtractor extractor;

    protected final int MSG_LOGGING = 1;
    protected final int MSG_TRANSCRIPT = 2;
    protected final int MSG_LOADAUDIO= 3;

    protected ChannelType rssType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_audio_main);
        initializePlayResource();
        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
        Log.d(TAG, "onCreate: finished");


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mPlayerAdapter.release();
            Log.d(TAG, "onStop: release MediaPlayer");
        }
    }

    private void initializePlayResource() {

        dqt = new DataQueryTools(((RssApplication) getApplication()).daoSession());
        Intent data = getIntent();
        if (data == null) {
            return;
        }
        item = data.getParcelableExtra("rssitem");
        Log.d(TAG, "initializePlayResource: " + MEDIA_RES_ID_PATH);
    }


    protected void loadAudioFromEmptyEnclosure(String audioUrl) {
        loadAudioFromUri(audioUrl);
    }


    protected void loadAudioFromNotEmptyEnclosure() {
        //from local
        AudioVideoItem aviItem = dqt.findAvPathByUrl(item.getEnclosure());
        if (aviItem != null) {
            loadAudioFromLocal(aviItem.getStorePath());
        } else {
            loadAudioFromUri(item.getEnclosure());
        }
    }


    private void loadAudioFromLocal(String path) {
        MEDIA_RES_ID_PATH = path;
        mPlayerAdapter.loadMedia(MEDIA_RES_ID_PATH);
        mTextDebug.append("init audio from [local]\n");

    }

    private void loadAudioFromUri(String uripath) {
        //from url
        Uri uri = Uri.parse(uripath);
        mTextDebug.append(String.format("init audio from [url]\n"));
        mPlayerAdapter.loadMedia(uri);
    }

    private void initializeUI() {
        mTextDebug = (TextView) findViewById(R.id.text_debug);
        Button mPlayButton = (Button) findViewById(R.id.button_play);
        Button mPauseButton = (Button) findViewById(R.id.button_pause);
        Button mResetButton = (Button) findViewById(R.id.button_reset);
        mSeekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        mScrollContainer = (ScrollView) findViewById(R.id.scroll_container);
        mTranscriptTv = findViewById(R.id.transcript);

        mPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.pause();
                    }
                });
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.play();
                    }
                });
        mResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.reset();
                    }
                });
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        Log.d(TAG, "initializePlaybackController: created MediaPlayerHolder");
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Log.d(TAG, "initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGGING:
                    mTextDebug.append((String) msg.obj);
                    break;
                case MSG_TRANSCRIPT:
                    mTranscriptTv.append((String) msg.obj);
                    break;
                case MSG_LOADAUDIO:
                    String audio =(String)msg.obj;
                    loadAudioFromEmptyEnclosure(audio);
                    break;
            }

        }
    };


    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mSeekbarAudio.setProgress(position, true);
                } else {
                    mSeekbarAudio.setProgress(position);
                }

                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position));
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
        }

        @Override
        public void onLogUpdated(String message) {
            if (mTextDebug != null) {
                mTextDebug.append(message);
                mTextDebug.append("\n");
                // Moves the scrollContainer focus to the end.
                mScrollContainer.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
            }
        }
    }
}