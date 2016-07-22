package com.ef.cat.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Field;

public class FixedViewPager extends ViewPager {

    private boolean isScrollable = false;
    private CustomDurationScroller mScroller;

    public FixedViewPager(Context context) {
        super(context);
        init();
    }

    public FixedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new CustomDurationScroller(getContext(), new DecelerateInterpolator());
            scroller.set(this, mScroller);
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isScrollable) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.isScrollable = scrollable;
    }
}