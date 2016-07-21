package com.ef.cat.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ef.cat.R;
import com.ran.delta.utils.ViewUtils;

public class DiscreteSlider extends View {

    public static final String SAVED_STATE = "saved_state";

    private static final int DEFAULT_FILLED_COLOR = Color.parseColor("#FFA500");
    private static final int DEFAULT_EMPTY_COLOR = Color.parseColor("#C3C3C3");
    private static final float DEFAULT_BAR_HEIGHT_PERCENT = 0.15f;
    private static final float DEFAULT_SLOT_HEIGHT_PERCENT = 0.05f;
    private static final float DEFAULT_SLIDER_RADIUS_PERCENT = 0.5f;
    private static final int DEFAULT_RANGE_COUNT = 6;
    private static final int DEFAULT_HEIGHT_IN_DP = 30;

    protected Paint paint;
    protected float sliderRadius;
    protected float slotHeight;
    private int currentIndex;
    private float currentSlidingX;
    private float selectedSlotX;
    private float selectedSlotY;
    private boolean gotSlot = false;
    private float[] slotPositions;
    private int filledColor = DEFAULT_FILLED_COLOR;
    private int emptyColor = DEFAULT_EMPTY_COLOR;
    private float barHeightPercent = DEFAULT_BAR_HEIGHT_PERCENT;
    private int rangeCount = DEFAULT_RANGE_COUNT;
    private float slotHeightPercent = DEFAULT_SLOT_HEIGHT_PERCENT;
    private float sliderRadiusPercent = DEFAULT_SLIDER_RADIUS_PERCENT;
    private int barHeight;

    private OnSlideListener listener;

    public DiscreteSlider(Context context) {
        this(context, null);
    }

    public DiscreteSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscreteSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiscreteSlider);
            try {
                rangeCount = a.getInt(
                        R.styleable.DiscreteSlider_rangeCount, DEFAULT_RANGE_COUNT);
                filledColor = a.getColor(
                        R.styleable.DiscreteSlider_filledColor, DEFAULT_FILLED_COLOR);
                emptyColor = a.getColor(
                        R.styleable.DiscreteSlider_emptyColor, DEFAULT_EMPTY_COLOR);
                barHeightPercent = a.getFloat(
                        R.styleable.DiscreteSlider_barHeightPercent, DEFAULT_BAR_HEIGHT_PERCENT);
                barHeightPercent = a.getFloat(
                        R.styleable.DiscreteSlider_barHeightPercent, DEFAULT_BAR_HEIGHT_PERCENT);
                slotHeightPercent = a.getFloat(
                        R.styleable.DiscreteSlider_slotRadiusPercent, DEFAULT_SLOT_HEIGHT_PERCENT);
                sliderRadiusPercent = a.getFloat(
                        R.styleable.DiscreteSlider_sliderRadiusPercent, DEFAULT_SLIDER_RADIUS_PERCENT);
            } finally {
                a.recycle();
            }
        }

        slotPositions = new float[rangeCount];
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                updateSize(getHeight());
                preComputeDrawingPosition();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        currentIndex = 0;
    }

    private void updateSize(int height) {
        barHeight = (int) (height * barHeightPercent);
        sliderRadius = height * sliderRadiusPercent;
        slotHeight = height * slotHeightPercent;
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.listener = listener;
    }

    private void preComputeDrawingPosition() {
        int w = getWidthWithoutPadding();
        int h = getHeightWithoutPadding();

        int spacing = w / rangeCount;

        int y = getPaddingTop() + h / 2;
        selectedSlotY = y;

        int x = getPaddingLeft() + (int) sliderRadius;

        for (int i = 0; i < rangeCount; ++i) {
            slotPositions[i] = x;
            if (i == currentIndex) {
                currentSlidingX = x;
                selectedSlotX = x;
            }
            x += spacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = ViewUtils.dpToPx(DEFAULT_HEIGHT_IN_DP) + getPaddingTop() + getPaddingBottom();
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = specSize + getPaddingLeft() + getPaddingRight() + (int) (2 * sliderRadius);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void updateCurrentIndex() {
        float min = Float.MAX_VALUE;
        int j = 0;

        for (int i = 0; i < rangeCount; ++i) {
            float dx = Math.abs(currentSlidingX - slotPositions[i]);
            if (dx < min) {
                min = dx;
                j = i;
            }
        }

        if (j != currentIndex) {
            if (listener != null) {
                listener.onSlide(j);
            }
        }
        currentIndex = j;
        currentSlidingX = slotPositions[j];
        selectedSlotX = currentSlidingX;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                gotSlot = isInSelectedSlot(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (gotSlot) {
                    if (x >= slotPositions[0] && x <= slotPositions[rangeCount - 1]) {
                        currentSlidingX = x;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (gotSlot) {
                    gotSlot = false;
                    currentSlidingX = x;
                    updateCurrentIndex();
                }
                break;
        }
        return true;
    }

    private boolean isInSelectedSlot(float x, float y) {
        return selectedSlotX - sliderRadius <= x && x <= selectedSlotX + sliderRadius &&
                selectedSlotY - sliderRadius <= y && y <= selectedSlotY + sliderRadius;
    }

    private void drawEmptySlots(Canvas canvas) {
        paint.setColor(emptyColor);
        int h = getHeightWithoutPadding();
        int y = getPaddingTop() + (h / 2);
        for (int i = 1; i < rangeCount - 1; i++) {
            canvas.drawRect(slotPositions[i] - slotHeight, y - slotHeight * 4,
                    slotPositions[i] + slotHeight, y + slotHeight * 4, paint);
        }
    }

    public int getHeightWithoutPadding() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    public int getWidthWithoutPadding() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void drawFilledSlots(Canvas canvas) {
        paint.setColor(filledColor);
        int h = getHeightWithoutPadding();
        int y = getPaddingTop() + (h / 2);
        for (int i = 1; i < rangeCount - 1; i++) {
            if (slotPositions[i] <= currentSlidingX) {
                canvas.drawRect(slotPositions[i] - slotHeight, y - slotHeight * 4,
                        slotPositions[i] + slotHeight, y + slotHeight * 4, paint);
            }
        }
    }

    private void drawBar(Canvas canvas, int from, int to, int color) {
        paint.setColor(color);
        int h = getHeightWithoutPadding();
        int half = (barHeight / 2);
        int y = getPaddingTop() + (h / 2);
        canvas.drawRect(from, y - half, to, y + half, paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = getHeightWithoutPadding();
        int x0 = getPaddingLeft() + (int) sliderRadius;
        int y0 = getPaddingTop() + (h / 2);
        drawEmptySlots(canvas);
        drawFilledSlots(canvas);

        drawBar(canvas, (int) slotPositions[0], (int) slotPositions[rangeCount - 1], emptyColor);

        drawBar(canvas, x0, (int) currentSlidingX, filledColor);

        paint.setColor(filledColor);
        canvas.drawCircle(currentSlidingX, y0, sliderRadius, paint);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt(SAVED_STATE, currentIndex);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.currentIndex = bundle.getInt(SAVED_STATE);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public interface OnSlideListener {

        void onSlide(int index);
    }
}
