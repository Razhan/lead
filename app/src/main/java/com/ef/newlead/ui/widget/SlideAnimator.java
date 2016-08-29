package com.ef.newlead.ui.widget;

import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ef.newlead.Constant;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;

public class SlideAnimator extends BaseItemAnimator {

    public SlideAnimator() {
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        ObjectAnimator animator;

        if ((int) holder.itemView.getTag() % 2 == 0) {
            animator = getSlideAnimation(holder.itemView, 0, -holder.itemView.getRootView().getWidth());
        } else {
            animator = getSlideAnimation(holder.itemView, 0, holder.itemView.getRootView().getWidth());
        }

        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME)
                .setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth());
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ObjectAnimator animator;

        if (holder.getAdapterPosition() % 2 == 0) {
            animator = getSlideAnimation(holder.itemView, -holder.itemView.getRootView().getWidth(), 0);
        } else {
            animator = getSlideAnimation(holder.itemView, holder.itemView.getRootView().getWidth(), 0);
        }

        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME)
                .setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private ObjectAnimator getSlideAnimation(View view, float start, float end) {
        return ObjectAnimator.ofFloat(view, "translationX", start, end);
    }

}
