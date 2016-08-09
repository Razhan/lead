package com.ef.newlead.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ef.newlead.util.ViewUtils;

public class BubbleTextVew extends TextView {

    private static int DEFAULT_CORNER_RADIUS;
    private static int DEFAULT_PEAK_HEIGHT;

    private Path mPath;
    private Paint mPaint;

    public BubbleTextVew(Context context) {
        this(context, null);
    }

    public BubbleTextVew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleTextVew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);

        DEFAULT_CORNER_RADIUS = ViewUtils.dpToPx(getContext(), 6);
        DEFAULT_PEAK_HEIGHT = ViewUtils.dpToPx(getContext(), 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBubble(canvas);
        super.onDraw(canvas);
    }

    private void drawBubble(Canvas canvas) {
        mPath.moveTo(getWidth() / 2 - DEFAULT_CORNER_RADIUS, DEFAULT_PEAK_HEIGHT);
        mPath.lineTo(getWidth() / 2, 0);
        mPath.lineTo(getWidth() / 2 + DEFAULT_CORNER_RADIUS, DEFAULT_PEAK_HEIGHT);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
        canvas.drawRoundRect(new RectF(0, DEFAULT_PEAK_HEIGHT, getWidth(), getHeight()), DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS, mPaint);
    }

}
