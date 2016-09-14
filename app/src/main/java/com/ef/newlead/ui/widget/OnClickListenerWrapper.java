package com.ef.newlead.ui.widget;

import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by seanzhou on 9/14/16.
 * <p>
 * A wrapper listener for {@link android.view.View.OnClickListener} which only allows single tap
 * during a short time (e.g. 1.5s).
 */
public class OnClickListenerWrapper implements View.OnClickListener {

    private static final long DEFAULT_MIN_INTERVAL = 1500;
    private final View.OnClickListener mListener;
    private long mLastClickTime = 0;

    public OnClickListenerWrapper(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        if (currentTime - mLastClickTime > DEFAULT_MIN_INTERVAL) {
            mListener.onClick(v);
            mLastClickTime = currentTime;
        }
    }
}
