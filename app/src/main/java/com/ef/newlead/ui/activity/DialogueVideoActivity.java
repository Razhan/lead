package com.ef.newlead.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.ActivityTemplate;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.presenter.VideoPresenter;
import com.ef.newlead.ui.adapter.VideoDialogueAdapter;
import com.ef.newlead.ui.view.VideoView;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.CheckProgressView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.SlideAnimator;
import com.ef.newlead.ui.widget.SmoothScrollLayoutManager;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.ef.newlead.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * An Activity provides video interaction.
 */
public class DialogueVideoActivity extends BaseMVPActivity<VideoPresenter> implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener,
        VideoControlLayout.PlayingProgressChangeListener,
        VideoView {

    public static final String KEY_LESSONID = "lessonID";

    @BindView(R.id.video_dialogue_video)
    AutoSizeVideoView video;
    @BindView(R.id.video_dialogue_progressbar)
    ColorfulProgressBar progress;
    @BindView(R.id.video_dialogue_list)
    RecyclerView list;
    @BindView(R.id.video_dialogue_hint)
    TextView hint;
    @BindView(R.id.video_dialogue_switch)
    SwitchCompat switcher;
    @BindView(R.id.video_dialogue_progress)
    CheckProgressView loadProgress;
    @BindView(R.id.video_dialogue_load_text)
    TextView loadText;
    @BindView(R.id.video_dialogue_load_wrapper)
    RelativeLayout loadWrapper;
    @BindView(R.id.video_dialogue_video_wrapper)
    LinearLayout videoWrapper;
    @BindView(R.id.video_dialogue_bottom_bar)
    CardView bottomBar;

    protected boolean pausedInOnStop = false;
    private boolean isRestarted = false;
    private boolean favored = false;

    private VideoDialogueAdapter mAdapter;

    //private ActivityTemplate activityTemplate;
    private List<Double> timestamps;
    private float duration;
    private int dialogueIndex = 0;
    private int stepIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_dialogue_video;
    }

    @Override
    protected String setToolBarText() {
        return "At the cafe";
    }

    @NonNull
    @Override
    protected VideoPresenter createPresenter() {
        return new VideoPresenter(this, this, null);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (toolbar != null) {
            toolbar.bringToFront();
        }

        setLoadWrapperBackground();
        loadProgress.post(() -> loadProgress.startAnim());

        initRecyclerView();

        video.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (presenter != null) {
                    // resize the video, respecting the given aspect ratio
                    video.getLayoutParams().height = (int) (video.getWidth() / presenter.getVideoRatio());
                    video.requestLayout();

                    video.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mAdapter.showTranslation(true);
            } else {
                mAdapter.showTranslation(false);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomBar.setCardElevation(ViewUtils.dpToPx(this, 5));
        }
    }

    private void setLoadWrapperBackground() {
        GradientColor color = new GradientColor(new GradientColor.GradientBean(248, 193, 68, 1),
                new GradientColor.GradientBean(246, 111, 159, 1), 0);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{color.getTopGradient().toHex(), color.getBottomGradient().toHex()});

        loadWrapper.setBackground(drawable);
    }

    private void initData() {
        timestamps = presenter.getTimeStamps();
    }

    private void initVideoComponent() {
        VideoControlLayout controlLayout = new VideoControlLayout(this);
        controlLayout.setVisibilityAnimationListener(this);
        controlLayout.setPlayingProgressChangeListener(this);
        controlLayout.centralizeControls(true);

        video.setControls(controlLayout);
        video.setMeasureBasedOnAspectRatioEnabled(false);
        video.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        video.setScaleType(ScaleType.NONE); // works for width match_parent
        video.setOnCompletionListener(() -> {
            progress.reset();
            dialogueIndex = 0;
            stepIndex = 0;

            controlLayout.reset();
            video.restart();

            video.postDelayed(this::toDialogueList, 500);
        });

        video.setVideoURI(presenter.getVideo());
    }

    private void initRecyclerView() {
        list.setLayoutManager(new SmoothScrollLayoutManager(this));
        list.setItemAnimator(new SlideAnimator());

        mAdapter = new VideoDialogueAdapter(this, null);

        list.setAdapter(mAdapter);
    }

    @Override
    public void onPrepared() {
        if (isRestarted) {
            isRestarted = false;
            stopVideo();
        }

        duration = (float) video.getDuration() / 1000;
        progress.setDotsPosition(duration, timestamps);

        video.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        resumeVideoPosition();
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
            resumeVideoPosition();

            video.start();
            pausedInOnStop = false;
        }
    }

    private void resumeVideoPosition() {
        // reset the position to avoid a un-refreshed video screen
        if (video.getCurrentPosition() > 0) {
            video.seekTo(video.getCurrentPosition() + 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.release();
        video = null;
    }

    @Override
    public void onAnimate(boolean visible) {
        progress.setThumb(visible);

        if (toolbar == null)
            return;

        if (visible) {
            toolbar.bringToFront();
            toolbar.animate()
                    .translationY(0)
                    .alpha(1)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            toolbar.bringToFront();
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .alpha(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();
        }
    }

    @Override
    public void onUpdate(float progress) {
        this.progress.setProgress(progress);

        if (dialogueIndex >= presenter.getDialogSize()) {
            return;
        }

        List<ActivityTemplate.DialogBean> beans = presenter.getDialogsByIndex(dialogueIndex);
        ActivityTemplate.DialogBean bean = beans.get(stepIndex);

        if (duration * progress >= (float) bean.getStartTime()) {
            mAdapter.add(mAdapter.getItemCount(), bean);
            list.smoothScrollToPosition(mAdapter.getItemCount());

            stepIndex++;

            if (stepIndex == beans.size()) {
                ActivityTemplate.DialogBean lastBean = beans.get(beans.size() - 1);

                new Handler().postDelayed(() -> mAdapter.removeAll(),
                        (long) (lastBean.getEndTime() - lastBean.getStartTime()) * 1000);
            }

            if (stepIndex >= beans.size()) {
                dialogueIndex++;
                stepIndex = 0;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_dialog_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                if (favored) {
                    item.setIcon(R.drawable.ic_favorite_empty);
                } else {
                    item.setIcon(R.drawable.ic_favorite_fill);
                }
                favored = !favored;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterScoreSubmitted() {
    }

    @Override
    public void updateLoadProgress(int progress) {
        loadText.setText("正在载入 " + String.valueOf(progress) + "%");
    }

    @Override
    public void afterLoaded() {
        loadProgress.startAnim();

        initData();
        initVideoComponent();

        loadWrapper.animate()
                .alpha(0)
                .translationY(-loadWrapper.getHeight())
                .setDuration(Constant.DEFAULT_ANIM_FULL_TIME )
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        progress.setVisibility(View.VISIBLE);
                        videoWrapper.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    private void toDialogueList() {
        Intent i = new Intent(this, DialogueListActivity.class);
        i.putParcelableArrayListExtra(DialogueListActivity.DIALOGUE_KEY, new ArrayList<>(presenter.getAllDialogueBeans()));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        video.pause();

        showDialog("Are you sure you want to quit?", "QUIT", "CANCEL",
                (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                },
                (dialog, which) -> {
                    dialog.cancel();
                    video.start();
                });
    }
}
