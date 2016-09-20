package com.ef.newlead.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.util.ViewUtils;

public class ASRProgressView extends RelativeLayout {

    private static final int REMAIN_TIME = 1000;
    private static final int DEFAULT_BACKGROUND = Color.parseColor("#33000000");

    private float mProgress = -1;
    private ValueAnimator mValueAnimator;
    private Paint mPaint;
    private TextView mTextView;

    private ProgressListener listener;
    private String initText = "Read the sentences below.";

    public ASRProgressView(Context context) {
        this(context, null);
    }

    public ASRProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ASRProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mTextView = new TextView(getContext());
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mTextView.setTextColor(Color.WHITE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.setMarginStart(ViewUtils.dpToPx(getContext(), 24));

        addView(mTextView, params);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mProgress >= 0) {
            mPaint.setColor(Color.parseColor("#4c0078ff"));
            RectF progress = new RectF(0, 0, mProgress * getWidth(), getHeight());
            canvas.drawRect(progress, mPaint);

            mPaint.setColor(Color.parseColor("#0078ff"));
            RectF cursor = new RectF(mProgress * getWidth(), 0,
                    mProgress * getWidth() + ViewUtils.dpToPx(getContext(), 1), getHeight());
            canvas.drawRect(cursor, mPaint);
        }
    }

    public void show() {
        setBackgroundColor(DEFAULT_BACKGROUND);
        mTextView.setText(initText);
        inAndOutAnimation(getHeight(), 0);
    }

    public void hide() {
        mValueAnimator = null;
        mProgress = -1;

        animate().translationY(getHeight()).setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();
    }

    public void startCountDown(long time) {
        mProgress = 0;

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(time * 1000);
        mValueAnimator.setInterpolator(new LinearInterpolator());

        mValueAnimator.addUpdateListener(animator -> {
            mProgress = (float) animator.getAnimatedValue();
            ASRProgressView.this.invalidate();
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onProgressEnd(mProgress);
                }
            }
        });

        if (!mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }
    }

    public void stopCountDown() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void setResult(boolean result, String text) {
        stopCountDown();

        if (result) {
            setBackgroundColor(Color.parseColor("#74d17f"));
        } else {
            setBackgroundColor(Color.parseColor("#de4c4c"));
        }

        mTextView.setText(text);

        mProgress = -1;
        invalidate();

        postDelayed(() -> inAndOutAnimation(0, getHeight()), REMAIN_TIME);
    }

    public void setListener(ProgressListener listener) {
        this.listener = listener;
    }

    private void inAndOutAnimation(float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, start, end);

        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    if (end > 0) {
                        listener.onResultEnd();
                    }
                }
            }
        });

        animator.start();
    }

    public void setInitText(String initText) {
        this.initText = initText;
    }

    public interface ProgressListener {
        void onProgressEnd(float progress);

        void onResultEnd();
    }
}
