package com.ef.newlead.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ef.newlead.R;
import com.ef.newlead.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class DiscreteSlider extends View {

    private final static int DEFAULT_COUNT = 4;
    private final static int DEFAULT_COLOR = Color.parseColor("#4dffffff");
    private final static int DEFAULT_BAR_HEIGHT = 2;
    private final static int DEFAULT_THUMB_RADIUS = 20;
    private final static int DEFAULT_EXTRA_SPACE = 20;
    private final static int DEFAULT_DOT_RADIUS = 1;


    private float cy;
    private float mProgress = Float.MIN_VALUE;
    private List<Float> dotsPos;
    private int barColor;
    private int barHeight;
    private int thumbRadius;
    private int rangeCount;
    private boolean isDragging;
    private Paint mPaint;
    private Drawable fab;
    private OnSlideListener listener;

    public DiscreteSlider(Context context) {
        this(context, null);
    }

    public DiscreteSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscreteSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiscreteSlider);

        rangeCount = a.getInt(R.styleable.DiscreteSlider_dotCount, DEFAULT_COUNT);
        barColor = a.getColor(R.styleable.DiscreteSlider_barColor, DEFAULT_COLOR);

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        barHeight = ViewUtils.dpToPx(getContext(), DEFAULT_BAR_HEIGHT);
        thumbRadius = ViewUtils.dpToPx(getContext(), DEFAULT_THUMB_RADIUS);
        dotsPos = new ArrayList<>();

        fab = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fab, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(widthMeasureSpec, 800);
        int height = measureDimension(heightMeasureSpec, 100);

        setMeasuredDimension(width, height);

        cy = height / 2;

        int gap = (width - getPaddingStart() - getPaddingEnd()) / (rangeCount - 1);

        dotsPos.clear();

        for (int i = 0; i < rangeCount; i++) {
            if (i == 0) {
                dotsPos.add(gap * i + getPaddingStart() * 1.0f);
            } else if (i == rangeCount - 1) {
                dotsPos.add(getMeasuredWidth() - getPaddingEnd() * 1.0f);
            } else {
                dotsPos.add(dotsPos.get(i - 1) + gap);
            }
        }
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
    public void onDraw(Canvas canvas) {
        drawProgressBar(canvas, getPaddingStart(), getWidth() - getPaddingEnd(), barColor);
        drawDots(canvas);
        drawThumb(canvas);
    }

    private void drawProgressBar(Canvas canvas, float start, float end, int color) {
        mPaint.setColor(color);
        canvas.drawRect(start, cy - barHeight / 2, end, cy + barHeight / 2, mPaint);
    }

    private void drawThumb(Canvas canvas) {
        if (mProgress == Float.MIN_VALUE) {
            mProgress = getWidth() / 2;
        }

        mProgress = Math.max(mProgress, getPaddingStart() - DEFAULT_EXTRA_SPACE);
        mProgress = Math.min(mProgress, getWidth() - getPaddingEnd() + DEFAULT_EXTRA_SPACE);

        fab.setBounds((int)(mProgress - thumbRadius), (int)(cy - thumbRadius),
                            (int)(mProgress + thumbRadius), (int)(cy + thumbRadius));

        fab.draw(canvas);
    }

    private void drawDots(Canvas canvas) {
        mPaint.setColor(Color.WHITE);

        for (float pos : dotsPos) {
            canvas.drawCircle(pos, cy, ViewUtils.dpToPx(getContext(), DEFAULT_DOT_RADIUS), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = isDrag(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    mProgress = event.getX();

                    if (event.getX() >= getPaddingLeft() && event.getX() <= getWidth() - getPaddingRight()) {
                        listener.onMove((int) ((event.getX() - getPaddingLeft()) * 100 /
                                (getWidth() - getPaddingLeft() - getPaddingRight())));
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDragging) {
                    isDragging = false;
                    toFinalPosition(event.getX());
                }
                break;
            default:
                break;
        }

        return true;
    }

    private boolean isDrag(float x, float y) {
        float extra = 2.5f * DEFAULT_EXTRA_SPACE;
        return x >= mProgress - extra && x <= mProgress + extra && y >= cy - extra && y <= cy + extra;
    }

    private void toFinalPosition(float x) {
        int count = getClosestPos(x);
        mProgress = dotsPos.get(count);

        if (listener != null) {
            listener.onSelected(count);
        }
        Log.d("count", String.valueOf(count));

        invalidate();
    }

    private int getClosestPos(float key) {
        for (float pos : dotsPos) {
            if (pos > key) {
                return Math.max(dotsPos.indexOf(pos) - 1, 0);
            }
        }
        return dotsPos.size() - 1;
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.listener = listener;
    }

    public interface OnSlideListener {

        void onSelected(int index);

        void onMove(int index);
    }

}
