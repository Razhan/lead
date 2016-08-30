package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.adapter.VideoDialogueAdapter;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.SlideAnimator;
import com.ef.newlead.ui.widget.SmoothScrollLayoutManager;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * An Activity provides video interaction.
 */
public class DialogueVideoActivity extends BaseActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener, VideoControlLayout.PlayingProgressChangeListener {

    protected boolean pausedInOnStop = false;
    @BindView(R.id.video_dialogue_favorite)
    ImageView favorite;
    @BindView(R.id.video_dialogue_video)
    AutoSizeVideoView video;
    @BindView(R.id.video_dialogue_progressbar)
    ColorfulProgressBar progressbar;
    @BindView(R.id.video_dialogue_list)
    RecyclerView list;
    @BindView(R.id.video_dialogue_hint)
    TextView hint;
    @BindView(R.id.video_dialogue_switch)
    SwitchCompat switcher;

    private boolean isRestarted = false;
    private boolean favored = false;

    private VideoDialogueAdapter mAdapter;

    private Dialogue dialogue;
    private List<Double> timestamps;
    private float duration;
    private int dialogueIndex = 0;
    private int stepIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_dialogue_video;
    }

    @Override
    protected boolean showBackIcon() {
        return true;
    }

    @Override
    protected String setToolBarText() {
        return "At the cafe";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (toolbar != null) {
            toolbar.bringToFront();
        }

        initData();
        initVideoComponent();
        initRecyclerView();

        video.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                video.getLayoutParams().height = (int) (video.getWidth() / dialogue.getVideo().getRatio());
                video.requestLayout();
                video.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mAdapter.showTranslation(true);
            } else {
                mAdapter.showTranslation(false);
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
        progressbar.setDotsPosition(duration, timestamps);

        video.start();
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
            progressbar.setThumb(true);
        } else {
            toolbar.bringToFront();
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .alpha(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();
            progressbar.setThumb(false);
        }
    }

    @Override
    public void onUpdate(float progress) {
        progressbar.setProgress(progress);

        if (dialogueIndex >= dialogue.getDialogs().size()) {
            return;
        }

        List<Dialogue.DialogBean> beans = dialogue.getDialogs().get(dialogueIndex);
        Dialogue.DialogBean bean = beans.get(stepIndex);

        if (duration * progress >= (float) bean.getStartTime()) {
            mAdapter.add(mAdapter.getItemCount(), bean);
            list.smoothScrollToPosition(mAdapter.getItemCount());

            stepIndex++;

            if (stepIndex == beans.size()) {
                Dialogue.DialogBean lastBean = beans.get(beans.size() - 1);

                new Handler().postDelayed(() -> mAdapter.removeAll(),
                        (long) (lastBean.getEndTime() - lastBean.getStartTime()) * 1000 + 1000);
            }

            if (stepIndex >= beans.size()) {
                dialogueIndex++;
                stepIndex = 0;
            }
        }
    }

    @OnClick({R.id.video_dialogue_favorite, R.id.video_dialogue_hint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_dialogue_favorite:
                if (favored) {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_empty);
                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_full);
                }
                favored = !favored;
                break;
            case R.id.video_dialogue_hint:
                Intent i = new Intent(this, DialogueListActivity.class);
                i.putParcelableArrayListExtra("AllDialogueBeans", getAllDialogueBeans());
                startActivity(i);
                break;
        }
    }

    private ArrayList<Dialogue.DialogBean> getAllDialogueBeans() {
        ArrayList<Dialogue.DialogBean> allBeans = new ArrayList<>();

        for (List<Dialogue.DialogBean> beans : dialogue.getDialogs()) {
            if (beans == null) {
                break;
            }

            for (Dialogue.DialogBean bean : beans) {
                allBeans.add(bean);
            }
        }

        return allBeans;
    }
}
