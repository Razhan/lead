package com.ef.newlead.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Level;

import java.util.ArrayList;
import java.util.List;

public class CardSlideView extends ViewGroup implements View.OnClickListener {

    private final static int DEFAULT_DELAY = 30;
    private final int yOffset = 25;
    private final float scaleStep = 0.03f;
    private int elevation = 10;
    private int count = 0;
    private CardSlideListener listener;

    private List<View> viewList = new ArrayList<>();
    private List<Level> data;
    private boolean firstTime = true;

    private AnimatorSet selectedCardAnimation;
    private AnimatorSet otherCardAnimation;

    public CardSlideView(Context context) {
        this(context, null);
    }

    public CardSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardSlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setListener(CardSlideListener listener) {
        this.listener = listener;
    }

    public void setData(List<Level> data) {
        this.data = data;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        viewList.clear();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View childView = getChildAt(i);

            if (childView instanceof CardView) {
                CardView cardView = (CardView) childView;
                cardView.setCardElevation(elevation--);

                ImageView select = (ImageView) cardView.findViewById(R.id.level_select);
                ImageView refuse = (ImageView) cardView.findViewById(R.id.level_unselect);

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
    protected void dispatchDraw(Canvas canvas) {
        if (data != null && firstTime) {
            for (int i = 0; i < viewList.size(); i++) {
                ((FontTextView) viewList.get(i).findViewById(R.id.level_content)).setText(data.get(i).getLevelExample());
            }

            firstTime = false;
        }

        super.dispatchDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        if (selectedCardAnimation != null && selectedCardAnimation.isRunning()) {
            return;
        }

        if (otherCardAnimation != null && otherCardAnimation.isRunning()) {
            return;
        }

        v.setAlpha(1);

        if (listener != null) {
            listener.onSlide(count);
        }
        count++;

        View view = viewList.remove(0);
        if (v.getTag().equals("yes")) {
            selectedCardAnimation(view, true);
        } else {
            selectedCardAnimation(view, false);
        }

        otherCardAnimation();
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

        selectedCardAnimation = new AnimatorSet();
        selectedCardAnimation.playTogether(transX, rotation);
        selectedCardAnimation.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);
        selectedCardAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!direction || viewList.size() == 0) {
                    listener.onFinish();
                }
            }
        });

        selectedCardAnimation.start();
    }

    private void otherCardAnimation() {
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);

            ObjectAnimator transY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY() - yOffset);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX() + scaleStep);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY() + scaleStep);

            otherCardAnimation = new AnimatorSet();
            otherCardAnimation.playTogether(transY, scaleX, scaleY);
            otherCardAnimation.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            otherCardAnimation.setStartDelay(i * DEFAULT_DELAY);
            otherCardAnimation.start();
        }
    }

    public interface CardSlideListener {
        void onSlide(int count);

        void onFinish();
    }

}
