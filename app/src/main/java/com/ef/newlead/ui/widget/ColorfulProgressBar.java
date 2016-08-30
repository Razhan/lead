package com.ef.newlead.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ef.newlead.Constant;
import com.ef.newlead.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ColorfulProgressBar extends View {

    private final static int DEFAULT_BAR_COLOR = Color.parseColor("#4dffffff");
    private final static int DEFAULT_CIRCLE_COLOR = Color.parseColor("#80ffffff");
    private final static int DEFAULT_DOT_RADIUS = 2;
    private final static int DEFAULT_THUMB_RADIUS = 8;

    private float cy;
    private int barHeight;
    private GradientDrawable gradientDrawable;
    private Paint mPaint;

    private boolean showThumb = false;
    private float mProgress = 0;
    private float mAnimationProgress = -1;
    private int currentDot = -1;
    private float length;

    private List<Float> dotsRatio;

    public ColorfulProgressBar(Context context) {
        this(context, null);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor("#fff66f9f"), Color.parseColor("#fff8c144")});

        barHeight = ViewUtils.dpToPx(getContext(), 4);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultHeight = ViewUtils.dpToPx(getContext(), (DEFAULT_THUMB_RADIUS + 1) * 2);

        int width = measureDimension(widthMeasureSpec, 800);
        int height = measureDimension(heightMeasureSpec, defaultHeight /*200*/);

        setMeasuredDimension(width, height);

        cy = height / 2;
        length = getMeasuredWidth() /*getWidth()*/ - getPaddingStart() - getPaddingEnd();
    }

    private int measureDimension(int measureSpec, int defaultSize) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawProgressBar(canvas);
        drawColorfulProgressBar(canvas);
        drawDots(canvas);

        if (showThumb) {
            drawThumb(canvas);
        }

        if (mAnimationProgress >= 0) {
            drawHalo(canvas);
        }
    }

    private void drawColorfulProgressBar(Canvas canvas) {
        gradientDrawable.setBounds(getPaddingStart(), (int) cy - barHeight / 2,
                (int) mProgress, (int) cy + barHeight / 2);

        gradientDrawable.draw(canvas);
    }

    private void drawProgressBar(Canvas canvas) {
        mPaint.setColor(DEFAULT_BAR_COLOR);
        canvas.drawRect(getPaddingStart(), cy - barHeight / 2, getWidth() - getPaddingEnd(),
                cy + barHeight / 2, mPaint);
    }

    private void drawDots(Canvas canvas) {
        if (dotsRatio == null) {
            return;
        }

        mPaint.setColor(DEFAULT_CIRCLE_COLOR);
        int circleRadius = ViewUtils.dpToPx(getContext(), DEFAULT_DOT_RADIUS);

        for (float ratio : dotsRatio) {
            float absPos = length * ratio + getPaddingStart();
            canvas.drawCircle(absPos, cy, circleRadius, mPaint);
        }
    }

    private void drawThumb(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        int circleRadius = ViewUtils.dpToPx(getContext(), DEFAULT_THUMB_RADIUS);

        int x = getPaddingStart() + circleRadius;
        x = Math.max((int) mProgress, x);
        x = Math.min(x, getWidth() - getPaddingEnd() - circleRadius);

        canvas.drawCircle(x, cy, circleRadius, mPaint);
    }

    private void drawHalo(Canvas canvas) {

        float x = getPaddingStart() + dotsRatio.get(currentDot) * length;
        float maxRadius = ViewUtils.dpToPx(getContext(), 12);

        mPaint.setColor(Color.parseColor(getGradientAlpha(mAnimationProgress, 0.7f)));
        canvas.drawCircle(x, cy, maxRadius * mAnimationProgress, mPaint);

        if (mAnimationProgress >= 0.5f) {
            mPaint.setColor(Color.parseColor(getGradientAlpha(((mAnimationProgress - 0.5f) * 2), 1f)));
            canvas.drawCircle(x, cy, maxRadius * (mAnimationProgress - 0.5f), mPaint);
        }
    }

    private String getGradientAlpha(float progress, float threshold) {
        if (progress >= 1) {
            return "#00ffffff";
        }

        int alpha = (int) ((1 - progress) * 255 * threshold);
        return "#" + String.format("%02X", alpha) + "ffffff";
    }

    public void setDotsPosition(float total, List<Double> stamps) {
        dotsRatio = new ArrayList<>();
        for (Double stamp : stamps) {
            dotsRatio.add((float) (stamp / total));
        }
        invalidate();
    }

    public void setThumb(boolean showThumb) {
        this.showThumb = showThumb;
        invalidate();
    }

    public boolean isShowThumb() {
        return showThumb;
    }

    /***
     * Sets the progress value
     *
     * @param progress range from 0 to 1.
     */
    public void setProgress(float progress) {

        mProgress = getPaddingStart() + length * progress;

        if (dotsRatio != null && currentDot + 1 < dotsRatio.size() &&
                progress >= dotsRatio.get(currentDot + 1)) {
            currentDot++;
            startViewAnim(Constant.DEFAULT_ANIM_FULL_TIME * 3);
        } else {
            invalidate();
        }
    }

    public void reset() {
        mProgress = 0;
        currentDot = -1;
        showThumb = false;
    }

    public ValueAnimator startViewAnim(long time) {
        mAnimationProgress = 0;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);

        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new DecelerateInterpolator());

        valueAnimator.addUpdateListener(animator -> {
            mAnimationProgress = (float) animator.getAnimatedValue();
            ColorfulProgressBar.this.invalidate();
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationProgress = -1;
            }
        });

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }
}
