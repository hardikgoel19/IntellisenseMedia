package com.video.player.intellisensevideoview.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.video.player.intellisensevideoview.R;
import com.video.player.intellisensevideoview.utils.Holder;
import com.video.player.intellisensevideoview.utils.VideoViewParams;
import com.video.player.intellisensevideoview.utils.VideoViewSettings;
import com.video.player.intellisensevideoview.view.IntelligentVideoView;

import androidx.annotation.Nullable;

public class PIPService extends Service implements VideoViewParams {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //WINDOW MANAGER FOR VIEW HANDLING
    private WindowManager manager;

    //PIP MODE LAYOUT OBJECT
    private View pip;

    //EXO PLAYER
    private SimpleExoPlayer player;

    //EXO-PLAYER VIEW
    private PlayerView playerView;

    //PARAMETERS
    private String path;
    private String title;
    private long played;
    private VideoViewSettings settings;

    //LOAD INTENT PARAMETERS
    private void loadParameters(Intent intent) {

        //GET DATA FROM INTENT

        //GET PATH========================================================================
        path = intent.getStringExtra(VIDEO_PATH_URI);
        if (path == null || path.isEmpty()) {
            Toast.makeText(this, "No Video Path", Toast.LENGTH_SHORT).show();
            closeService();
        }
        //================================================================================

        //GET PLAYED========================================
        played = intent.getLongExtra(VIDEO_SEEK_TO, 0);
        //===================================================

        //GET TITLE
        title = intent.getStringExtra(VIDEO_TITLE);
        if (title == null || title.isEmpty()) {
            Toast.makeText(this, "No Video Title", Toast.LENGTH_SHORT).show();
        }

        //GET SETTINGS==============================================================================
        if (intent.hasExtra(VIDEO_VIEW_SETTINGS))
            this.settings = (VideoViewSettings) intent.getSerializableExtra(VIDEO_VIEW_SETTINGS);
        else
            this.settings = new VideoViewSettings();
        //==========================================================================================

    }

    //ON SERVICE IS STARTED
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (intent != null) {

            //LOAD INTENT PARAMETERS
            loadParameters(intent);

            Uri uri = Uri.parse(path);

            //AVOID MULTIPLE PIP WINDOWS
            if (manager != null && pip != null && pip.isShown() && player != null) {
                manager.removeView(pip);
                pip = null;
                manager = null;
                player.setPlayWhenReady(false);
                player.release();
                player = null;
            }

            //START MAKING PIP WINDOW

            //SET FLAGS ACCORDING TO PHONE TYPE
            WindowManager.LayoutParams layoutParams;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                layoutParams = new WindowManager.LayoutParams(
                        toPixel(settings.getPipWindowWidth()),
                        toPixel(settings.getPipWindowHeight()),
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );
            else
                layoutParams = new WindowManager.LayoutParams(
                        toPixel(settings.getPipWindowWidth()),
                        toPixel(settings.getPipWindowHeight()),
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );

            //POSITION FOR DEFAULT PIP
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.x = 100;
            layoutParams.y = 100;

            //INFLATE PIP VIEW
            pip = LayoutInflater.from(this).inflate(R.layout.pip_layout, null);

            //SET HEIGHT AND WIDTH OF PIP WINDOW
            RelativeLayout layout = pip.findViewById(R.id.relativeLayout);
            layout.setBackgroundColor(Color.parseColor(settings.getPipWindowColor()));

            //GET CLOSE BUTTON
            ImageView close = pip.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeService();
                }
            });

            //GET MAXIMIZE BUTTON
            ImageView maximum = pip.findViewById(R.id.maximize);
            maximum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Holder.object.getClass());
                    intent.putExtra(VideoViewParams.VIDEO_PATH_URI, path);
                    intent.putExtra(VideoViewParams.VIDEO_SEEK_TO, player.getCurrentPosition());
                    intent.putExtra(VideoViewParams.VIDEO_TITLE, title);
                    intent.putExtra(VideoViewParams.VIDEO_VIEW_SETTINGS, settings);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    closeService();
                }
            });

            //START WINDOW BY CALLING MANAGER
            manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            if (manager == null) {
                Toast.makeText(this, "Permission Denied for PIP", Toast.LENGTH_SHORT).show();
                closeService();
            }

            //ADD LAYOUT TO FLOATING WINDOW WITH PARAM
            manager.addView(pip, layoutParams);

            //EXOPLAYER CODING

            //SET EXOPLAYER STREAMING
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            //SONG PLAYING PARAMETERS
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            //CREATE SIMPLE EXOPLAYER INSTANCE
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            //FIND EXOPLAYER VIEW
            playerView = pip.findViewById(R.id.simpip);

            //PLAY VIDEO
            playVideo(uri);

            //HANDLE TOUCH EVENTS FOR DRAGGING
            touchHandler(layoutParams);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //RELEASE SERVICE AND CLOSE PIP MODE
    private void closeService() {
        if (manager != null && pip != null && pip.isShown() && player != null) {
            manager.removeView(pip);
            pip = null;
            manager = null;
            player.setPlayWhenReady(false);
            player.release();
            player = null;
        }

    }

    //PLAY VIDEO FOR GIVEN URI
    private void playVideo(Uri uri) {
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        String playerInfo = Util.getUserAgent(this, null);
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(this, playerInfo);
        MediaSource mediaSource = new ExtractorMediaSource(uri, defaultDataSourceFactory, extractorsFactory, null, null);

        playerView.setPlayer(player);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.seekTo(played);
    }

    //SET TOUCH HANDLER TO PIP WINDOW
    private void touchHandler(final WindowManager.LayoutParams layoutParams) {
        LinearLayout linearLayout = pip.findViewById(R.id.simpip2);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            private int iX, iY;
            private float iTX, iTY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iX = layoutParams.x;
                        iY = layoutParams.y;
                        iTX = event.getRawX();
                        iTY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = iX + (int) (event.getRawX() - iTX);
                        layoutParams.y = iY + (int) (event.getRawY() - iTY);
                        manager.updateViewLayout(pip, layoutParams);
                        return true;
                }
                return false;
            }
        });
    }

    //CONVERT DP TO PIXEL
    private int toPixel(int dp) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}

