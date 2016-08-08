package com.ef.newlead.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CardSlideView extends ViewGroup {

    private int itemMarginTop = 10;
    private int yOffsetStep = 40;
    private float SCALE_STEP = 0.08f;

    private List<View> viewList = new ArrayList<>();

    public CardSlideView(Context context) {
        this(context, null);
    }

    public CardSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardSlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        viewList.clear();
        int num = getChildCount();

        for (int i = num - 1; i >= 0; i--) {
            viewList.add(getChildAt(i));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < viewList.size(); i++) {
            View viewItem = viewList.get(i);
            int childHeight = viewItem.getMeasuredHeight();
            int viewLeft = (getWidth() - viewItem.getMeasuredWidth()) / 2;
            viewItem.layout(viewLeft, itemMarginTop, viewLeft + viewItem.getMeasuredWidth(), itemMarginTop + childHeight);

            int offset = yOffsetStep * i;
            float scale = 1 - SCALE_STEP * i;

            viewItem.offsetTopAndBottom(offset);
            viewItem.setScaleX(scale);
            viewItem.setScaleY(scale);
        }
    }




}
