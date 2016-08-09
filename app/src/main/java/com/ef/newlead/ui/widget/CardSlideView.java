package com.ef.newlead.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;

import java.util.ArrayList;
import java.util.List;

public class CardSlideView extends ViewGroup implements View.OnClickListener {

    private int elevation = 10;
    private final int yOffset = 25;
    private final float scaleStep = 0.03f;

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
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View childView = getChildAt(i);

            if (childView instanceof CardView) {
                CardView cardView = (CardView)childView;
                cardView.setCardElevation(elevation--);

                ImageView select = (ImageView)cardView.findViewById(R.id.level_select);
                ImageView refuse = (ImageView)cardView.findViewById(R.id.level_not_select);

                select.setOnClickListener(this);
                refuse.setOnClickListener(this);
                
                viewList.add(childView);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(widthMeasureSpec, 800, 0);
        int height = measureDimension(heightMeasureSpec, 100, 1);

        setMeasuredDimension(width, height);
    }

    private int measureDimension(int measureSpec, int defaultSize, int type) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                if (getChildCount() < 1) {
                    return Math.min(result, specSize);
                }

                if (type == 1) {
                    result = getChildAt(0).getMeasuredHeight() + (viewList.size() - 1) * yOffset + getPaddingTop();
                    result = Math.min(result, specSize);
                } else {
                    result = getChildAt(0).getMeasuredWidth();
                    result = Math.min(result, specSize);
                }
            }
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < viewList.size(); i++) {
            View viewItem = viewList.get(i);
            int childHeight = viewItem.getMeasuredHeight();
            int viewLeft = (getWidth() - viewItem.getMeasuredWidth()) / 2;
            viewItem.layout(viewLeft, getPaddingTop(), viewLeft + viewItem.getMeasuredWidth(), childHeight + getPaddingTop());

            int offset = yOffset * i;
            float scale = 1 - scaleStep * i;

            viewItem.setTranslationY(offset);
            viewItem.setScaleX(scale);
            viewItem.setScaleY(scale);
        }
    }

    @Override
    public void onClick(View v) {
        v.setAlpha(1);
        View view = viewList.remove(0);
        if (v.getTag().equals("select")) {
            selectedCardAnimation(view, true);
        } else {
            selectedCardAnimation(view, false);
        }

        restCardAnimation();
    }

    private void selectedCardAnimation(View v, boolean direction) {
        ObjectAnimator transX, rotation;
        if (direction) {
            transX = ObjectAnimator.ofFloat(v, "translationX", -this.getWidth());
            rotation = ObjectAnimator.ofFloat(v, "rotation", 90);
        } else {
            transX = ObjectAnimator.ofFloat(v, "translationX", this.getWidth());
            rotation = ObjectAnimator.ofFloat(v, "rotation", -90);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(transX, rotation);
        set.setDuration(Constant.DEFAULT_ANIM_FULL_TIME).setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(v);
            }
        });

        set.start();
    }

    private void restCardAnimation() {
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);

            ObjectAnimator transY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY() - yOffset);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX() + scaleStep);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY() + scaleStep);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(transY, scaleX, scaleY);
            set.setDuration(Constant.DEFAULT_ANIM_FULL_TIME).setInterpolator(new DecelerateInterpolator());

            set.start();
        }
    }

}
