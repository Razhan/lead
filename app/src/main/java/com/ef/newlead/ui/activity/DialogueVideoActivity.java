package com.ef.newlead.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
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

import butterknife.BindView;

/***
 * An Activity provides video interaction and ASR evaluation.
 */
public class DialogueVideoActivity extends BaseActivity implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener, VideoControlLayout.PlayingProgressChangeListener {

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

    private VideoControlLayout controlLayout;

    private boolean isRestarted = false;
    protected boolean pausedInOnStop = false;
    private boolean addedToFavorite = false;
    private VideoDialogueAdapter mAdapter;

    private Dialogue dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_video_role_play;
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
        initData();

        video.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                video.getLayoutParams().height = (int) (video.getWidth() / dialogue.getVideo().getRatio());
                video.requestLayout();
                video.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        progressbar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float[] array = {1f, 2f, 3f, 5f, 8f, 9f};
                progressbar.setDotsPosition(10f, array);
                progressbar.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        initVideoComponent();
        initRecyclerView();
    }

    private void initData() {
        String dialogueStr = getLocaleText("dialogue_example");
        dialogue = new Gson().fromJson(dialogueStr,
                new TypeToken<Dialogue>() {
                }.getType());
    }

    private void initRecyclerView() {
        list.setLayoutManager(new SmoothScrollLayoutManager(this));
        list.setItemAnimator(new SlideAnimator());

        mAdapter = new VideoDialogueAdapter(this, null);

        list.setAdapter(mAdapter);
    }

    @Override
    public void onPrepared() {
        video.start();

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
        video.setOnCompletionListener(() -> isRestarted = video.restart());

        // for testing only
        Uri uri = Uri.parse("file:///android_asset/test.mp4");
        video.setVideoURI(uri);
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
    }

}
