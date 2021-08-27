package com.video.player.intellisensevideoview.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;
import com.video.player.intellisensevideoview.R;
import com.video.player.intellisensevideoview.camera.CameraFrontFaceDetectorHelper;
import com.video.player.intellisensevideoview.camera.OnFaceListener;
import com.video.player.intellisensevideoview.services.PIPService;
import com.video.player.intellisensevideoview.utils.Holder;
import com.video.player.intellisensevideoview.utils.VideoViewParams;
import com.video.player.intellisensevideoview.utils.VideoViewSettings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntelligentVideoView extends AppCompatActivity
        implements VideoViewParams, OnFaceListener, SensorEventListener {

    //CONSTANTS=====================================
    public static final long SEEK_TILT = 5000;
    public static final long SEEK_DOUBLE_TAP = 10000;
    //==============================================

    //OBJECT HOLDER=============
    private Object object = null;
    //==========================

    //SENSOR MANAGERS====================
    private SensorManager sensorManager;
    private Sensor sensor;
    //===================================


    //SETTING FOR VIDEO VIEW============
    private VideoViewSettings settings;
    //==================================


    //PARAMS REQUIRED FOR PLAYING VIDEO
    private long played = 0;
    private String path = "";
    private String title = "";
    //=================================


    //SIMPLE EXO PLAYER OBJECT==============
    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    //======================================


    //CAMERA FRONT HELPER===============================================
    private CameraFrontFaceDetectorHelper cameraFrontFaceDetectorHelper;
    //==================================================================

    //SOME RAW OBJECTS=================================================
    private boolean[] arr = new boolean[15];
    private int index = 0;
    private int temp1, temp2, i;
    //=================================================================

    public void setClass(Object object) {
        this.object = object;
        Holder.object = object;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SCREEN SETTINGS========================================================
        //FULL SCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //HIDE ACTION BAR
        if (getSupportActionBar() != null && getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
        //========================================================================

        //SET CONTENT============================
        setContentView(R.layout.basic_exo_player);
        //=======================================

        //INITIALIZE SENSORS
        initSensors();
        //=================

        //GET INTENT PARAMETERS===========================================================
        path = getIntent().getStringExtra(VIDEO_PATH_URI);
        if (path == null || path.isEmpty()) {
            Toast.makeText(this, "No Video Path", Toast.LENGTH_SHORT).show();
            finish();
        }

        title = getIntent().getStringExtra(VIDEO_TITLE);
        if (title == null || title.isEmpty()) {
            Toast.makeText(this, "No Video Title", Toast.LENGTH_SHORT).show();
            title = "Title Not Available";
        }

        played = getIntent().getLongExtra(VIDEO_SEEK_TO, 0);

        setSettings();
        //=================================================================================

        //INIT PLAYER======
        initializePlayer();
        //=================

        //INITIALIZE UI
        initViews();
        //=============

        //SET PATH AND PLAY
        setPath(path);
        //=================

        //START FACE DETECTION SERVICE
        initFaceDetection();
        //============================

    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else {
            Toast.makeText(this, "No Sensors Detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFaceDetection() {
        cameraFrontFaceDetectorHelper = new CameraFrontFaceDetectorHelper();
        cameraFrontFaceDetectorHelper.init(this);
        cameraFrontFaceDetectorHelper.setFaceListener(this);
    }

    private void initViews() {

        final GestureDetector gestureDetector = new GestureDetector(this, new PlayerGestureListener());

        //START DOUBLE TAP GESTURE LISTENER===================================
        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                return false;
            }
        });
        //====================================================================

        //SETTING PIP MODE BEHAVIOUR====================================================================
        ImageView pipMode = findViewById(R.id.exo_pip);
        pipMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings.isPIP()) {
                    Intent intent = new Intent(IntelligentVideoView.this, PIPService.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(VIDEO_PATH_URI, path);
                    intent.putExtra(VIDEO_SEEK_TO, simpleExoPlayer.getCurrentPosition());
                    intent.putExtra(VIDEO_TITLE, title);
                    intent.putExtra(VIDEO_VIEW_SETTINGS, settings);
                    startService(intent);
                    finish();
                } else {
                    Toast.makeText(IntelligentVideoView.this, "PIP Mode Not Allowed...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //==============================================================================================

        //SETTING FACE DETECTION BEHAVIOUR==========================================================
        final ImageView exo_face = findViewById(R.id.exo_face);
        exo_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settings.isFaceDetection()) {
                    settings.setFaceDetection(!settings.isFaceDetection());
                    exo_face.setImageResource(R.drawable.face_off);
                    cameraFrontFaceDetectorHelper.release();
                    Toast.makeText(IntelligentVideoView.this, "Face Detection Service Stopped", Toast.LENGTH_SHORT).show();
                } else {
                    settings.setFaceDetection(!settings.isFaceDetection());
                    exo_face.setImageResource(R.drawable.face_on);
                    initFaceDetection();
                    Toast.makeText(IntelligentVideoView.this, "Face Detection Service Started", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //==========================================================================================


        //SETTING EYES DETECTION BEHAVIOUR==========================================================
        final ImageView exo_eyes = findViewById(R.id.exo_eyes);
        exo_eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!settings.isFaceDetection()) {
                    Toast.makeText(IntelligentVideoView.this, "Cannot Start Without Face Detection Service", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (settings.isEyesDetection()) {
                    settings.setEyesDetection(!settings.isEyesDetection());
                    exo_eyes.setImageResource(R.drawable.eyes_off);
                    Toast.makeText(IntelligentVideoView.this, "Eyes Detection Service Stopped", Toast.LENGTH_SHORT).show();
                } else {
                    settings.setEyesDetection(!settings.isEyesDetection());
                    exo_eyes.setImageResource(R.drawable.eyes_on);
                    Toast.makeText(IntelligentVideoView.this, "Eyes Detection Service Started", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //==========================================================================================

        //SETTING TILT BUTTON BEHAVIOUR
        final ImageView tilt = findViewById(R.id.exo_tilt);
        tilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sensorManager == null) {
                    tilt.setImageResource(R.drawable.tilt_off);
                    settings.setTilt(false);
                    Toast.makeText(IntelligentVideoView.this, "No Sensors Detected", Toast.LENGTH_SHORT).show();
                }

                if (settings.isTilt()) {
                    settings.setTilt(!settings.isTilt());
                    tilt.setImageResource(R.drawable.tilt_off);
                    sensorManager.unregisterListener(IntelligentVideoView.this);
                    Toast.makeText(IntelligentVideoView.this, "Tilt Detection Service Stopped", Toast.LENGTH_SHORT).show();
                } else {
                    settings.setTilt(!settings.isTilt());
                    tilt.setImageResource(R.drawable.tilt_on);
                    sensorManager.registerListener(IntelligentVideoView.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                    Toast.makeText(IntelligentVideoView.this, "Tilt Detection Service Started", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //SET VIDEO TITLE================================
        TextView textView = findViewById(R.id.title);
        textView.setText(title);
        //===============================================


        //SET BACK BUTTON LISTENER===================================
        ImageView i = findViewById(R.id.back);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //===========================================================


    }

    public void setPath(String path) {
        //IF PATH IS EMPTY OR NULL EXIT ELSE START PLAYING VIDEO==========================
        if (path == null || path.isEmpty()) {
            Toast.makeText(this, "No Video Path", Toast.LENGTH_SHORT).show();
            finish();
        } else
            buildMediaSource(Uri.parse(path));
        //================================================================================
    }

    private void initializePlayer() {
        //EXO PLAYER OBJECTS
        playerView = findViewById(R.id.exoPlayer);
        if (simpleExoPlayer == null) {

            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, 16),
                    settings.getMinimumVideoLoadBuffer(),
                    settings.getMaximumVideoLoadBuffer(),
                    settings.MIN_PLAYBACK_START_BUFFER,
                    settings.MIN_PLAYBACK_RESUME_BUFFER,
                    -1, true);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl, null);
            playerView.setPlayer(simpleExoPlayer);
        }
    }

    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Video View"), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mUri);
        // Prepare the player with the source.
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(played);
    }

    //PLAYER METHODS - RELEASE RESOURCES ALLOCATED
    public void release() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        if (cameraFrontFaceDetectorHelper != null) {
            cameraFrontFaceDetectorHelper.release();
        }
    }

    //PLAYER METHODS - PAUSES PLAYER
    public void pause() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.getPlaybackState();
        }
    }

    //PLAYER METHODS - RESUMES PLAYER
    public void resume() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
        }
    }

    //PLAYER METHODS - RETURNS TRUE IS MEDIA IS PLAYING
    public boolean isPlaying() {
        return simpleExoPlayer != null
                && simpleExoPlayer.getPlaybackState() != Player.STATE_ENDED
                && simpleExoPlayer.getPlaybackState() != Player.STATE_IDLE
                && simpleExoPlayer.getPlayWhenReady();
    }

    //PLAYER METHODS - RETURN CURRENT POSITION (long) OF PLAYER
    public long getCurrentPosition() {
        return simpleExoPlayer.getCurrentPosition();
    }

    //GET SETTINGS - SETS SETTINGS TO GLOBAL OBJECT
    public void setSettings() {
        if (getIntent().hasExtra(VIDEO_VIEW_SETTINGS))
            this.settings = (VideoViewSettings) getIntent().getSerializableExtra(VIDEO_VIEW_SETTINGS);
        else
            this.settings = new VideoViewSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onface(Detector.Detections<Face> detections, final Face face) {

        if (index >= arr.length) index = 0;

        arr[index++] = true;

        temp1 = 0;
        temp2 = 0;
        for (i = 0; i < arr.length; i++) {
            if (arr[i]) temp1++;
            else temp2++;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (temp1 > temp2) {
                    if (settings.isEyesDetection()) {
                        if (face.getIsLeftEyeOpenProbability() >= settings.getEyesThreshold() ||
                                face.getIsRightEyeOpenProbability() >= settings.getEyesThreshold()) {
                            resume();
                        } else pause();
                    } else resume();
                } else pause();
            }
        });

    }

    @Override
    public void noface(Detector.Detections<Face> detections) {

        if (index >= arr.length) index = 0;

        arr[index++] = false;

        temp1 = 0;
        temp2 = 0;
        for (i = 0; i < arr.length; i++) {
            if (arr[i]) temp1++;
            else temp2++;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (temp1 < temp2) pause();
                else resume();
            }
        });

    }

    //TEMPORARY VARIABLES
    private boolean temp;
    //===================

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (simpleExoPlayer == null || !isPlaying()) return;

        float x = event.values[0];
        float y = event.values[1];
        if (Math.abs(x) > Math.abs(y)) {
            temp = true;
        } else {
            if (!temp) return;
            if (y < 0) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - SEEK_TILT);
            }
            if (y > 0) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + SEEK_TILT);
            }
            temp = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //NO USE
    }

    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {

        private int screenWidthPixels = getResources().getDisplayMetrics().widthPixels;

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if(simpleExoPlayer == null || !isPlaying()) return false;

            boolean b = e.getX() > screenWidthPixels * 0.5f;
            if (b)
                simpleExoPlayer.seekTo(getCurrentPosition() + SEEK_DOUBLE_TAP);
            else
                simpleExoPlayer.seekTo(getCurrentPosition() - SEEK_DOUBLE_TAP);
            return true;
        }
    }

}
