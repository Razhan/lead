package com.ef.newlead.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ef.newlead.R;
import com.ef.newlead.Constant;

public class TransmutableView extends View {

    public static final int STATE_ANIM_NONE = 0;
    public static final int STATE_ANIM_LOOP = 1;
    public static final int STATE_ANIM_START = 2;
    public static final int STATE_ANIM_STOP = 3;

    private static final float DEFAULT_ANIM_START = 0;
    private static final float DEFAULT_ANIM_END = 1;

    private int mState = STATE_ANIM_NONE;
    private int mColorTran = R.color.tranBlack;
    private int mAngle = 10;

    private Paint mPaint, mFontPaint;
    private float cx, cy, cr;
    private RectF mRectF, mRectF2;
    private float mCirCleDis = 150;
    private Paint.FontMetricsInt fontMetrics;
    private ValueAnimator mValueAnimator;
    private float mProgress = -1;

    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;

    public TransmutableView(Context context) {
        this(context, null);
    }

    public TransmutableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransmutableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.transmutableView, 0, 0);
        mTitleText = type.getString(R.styleable.transmutableView_text);
        mTitleTextSize = type.getDimensionPixelSize(R.styleable.transmutableView_text_size, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mTitleTextColor = type.getColor(R.styleable.transmutableView_text_color, Color.WHITE);
        type.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRectF = new RectF();
        mRectF2 = new RectF();

        mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFontPaint.setStrokeWidth(1);
        mFontPaint.setColor(mTitleTextColor);
        mFontPaint.setStyle(Paint.Style.FILL);
        mFontPaint.setTextSize(mTitleTextSize);
        mFontPaint.setTextAlign(Paint.Align.CENTER);

        fontMetrics = mFontPaint.getFontMetricsInt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mState) {
            case STATE_ANIM_NONE:
                break;
            case STATE_ANIM_LOOP:
                drawLoopView(canvas);
                break;
            case STATE_ANIM_START:
                drawStartAnimView(canvas);
                break;
            case STATE_ANIM_STOP:
                drawStopAnimView(canvas);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(400, widthMeasureSpec);
        int height = measureDimension(200, heightMeasureSpec);

        cr = height / 3;
        cx = width / 2;
        cy = height / 2;

        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
        mRectF2.top = cy - cr;
        mRectF2.bottom = cy + cr;

        setMeasuredDimension(width, height);
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

    private void drawLoopView(Canvas canvas) {
        mPaint.setColor(ContextCompat.getColor(getContext(), mColorTran));
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, cr, mPaint);

        mRectF.left = cx - cr;
        mRectF.right = cx + cr;
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;

        canvas.save();
        mPaint.setColor(Color.WHITE);
        mAngle += 10;
        canvas.rotate(mAngle, cx, cy);
        canvas.drawArc(mRectF, 0, 20, false, mPaint);
        canvas.restore();
    }

    private void drawStartAnimView(Canvas canvas) {
        canvas.save();

        if (mProgress <= 0.25) {
            canvas.drawCircle(cx, cy, cr, mPaint);
        } else if (mProgress > 0.25f && mProgress <= 0.80f) {
            mRectF.left = cx - cr + mCirCleDis * mProgress;
            mRectF.right = cx + cr + mCirCleDis * mProgress;
            mRectF2.left = cx - cr - mCirCleDis * mProgress;
            mRectF2.right = cx + cr - mCirCleDis * mProgress;

            canvas.drawArc(mRectF, 90, -180, false, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom, mPaint);
            canvas.drawArc(mRectF2, 90, 180, false, mPaint);
        } else {
            float baseline = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;

            if (mProgress > 0.80f && mProgress <= 0.85f) {
                canvas.drawText("点", cx, baseline, mFontPaint);
            } else if (mProgress > 0.85f && mProgress <= 0.90f) {
                canvas.drawText("点击", cx, baseline, mFontPaint);
            } else if (mProgress > 0.90f && mProgress <= 0.95f) {
                canvas.drawText("点击开", cx, baseline, mFontPaint);
            } else if (mProgress > 0.95f && mProgress <= 1.0f) {
                canvas.drawText("点击开始", cx, baseline, mFontPaint);
            }

            canvas.drawArc(mRectF, 90, -180, false, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom, mPaint);
            canvas.drawArc(mRectF2, 90, 180, false, mPaint);
        }

        canvas.restore();
    }

    private void drawStopAnimView(Canvas canvas) {
        canvas.save();

        if (mProgress >= 0.8) {
            canvas.drawCircle(cx, cy, cr, mPaint);
        } else {
            mRectF.left = cx - cr + mCirCleDis * (0.80f - mProgress);
            mRectF.right = cx + cr + mCirCleDis * (0.80f - mProgress);
            mRectF2.left = cx - cr - mCirCleDis * (0.80f - mProgress);
            mRectF2.right = cx + cr - mCirCleDis * (0.80f - mProgress);

            canvas.drawArc(mRectF, 90, -180, false, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, mPaint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    mPaint);
            canvas.drawArc(mRectF2, 90, 180, false, mPaint);
        }

        canvas.restore();
    }

    public void startAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }

        if (mState == STATE_ANIM_NONE) {
            mState = STATE_ANIM_LOOP;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, Constant.DEFAULT_ANIM_FULL_TIME, true);
        } else if (mState == STATE_ANIM_LOOP) {
            mState = STATE_ANIM_START;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, Constant.DEFAULT_ANIM_FULL_TIME, false);
        } else if (mState == STATE_ANIM_START) {
            mState = STATE_ANIM_STOP;
            mValueAnimator = startViewAnim(DEFAULT_ANIM_START, DEFAULT_ANIM_END, Constant.DEFAULT_ANIM_HALF_TIME, false);
        } else {
            mState = STATE_ANIM_NONE;
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
            TransmutableView.this.invalidate();
        });

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        mState = STATE_ANIM_NONE;
        super.onRestoreInstanceState(state);
    }

    public int getState() {
        return mState;
    }
}
