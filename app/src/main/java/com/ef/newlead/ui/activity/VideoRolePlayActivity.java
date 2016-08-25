package com.ef.newlead.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.ef.newlead.R;
import com.ef.newlead.ui.widget.VideoControlLayout;

import java.io.File;

/***
 * An Activity provides video interaction and ASR evaluation.
 */
public class VideoRolePlayActivity extends AppCompatActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener {

    private EMVideoView videoView;

    protected boolean pausedInOnStop = false;
    private boolean isRestarted = false;

    private VideoControlLayout controlLayout;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_role_play);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("At the cafe");
        }

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                Toast.makeText(this, "Added to favorite list.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepared() {
        videoView.start();

        if (isRestarted) {
            isRestarted = false;
            stopVideo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVideo();
    }

    private void stopVideo() {
        if (videoView.isPlaying()) {
            pausedInOnStop = true;
            videoView.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (pausedInOnStop) {
            videoView.start();
            pausedInOnStop = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }

    protected void init() {
        videoView = (EMVideoView) findViewById(R.id.video_play_activity_video_view);
        controlLayout = new VideoControlLayout(this);
        controlLayout.setVisibilityAnimationListener(this);
        videoView.setControls(controlLayout);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) controlLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        videoView.setMeasureBasedOnAspectRatioEnabled(false);
        videoView.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        videoView.setScaleType(ScaleType.NONE); // works for width match_parent
        videoView.setOnCompletionListener(() -> isRestarted = videoView.restart());

        // for testing only
        String filePath = "./sdcard/test.mp4";
        videoView.setVideoURI(Uri.fromFile(new File(filePath)));
    }

    @Override
    public void onAnimate(boolean visible) {
        if (actionBar == null)
            return;

        if (visible) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }
}
