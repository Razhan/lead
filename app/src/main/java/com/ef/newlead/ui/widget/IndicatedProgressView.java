package com.ef.newlead.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.util.ViewUtils;

public class IndicatedProgressView extends View {

    public static final int STATE_ANIM_NONE = 0;
    public static final int STATE_ANIM_START = 1;
    public static final int STATE_ANIM_STOP = 2;

    private static final float DEFAULT_ANIM_START = 0;
    private static final float DEFAULT_ANIM_END = 1;

    private int mAngle = 10;
    private static final float threshold = 0.35f;
    private static final float factor = 1.5f;

    private int mState = STATE_ANIM_NONE;
    private ValueAnimator mValueAnimator;
    private float mProgress;
    private int radius;
    private float cx, cy, markStart;
    private float line1_x, line1_y, line2_x, line2_y;
    private float ringWidth;

    private RectF mRectF;
    private Paint mPaint;

    public IndicatedProgressView(Context context) {
        this(context, null);
    }

    public IndicatedProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatedProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatedProgressView);
        ringWidth = mTypedArray.getDimension(R.styleable.IndicatedProgressView_progressRingWidth,
                ViewUtils.dpToPx(getContext(), 2));

        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setOnClickListener(v -> startAnim());
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

        mRectF.set(cx - radius, cy - radius, cx + radius, cy + radius);
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
                break;
            case STATE_ANIM_START:
                drawLoopView(canvas);
                break;
            case STATE_ANIM_STOP:
                drawStartAnimView(canvas);
                break;
        }
    }

    private void drawLoopView(Canvas canvas) {
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        canvas.save();
        mAngle += 13;
        canvas.rotate(mAngle, cx, cy);
        canvas.drawArc(mRectF, 0, 270, false, mPaint);
        canvas.restore();
    }

    private void drawStartAnimView(Canvas canvas) {
        line1_x = mProgress * radius * factor;
        line1_y = line1_x;
        line2_x = line1_x;
        line2_y = line1_y;

        float length = Math.min(line1_x, radius * threshold * factor);
        canvas.drawLine(markStart, cx, markStart + length, cx + length, mPaint);

        if (mProgress >= threshold) {
            line2_y = (length - radius * (mProgress - threshold) * factor);
            canvas.drawLine(markStart + length - ringWidth / 2, cx + length, markStart + line2_x, cx + line2_y, mPaint);
        }
    }

    public void startAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }

        if (mState == STATE_ANIM_NONE) {
            mState = STATE_ANIM_START;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, Constant.DEFAULT_ANIM_FULL_TIME, true);
        } else if (mState == STATE_ANIM_START) {
            mState = STATE_ANIM_STOP;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, Constant.DEFAULT_ANIM_FULL_TIME, false);
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
            IndicatedProgressView.this.invalidate();
        });

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }

}
