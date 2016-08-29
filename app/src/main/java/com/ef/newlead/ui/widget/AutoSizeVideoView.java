package com.ef.newlead.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.devbrackets.android.exomedia.core.EMListenerMux;
import com.devbrackets.android.exomedia.core.api.VideoViewApi;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

/**
 * Created by seanzhou on 8/26/16.
 * <p>
 * A custom {@link EMVideoView} which is also aware of video size change event.
 */
public class AutoSizeVideoView extends EMVideoView {
    public AutoSizeVideoView(Context context) {
        super(context);
    }

    public AutoSizeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoSizeVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface VideoSizeMonitor {
        void onSizeChanged(int displayWidth, int displayHeight, float pixelWidthHeightRatio);
    }

    private VideoSizeMonitor videoSizeMonitor;

    public AutoSizeVideoView setVideoSizeMonitor(VideoSizeMonitor videoSizeMonitor) {
        this.videoSizeMonitor = videoSizeMonitor;
        return this;
    }

    @Override
    protected void initView(Context context, @Nullable AttributeSet attrs) {
        //super.initView(context, attrs);

        if (!isInEditMode()) {
            inflateVideoView(context, attrs);

            previewImageView = (ImageView) findViewById(com.devbrackets.android.exomedia.R.id.exomedia_video_preview_image);
            videoViewImpl = (VideoViewApi) findViewById(com.devbrackets.android.exomedia.R.id.exomedia_video_view);

            muxNotifier = new MuxNotifierAuto();
            listenerMux = new EMListenerMux(muxNotifier);

            videoViewImpl.setListenerMux(listenerMux);
        }
    }

    class MuxNotifierAuto extends MuxNotifier {
        @Override
        public void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees, float pixelWidthHeightRatio) {
            super.onVideoSizeChanged(width, height, unAppliedRotationDegrees, pixelWidthHeightRatio);

            if (videoSizeMonitor != null) {
                videoSizeMonitor.onSizeChanged(width, height, pixelWidthHeightRatio);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        videoSizeMonitor = null;

        super.onDetachedFromWindow();
    }
}
