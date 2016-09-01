package com.ef.newlead.ui.activity;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.widget.ASRProgressView;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener, VideoControlLayout.PlayingProgressChangeListener {

    @BindView(R.id.b1)
    Button b1;
    @BindView(R.id.b2)
    Button b2;
    @BindView(R.id.b3)
    Button b3;
    @BindView(R.id.video_role_asr_progress)
    ASRProgressView asrProgress;
    @BindView(R.id.video_role_video)
    AutoSizeVideoView video;
    @BindView(R.id.video_role_progressbar)
    ColorfulProgressBar videoProgress;
    @BindView(R.id.video_role_replay)
    ImageView replay;
    @BindView(R.id.video_role_cover)
    View cover;
    @BindView(R.id.video_role_asr_wrapper)
    LinearLayout asrWrapper;
    @BindView(R.id.video_role_deny_title)
    TextView denyTitle;
    @BindView(R.id.video_role_deny_info)
    TextView denyInfo;
    @BindView(R.id.video_role_retry)
    Button retry;
    @BindView(R.id.video_role_deny_wrapper)
    LinearLayout denyWrapper;

    private boolean isRestarted = false;
    protected boolean pausedInOnStop = false;

    private Dialogue dialogue;
    private List<Double> timestamps;
    private float duration;
    private int stepIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initData();
        initVideoComponent();

        video.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                video.getLayoutParams().height = (int) (video.getWidth() / dialogue.getVideo().getRatio());
                video.requestLayout();

                cover.getLayoutParams().height = (int) (video.getWidth() / dialogue.getVideo().getRatio());
                cover.requestLayout();

                video.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        asrProgress.setListener(new ASRProgressView.ProgressListener() {
            @Override
            public void onProgressEnd() {
                //stop asrProgress后回掉，在这处理asr， 在asr最后的处理结果中，调用asrProgress.setResult
                asrProgress.setResult(true, "Prefect!!");
            }

            @Override
            public void onResultEnd() {
                //asrProgress最后动画结束后回掉
                videoProgress.setVisibility(View.VISIBLE);
                cover.setVisibility(View.INVISIBLE);
                video.getVideoControls().setVisibility(View.VISIBLE);
                replay.setVisibility(View.INVISIBLE);
                video.start();
            }
        });
    }

    private void initData() {
        String dialogueStr = getLocaleText("dialogue_example");
        dialogue = new Gson().fromJson(dialogueStr,
                new TypeToken<Dialogue>() {
                }.getType());

        timestamps = new ArrayList<>();

        for (List<Dialogue.DialogBean> beans : dialogue.getDialogs()) {
            if (beans == null) {
                break;
            }

            for (Dialogue.DialogBean bean : beans) {
                timestamps.add(bean.getStartTime());
            }
        }
    }

    protected void initVideoComponent() {
        VideoControlLayout controlLayout = new VideoControlLayout(this);
        controlLayout.setVisibilityAnimationListener(this);
        controlLayout.setPlayingProgressChangeListener(this);
        controlLayout.centralizeControlViewLayout();

        video.setControls(controlLayout);
        video.setMeasureBasedOnAspectRatioEnabled(false);
        video.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        video.setScaleType(ScaleType.NONE); // works for width match_parent
        video.setOnCompletionListener(() -> isRestarted = video.restart());

        // for testing only
        Uri uri = Uri.parse("file:///android_asset/test.mp4");
        video.setVideoURI(uri);
    }

    @Override
    public void onPrepared() {
        if (isRestarted) {
            isRestarted = false;
            stopVideo();
        }

        duration = (float) video.getDuration() / 1000;
        videoProgress.setDotsPosition(duration, timestamps);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVideo();
    }

    private void stopVideo() {
        if (video.isPlaying()) {
            pausedInOnStop = true;
            video.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (pausedInOnStop) {
            video.start();
            pausedInOnStop = false;
        }

        askForPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.release();
    }

    @Override
    public void onAnimate(boolean visible) {
        if (toolbar == null)
            return;

        if (visible) {
            toolbar.bringToFront();
            toolbar.animate()
                    .translationY(0)
                    .alpha(1)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            videoProgress.setThumb(true);
            showStatusBar(true);
        } else {
            toolbar.bringToFront();
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .alpha(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();
            videoProgress.setThumb(false);
            showStatusBar(false);
        }
    }

    @Override
    public void onUpdate(float progress) {
        this.videoProgress.setProgress(progress);

        if (stepIndex < timestamps.size() && duration * progress >= timestamps.get(stepIndex)) {
            stepIndex++;

            video.pause();
            videoProgress.setVisibility(View.INVISIBLE);
            cover.setVisibility(View.VISIBLE);
            replay.setVisibility(View.VISIBLE);
            video.getVideoControls().setVisibility(View.INVISIBLE);
            asrProgress.show();
        }
    }

    @OnClick(R.id.video_role_replay)
    public void onReplayClick(View view) {
        video.seekTo(previousPosition());

        videoProgress.setVisibility(View.VISIBLE);
        cover.setVisibility(View.INVISIBLE);
        replay.setVisibility(View.INVISIBLE);
        video.getVideoControls().setVisibility(View.VISIBLE);

        asrProgress.hide();
        video.start();
    }

    @OnClick(R.id.video_role_retry)
    public void onRetryClick(View view) {
        askForPermission();
    }

    private void askForPermission() {
        askForPermissions(new PermissionListener() {
            @Override
            public void permissionGranted() {
                video.getVideoControls().setVisibility(View.VISIBLE);
                denyWrapper.setVisibility(View.GONE);
                asrWrapper.setVisibility(View.VISIBLE);

                video.start();
            }

            @Override
            public void permissionDenied() {
                video.getVideoControls().setVisibility(View.INVISIBLE);
                denyWrapper.setVisibility(View.VISIBLE);
            }
        }, Manifest.permission.RECORD_AUDIO);
    }

    private int previousPosition() {
        int timestamp = 0;

        if (stepIndex - 2 >= 0) {
            timestamp = (int) (timestamps.get(stepIndex - 2) * 1000);
        }

        stepIndex--;
        return timestamp;
    }

    @OnClick({R.id.b1, R.id.b2, R.id.b3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                asrProgress.startCountDown(10);
                break;
            case R.id.b2:
                asrProgress.stopCountDown();
                break;
            case R.id.b3:
                break;
        }
    }

}
