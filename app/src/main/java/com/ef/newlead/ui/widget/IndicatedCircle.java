package com.ef.newlead.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ef.newlead.R;
import com.ef.newlead.util.ViewUtils;

public class IndicatedCircle extends View {

    public static final int STATE_ANIM_NONE = 0;
    public static final int STATE_ANIM_START = 1;
    public static final int STATE_ANIM_STOP = 2;
    private static final int DEFAULT_ANIM_FULL_TIME = 300;
    private static final float DEFAULT_ANIM_START = 0;
    private static final float DEFAULT_ANIM_END = 1;
    private static final float threshold = 0.30f;
    private static final float sizeFactor = 0.8f;
    protected int mState = STATE_ANIM_NONE;

    private Paint firstPaint;
    private Paint secondPaint;

    private int ringUnselectedColor;
    private int ringColor;
    private float ringWidth;
    private float cx, cy, markStart;
    private float line1_x, line1_y, line2_x, line2_y;
    private int radius;
    private RectF rectF;
    private ValueAnimator mValueAnimator;

    private float mProgress;

    public IndicatedCircle(Context context) {
        this(context, null);
    }

    public IndicatedCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatedCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        firstPaint = new Paint();
        secondPaint = new Paint();
        rectF = new RectF();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatedCircle);

        ringColor = mTypedArray.getColor(R.styleable.IndicatedCircle_ringColor, Color.RED);
        ringUnselectedColor = mTypedArray.getColor(R.styleable.IndicatedCircle_ringUnselectedColor, Color.BLACK);
        ringWidth = mTypedArray.getDimension(R.styleable.IndicatedCircle_ringWidth, ViewUtils.dpToPx(getContext(), 2));

        mTypedArray.recycle();

        firstPaint.setColor(ringUnselectedColor);
        firstPaint.setStyle(Paint.Style.STROKE);
        firstPaint.setStrokeWidth(ringWidth);
        firstPaint.setAntiAlias(true);

        secondPaint.setColor(ringColor);
        secondPaint.setStyle(Paint.Style.STROKE);
        secondPaint.setStrokeWidth(ringWidth);
        secondPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(400, widthMeasureSpec);
        int height = measureDimension(200, heightMeasureSpec);

        int finalSize = Math.min(width, height);
        setMeasuredDimension(finalSize, finalSize);

        cx = finalSize / 2;
        cy = finalSize / 2;
        radius = (int) (cx - ringWidth / 2);
        markStart = cx - getWidth() / 5;

        rectF.set(cx - radius, cy - radius, cx + radius, cy + radius);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
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
        switch (mState) {
            case STATE_ANIM_NONE:
                drawNormalView(canvas);
                break;
            case STATE_ANIM_START:
                drawStartAnimView(canvas);
                break;
            case STATE_ANIM_STOP:
                drawStopAnimView(canvas);
                break;
        }
    }

    private void drawNormalView(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, firstPaint);
    }

    private void drawStartAnimView(Canvas canvas) {
        tick(canvas);
    }

    private void drawStopAnimView(Canvas canvas) {
        mProgress = 1 - mProgress;
        tick(canvas);
    }

    private void tick(Canvas canvas) {
        line1_x = mProgress * radius * sizeFactor;
        line1_y = line1_x;
        line2_x = line1_x;
        line2_y = line1_y;

        canvas.drawArc(rectF, -90, 360 * (1 - mProgress), false, firstPaint);

        canvas.drawArc(rectF, -90, -360 * mProgress, false, secondPaint);

        float length = Math.min(line1_x, radius * threshold);
        canvas.drawLine(markStart, cx, markStart + length, cx + length, secondPaint);

        if (mProgress >= threshold) {
            line2_y = (length - radius * (mProgress - threshold)) * sizeFactor;
            canvas.drawLine(markStart + length - ringWidth / 2, cx + length, markStart + line2_x, cx + line2_y, secondPaint);
        }
    }

    public void startAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }

        if (mState == STATE_ANIM_NONE) {
            mState = STATE_ANIM_START;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, DEFAULT_ANIM_FULL_TIME, false);
        } else if (mState == STATE_ANIM_START) {
            mState = STATE_ANIM_STOP;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, DEFAULT_ANIM_FULL_TIME, false);
        } else {
            mState = STATE_ANIM_NONE;
            startAnim();
        }
    }

    public ValueAnimator startViewAnim(float startF, float endF, long time, boolean isRepeat) {
        mProgress = 0;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startF, endF);

        if (isRepeat) {
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animator -> {
            mProgress = (float) animator.getAnimatedValue();
            IndicatedCircle.this.invalidate();
        });

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }
}

