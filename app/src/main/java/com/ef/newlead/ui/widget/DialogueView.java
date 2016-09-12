package com.ef.newlead.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.ActivityTemplate;

import java.util.List;

public class DialogueView extends LinearLayout {

    private int currentChild;
    private LayoutParams layoutParams;
    private List<ActivityTemplate> data;

    public DialogueView(Context context) {
        this(context, null);
    }

    public DialogueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOrientation(VERTICAL);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
    }

    public void setChildCount(int childCount) {
        for (int i = 0; i < childCount; i++) {
            addChild();
        }
    }

    public void setData(List<ActivityTemplate> data) {
        if (data == null) {
            return;
        }

        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            initView(i);
        }
    }

    private void addChild() {
        View child = inflate(getContext(), R.layout.item_dialogue_video, null);
        child.setVisibility(INVISIBLE);

        this.addView(child, layoutParams);
    }

    private void initView(int index) {
        changeColor(getChildAt(index), Color.BLACK);

        // todo
    }

    public void showChild() {
        if (currentChild >= getChildCount()) {
            return;
        }

        changeColor(getChildAt(currentChild - 1), Color.parseColor("#666666"));

        View childView = getChildAt(currentChild);
        fadeIn(childView, currentChild++);
    }

    private void changeColor(View view, int color) {
        if (view == null) {
            return;
        }

        ((TextView) view.findViewById(R.id.dialogue_sentence)).setTextColor(color);
        ((TextView) view.findViewById(R.id.dialogue_translation)).setTextColor(color);
    }

    public void hideChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            fadeOut(getChildAt(i), i);
        }
        currentChild = 0;
    }

    public void showTranslation() {
        for (int i = 0; i < currentChild; i++) {
            View translation = getChildAt(i).findViewById(R.id.dialogue_translation);
            scaleAnimation(translation, 1);
        }
    }

    public void hideTranslation() {
        for (int i = 0; i < currentChild; i++) {
            View translation = getChildAt(i).findViewById(R.id.dialogue_translation);
            scaleAnimation(translation, 0);
        }
    }

    private void fadeOut(View view, int position) {
        ObjectAnimator animator;

        if (position % 2 == 0) {
            animator = ObjectAnimator.ofFloat(view, "translationX", 0, -1 * view.getWidth());
        } else {
            animator = ObjectAnimator.ofFloat(view, "translationX", 0, 2 * view.getWidth());
        }

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(INVISIBLE);
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();
    }

    private void fadeIn(View view, int position) {
        ObjectAnimator animator;

        if (position % 2 == 0) {
            animator = ObjectAnimator.ofFloat(view, "translationX", -1 * view.getWidth(), 0);
        } else {
            animator = ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0);
        }

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(VISIBLE);
            }
        });

        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();
    }

    private void scaleAnimation(View view, float scale) {
        view.animate().scaleY(scale).setDuration(Constant.DEFAULT_ANIM_HALF_TIME).start();
    }

}
