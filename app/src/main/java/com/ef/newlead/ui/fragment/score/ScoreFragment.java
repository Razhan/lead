package com.ef.newlead.ui.fragment.score;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ef.newlead.R;
import com.ef.newlead.ui.activity.RemindActivity;
import com.ef.newlead.ui.fragment.BaseFragment;
import com.ef.newlead.ui.widget.AutoSizeVideoView;
import com.ef.newlead.ui.widget.ColorfulProgressBar;
import com.ef.newlead.ui.widget.VideoControlLayout;
import com.ef.newlead.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ScoreFragment extends BaseFragment implements OnPreparedListener,
        VideoControlLayout.VisibilityAnimationListener,
        VideoControlLayout.PlayingProgressChangeListener {

    protected boolean pausedInOnStop = false;
    protected boolean isCreated = false;
    @BindView(R.id.score_close)
    ImageView close;
    @BindView(R.id.score_video)
    AutoSizeVideoView video;
    @BindView(R.id.score_share_text)
    TextView shareText;
    @BindView(R.id.score_share_icon)
    ImageView shareIcon;
    @BindView(R.id.score_share)
    CardView share;
    @BindView(R.id.score_title)
    TextView title;
    @BindView(R.id.score_info)
    TextView info;
    @BindView(R.id.score_next_button)
    Button next;
    @BindView(R.id.score_progressbar)
    ColorfulProgressBar progress;
    private boolean isRestarted = false;
    private List<Double> timestamps;

    public static ScoreFragment newInstance() {
        return new ScoreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_score;
    }

    @Override
    public void initView() {
        initData();
        initVideoComponent();

        next.getBackground().setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            share.setCardElevation(ViewUtils.dpToPx(getContext(), 5));
        }
    }

    private void initData() {
//        String dialogueStr = getLocaleText("dialogue_example");
//        ActivityTemplate dialogue = new Gson().fromJson(dialogueStr,
//                new TypeToken<ActivityTemplate>() {
//                }.getType());
//
//        timestamps = new ArrayList<>();
//
//        for (List<ActivityTemplate.DialogBean> beans : dialogue.getDialogs()) {
//            if (beans == null) {
//                break;
//            }
//
//            for (ActivityTemplate.DialogBean bean : beans) {
//                timestamps.add(bean.getStartTime());
//            }
//        }
    }

    private void initVideoComponent() {
        VideoControlLayout controlLayout = new VideoControlLayout(getContext());
        controlLayout.setVisibilityAnimationListener(this);
        controlLayout.setPlayingProgressChangeListener(this);
        controlLayout.centralizeControls(false);

        video.setControls(controlLayout);
        video.setMeasureBasedOnAspectRatioEnabled(false);
        video.setOnPreparedListener(this);

        // https://github.com/brianwernick/ExoMedia/issues/1
        video.setScaleType(ScaleType.NONE); // works for width match_parent
        video.setOnCompletionListener(() -> {
            isRestarted = video.restart();
//            progress.reset();
        });

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

        float duration = (float) video.getDuration() / 1000;
//        progress.setDotsPosition(duration, timestamps);

        video.seekTo(100);
    }

    @Override
    public void onUpdate(float progress) {
        this.progress.setProgress(progress);
    }

    @Override
    public void onAnimate(boolean visible) {
        progress.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progress.setElevation(ViewUtils.dpToPx(getContext(), 15));
        }

        progress.setThumb(visible);

        if (visible) {
            close.setVisibility(View.VISIBLE);
        } else {
            close.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }

        if (!isVisibleToUser) {
            resumeVideoPosition();
            stopVideo();
        } else {
            if (pausedInOnStop) {
                resumeVideoPosition();

                video.start();
                pausedInOnStop = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        resumeVideoPosition();
        stopVideo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        video.release();
    }

    private void stopVideo() {
        if (video.isPlaying()) {
            pausedInOnStop = true;
            video.pause();
        }
    }

    private void resumeVideoPosition() {
        // reset the position to avoid a un-refreshed video screen
        if (video.getCurrentPosition() > 0) {
            video.seekTo(video.getCurrentPosition() + 1);
        }
    }

    @OnClick({R.id.score_close, R.id.score_share_icon, R.id.score_next_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.score_close:
                getActivity().onBackPressed();
                break;
            case R.id.score_share_icon:
                shareText("");
                break;
            case R.id.score_next_button:
                Intent i = new Intent(getContext(), RemindActivity.class);
                i.putExtra(RemindActivity.REMIND_TYPE_KEY, RemindActivity.TYPE_ADD);
                startActivity(i);
                break;
        }
    }

    private void shareText(String share) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, share);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
    }

    @Override
    public void onStart() {
        super.onStart();

        //FIXME: the video related UI should be extracted out as a basic component.
        Timber.d(">>> onStart() invoked");
        if (video != null) {
            resumeVideoPosition();
        }

    }
}
