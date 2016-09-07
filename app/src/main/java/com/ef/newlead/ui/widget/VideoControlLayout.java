package com.ef.newlead.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.devbrackets.android.exomedia.ui.animation.BottomViewHideShowAnimation;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.util.TimeFormatUtil;
import com.ef.newlead.R;
import com.ef.newlead.util.ViewUtils;

/**
 * Created by seanzhou on 8/24/16.
 * <p>
 * A custom control layout for {@link com.devbrackets.android.exomedia.ui.widget.EMVideoView}.
 */
public class VideoControlLayout extends VideoControls {

    RelativeLayout videoTimeStampLayout;
    ColorfulProgressBar progressBar;
    private boolean userInteracting;
    private SeekBar seekBar;
    private long videoDuration;
    private LinearLayout controlParent;
    private VisibilityAnimationListener visibilityAnimationListener;
    private PlayingProgressChangeListener playingProgressChangeListener;

    private boolean manualPaused = false;

    public boolean isManualPaused() {
        return manualPaused;
    }

    public VideoControlLayout(Context context) {
        super(context);
    }

    public VideoControlLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoControlLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoControlLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public VideoControlLayout setPlayingProgressChangeListener(PlayingProgressChangeListener listener) {
        this.playingProgressChangeListener = listener;
        return this;
    }

    public VideoControlLayout setVisibilityAnimationListener(VisibilityAnimationListener visibilityAnimationListener) {
        this.visibilityAnimationListener = visibilityAnimationListener;
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        visibilityAnimationListener = null;
        playingProgressChangeListener = null;

        super.onDetachedFromWindow();
    }

    /***
     * Centralizes the play button after video is ready.
     *
     * @param statusBarShown whether to display the status bar.
     */
    public void centralizeControls(boolean statusBarShown) {

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (VideoControlLayout.this.getHeight() > 0 && controlParent.getHeight() > 0) {
                    int statusBarHeight = 0;
                    if (ViewUtils.hasKitKat() && statusBarShown) {
                        statusBarHeight = ViewUtils.getStatusBarHeight(getContext());
                    }

                    // centralize the play button
                    int topSpace = statusBarHeight + (VideoControlLayout.this.getHeight() - controlParent.getHeight()) / 2;
                    LayoutParams params = (LayoutParams) (controlParent.getLayoutParams());
                    params.setMargins(params.leftMargin, topSpace, params.rightMargin, params.bottomMargin);

                    controlParent.requestLayout();
                    controlParent.invalidate();
                    VideoControlLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void retrieveViews() {
        super.retrieveViews();
        seekBar = (SeekBar) findViewById(R.id.exomedia_controls_video_seek);

        progressBar = (ColorfulProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(GONE);

        videoTimeStampLayout = (RelativeLayout) findViewById(R.id.timestamp_parent);

        controlParent = (LinearLayout) findViewById(R.id.control_parent);
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        seekBar.setOnSeekBarChangeListener(new SeekBarChanged());
    }

    @Override
    public void setPosition(@IntRange(from = 0) long position) {
        currentTime.setText(TimeFormatUtil.formatMs(position));
        seekBar.setProgress((int) position);

        notifyProgressChange(position);
    }

    @Override
    public void setDuration(@IntRange(from = 0) long duration) {
        videoDuration = duration;

        if (duration != seekBar.getMax()) {
            endTime.setText(TimeFormatUtil.formatMs(duration));
            seekBar.setMax((int) duration);
        }
    }

    @Override
    public void updateProgress(@IntRange(from = 0) long position, @IntRange(from = 0) long duration, @IntRange(from = 0, to = 100) int bufferPercent) {
        if (!userInteracting) {
            seekBar.setSecondaryProgress((int) (seekBar.getMax() * ((float) bufferPercent / 100)));
            seekBar.setProgress((int) position);
            currentTime.setText(TimeFormatUtil.formatMs(position));

            notifyProgressChange(position);
            //progressBar.setProgress(progress);
        }
    }

    private void notifyProgressChange(@IntRange(from = 0) long position) {
        if (videoDuration > 0) {
            float progress = position * 1.0f / videoDuration;
            if (playingProgressChangeListener != null) {
                playingProgressChangeListener.onUpdate(progress);
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.widget_video_custom_control;
    }

    @Override
    public void hideDelayed(long delay) {
        hideDelay = delay;

        if (delay < 0 || !canViewHide || isLoading) {
            return;
        }

        //If the user is interacting with controls we don't want to start the delayed hide yet
        if (!userInteracting) {
            visibilityHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateVisibility(false);
                }
            }, delay);
        }
    }

    @Override
    protected void animateVisibility(boolean toVisible) {
        if (isVisible == toVisible) {
            return;
        }

        /*if (!hideEmptyTextContainer || !isTextContainerEmpty()) {
            textContainer.startAnimation(new TopViewHideShowAnimation(textContainer, toVisible, CONTROL_VISIBILITY_ANIMATION_LENGTH));
        }*/

        if (!isLoading) {
            if (visibilityAnimationListener != null) {
                visibilityAnimationListener.onAnimate(toVisible);
            }
            controlsContainer.startAnimation(new BottomViewHideShowAnimation(controlsContainer, toVisible, CONTROL_VISIBILITY_ANIMATION_LENGTH));
        }

        isVisible = toVisible;
        onVisibilityChanged();
    }

    @Override
    protected void updateTextContainerVisibility() {

    }

    @Override
    public void showLoading(boolean initialLoad) {
        if (isLoading) {
            return;
        }

        isLoading = true;
        controlsContainer.setVisibility(View.GONE);
        //seekBar.setVisibility(GONE);
        //videoTimeStampLayout.setVisibility(GONE);
        loadingProgress.setVisibility(View.VISIBLE);
        loadingProgress.bringToFront();

        show();
    }

    @Override
    public void finishLoading() {
        if (!isLoading) {
            return;
        }

        isLoading = false;
        controlsContainer.setVisibility(View.VISIBLE);
        //seekBar.setVisibility(VISIBLE);
        //videoTimeStampLayout.setVisibility(VISIBLE);
        loadingProgress.setVisibility(View.GONE);

        updatePlaybackState(videoView != null && videoView.isPlaying());
    }

    /***
     * Listener for monitoring the control view visibility change.
     */
    public interface VisibilityAnimationListener {
        void onAnimate(boolean visible);
    }

    /***
     * Listener for monitoring the playing progress
     */
    public interface PlayingProgressChangeListener {
        void onUpdate(float progress);
    }

    class SeekBarChanged implements SeekBar.OnSeekBarChangeListener {
        private int seekToTime;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            seekToTime = progress;
            if (currentTime != null) {
                currentTime.setText(TimeFormatUtil.formatMs(seekToTime));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            userInteracting = true;
            if (seekListener == null || !seekListener.onSeekStarted()) {
                internalListener.onSeekStarted();
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            userInteracting = false;
            if (seekListener == null || !seekListener.onSeekEnded(seekToTime)) {
                internalListener.onSeekEnded(seekToTime);
            }
        }
    }

    @Override
    protected void setup(Context context) {
        super.setup(context);

        // intercept with our own listener for basic functionality of video player.
        internalListener = new VideoControlListener();
    }

    class VideoControlListener extends InternalListener {

        @Override
        public boolean onPlayPauseClicked() {
            if (videoView != null) {
                manualPaused = videoView.isPlaying();
            }
            return super.onPlayPauseClicked();
        }
    }
}
