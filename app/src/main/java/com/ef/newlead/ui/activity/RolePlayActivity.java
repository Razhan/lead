package com.ef.newlead.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.R;
import com.ef.newlead.asr.DroidASRComponent;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.widget.ASRProgressView;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.MicrophoneVolumeView;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RolePlayActivity extends BaseActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener, VideoControlLayout.PlayingProgressChangeListener {

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
    @BindView(R.id.recorder_button)
    Button recordBtn;
    @BindView(R.id.script)
    TextView script;
    @BindView(R.id.microphone_volume)
    MicrophoneVolumeView microphoneView;

    private DroidASRComponent asrComponent;

    private boolean isRestarted = false;
    protected boolean pausedInOnStop = false;

    private Dialogue dialogue;
    private List<Double> timestamps;
    private float duration;
    private int stepIndex = 0;
    private boolean recordCountingDown = false;
    private VideoControlLayout controlLayout;

    private boolean timeOut = false;
    private boolean asrStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        asrComponent = new DroidASRComponent();

        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_role_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initData();
        initVideoComponent();
        initAsrComponent();

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
            public void onProgressEnd(float progress) {
                recordCountingDown = false;

                if (progress >= 1f){
                    timeOut = true;

                    if (!asrStarted) {
                        asrProgress.setResult(false, "Time Out!!");
                    }
                }
            }

            @Override
            public void onResultEnd() {
                recordBtn.setEnabled(false);

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
        controlLayout = new VideoControlLayout(this);
        controlLayout.setVisibilityAnimationListener(this);
        controlLayout.setPlayingProgressChangeListener(this);
        controlLayout.centralizeControlViewLayout();

        video.setControls(controlLayout);
        video.setMeasureBasedOnAspectRatioEnabled(false);
        video.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        video.setScaleType(ScaleType.NONE); // works for width match_parent
        video.setOnCompletionListener(
                () -> {
                    isRestarted = video.restart();

                    videoProgress.reset();
                    stepIndex = 0;
                }
        );

        // for testing only
        Uri uri = Uri.parse("file:///android_asset/test.mp4");
        video.setVideoURI(uri);
    }

    public void initAsrComponent() {

        recordBtn.setOnTouchListener((View v, MotionEvent event) ->
                {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        recordBtn.setPressed(true);
                        recordBtn.setBackgroundResource(R.drawable.mic_tapping);
                        onRecordStart();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        recordBtn.setPressed(false);
                        recordBtn.setBackgroundResource(R.drawable.mic);
                        onRecordComplete();
                        return true;
                    } else {
                        recordBtn.setPressed(false);
                        onRecordCancel();
                        return false;
                    }
                }
        );

        // testing sentence for "I'm fine. And you?" with options: {"Yes, she's great!", "Nice to meet you, too."}
        asrComponent.setAsrWords(Joiner.on(" ").join(Arrays.asList("BVPRC", "UGNFA", "PIZNB", "JUHKF")));
        asrComponent.setOptions(Arrays.asList(
                Joiner.on(" ").join(Arrays.asList("BVPRC", "UGNFA", "PIZNB", "JUHKF")),
                Joiner.on(" ").join(Arrays.asList("BYJWB", "ICGQA", "UDMEP")),
                Joiner.on(" ").join(Arrays.asList("ZWGKN", "ZBPGW", "HXTZZ", "LRXBB", "YBCLD"))
        ));
        asrComponent.setDictionary("BVPRC  AY M\nJUHKF  Y UW\nPIZNB  AE N D\nPIZNB  AH N D\nUGNFA  F AY N\n"
                + "BYJWB  Y EH S\nICGQA  SH IY S\nICGQA  SH IY Z\nUDMEP  G R EY T\n" +
                "HXTZZ  M IY T\nLRXBB  Y UW\nYBCLD  T UW\nZBPGW  T AH\nZBPGW  T IH\nZBPGW  T UH\nZBPGW  T UW\nZWGKN  N AY S\n");

        asrComponent.setResultListener(new DroidASRComponent.AsrResultListener() {
            @Override
            public void onSucceed() {
                onHandleAsrResult(true);
            }

            @Override
            public void onFailure() {
                onHandleAsrResult(false);
            }

            @Override
            public void onSampleLevelChanged(short level) {
                microphoneView.setProportion(level);
            }
        });
    }

    private void onHandleAsrResult(boolean successful) {
        runOnUiThread(() -> {
            if (timeOut) {
                asrProgress.setResult(false, "Time Out!!");
            } else if (successful) {
                asrProgress.setResult(true, "Prefect!!");
            } else {
                asrProgress.setResult(false, "Please try again.");
            }

            timeOut = false;
        });
    }

    private void onRecordCancel() {
        //asrComponent.stopRecording();
    }

    private void onRecordComplete() {
        asrProgress.stopCountDown();

        microphoneView.setVisibility(View.INVISIBLE);
        microphoneView.setProportion(0);
        asrComponent.stopRecording();
        asrStarted = false;
    }

    private void onRecordStart() {
        microphoneView.setVisibility(View.VISIBLE);
        asrComponent.startRecording();

        asrStarted = true;
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
    protected void onPause() {
        super.onPause();
        asrProgress.stopCountDown();
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
            //video.start();
            pausedInOnStop = false;
        }

        if (controlLayout != null && controlLayout.isManualPaused()) {
            resumeVideoPosition();
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            denyWrapper.setVisibility(View.GONE);
            asrWrapper.setVisibility(View.VISIBLE);

            if (recordCountingDown) {
                // continue with last counting down
                recordCountingDown = false;
                asrProgress.stopCountDown();
                asrProgress.startCountDown(10);

                resumeVideoPosition();
            } else {

                resumeVideoPosition();
                startPlayingVideo();
            }
        } else {
            askForPermission();
        }
    }

    private void resumeVideoPosition() {
        // reset the position to avoid a un-refreshed video screen
        if (video.getCurrentPosition() > 0) {
            video.seekTo(video.getCurrentPosition() + 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.release();

        asrComponent.setResultListener(null);
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

            Toast.makeText(RolePlayActivity.this, "It's your turn now", Toast.LENGTH_SHORT).show();
            recordBtn.setEnabled(true);
            recordCountingDown = true;
            asrProgress.startCountDown(10);
        }
    }

    @OnClick(R.id.video_role_replay)
    public void onReplayClick(View view) {
        video.seekTo(previousPosition());

        videoProgress.setVisibility(View.VISIBLE);
        cover.setVisibility(View.INVISIBLE);
        replay.setVisibility(View.INVISIBLE);
        video.getVideoControls().setVisibility(View.VISIBLE);

        asrProgress.stopCountDown();
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
                startPlayingVideo();
            }

            @Override
            public void permissionDenied() {
                video.getVideoControls().setVisibility(View.INVISIBLE);
                denyWrapper.setVisibility(View.VISIBLE);
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void startPlayingVideo() {
        video.getVideoControls().setVisibility(View.VISIBLE);
        denyWrapper.setVisibility(View.GONE);
        asrWrapper.setVisibility(View.VISIBLE);

        video.start();
    }

    private int previousPosition() {
        int timestamp = 0;

        if (stepIndex - 2 >= 0) {
            timestamp = (int) (timestamps.get(stepIndex - 2) * 1000);
        }

        stepIndex--;
        return timestamp;
    }
}
