package com.ef.newlead.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientBackground;
import com.ef.newlead.data.model.Purpose;
import com.ef.newlead.ui.widget.BubbleTextVew;
import com.ef.newlead.ui.widget.DiscreteSlider;
import com.ef.newlead.Constant;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;

public class PurposeFragment extends BaseFragment implements DiscreteSlider.OnSlideListener {

    @BindView(R.id.purpose_wrapper)         FrameLayout purposeWrapper;
    @BindView(R.id.purpose_hint)            BubbleTextVew hint;
    @BindView(R.id.purpose_slider)          DiscreteSlider slider;
    @BindView(R.id.purpose_title)           TextView title;
    @BindView(R.id.purpose_next_button)     Button next;
    @BindView(R.id.purpose_description)     TextView description;

    private List<Purpose> purposes;

    public static PurposeFragment newInstance() {
        return new PurposeFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_purpose;
    }

    @Override
    public void initView() {
        setBackground();

        String descriptionStr = SystemText.getSystemText(getContext(), "purpose_selections");
        purposes = new Gson().fromJson(descriptionStr, new TypeToken<List<Purpose>>() {}.getType());

        title.setText(SystemText.getSystemText(getContext(), "purpose_select_title"));
        next.setText(SystemText.getSystemText(getContext(), "purpose_select_next"));
        hint.setText(SystemText.getSystemText(getContext(), "purpose_tip_label"));

        slider.setOnSlideListener(this);
        new Handler().postDelayed(() -> animateHint(true, 0, 1), 100);
    }

    private void setBackground() {
        String backgroundStr = SystemText.getSystemText(getContext(), "purpose_select_gradient_color");
        GradientBackground background = new Gson().fromJson(backgroundStr,
                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{background.getBottomGradient().toHex(), background.getTopGradient().toHex()});
        purposeWrapper.setBackground(drawable);
    }

    @Override
    public void onSelected(int index) {
        if (hint.getVisibility() != View.GONE) {
            animateHint(false, 1, 0);
        }
        hideDescription(index);
    }

    @Override
    public void onMove(int index) {
    }

    private void animateHint(boolean show, float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(hint, "alpha", start, end);
        animator.setDuration(Constant.DEFAULT_ANIM_HALF_TIME);

        if (!show) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    hint.setVisibility(View.GONE);
                }
            });
        }
        animator.start();
    }

    private void hideDescription(int index) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(description, "alpha", 1, 0);
        animator.setDuration(Constant.DEFAULT_ANIM_HALF_TIME);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                description.setText(Html.fromHtml(purposes.get(index).getText()));
                new Handler().postDelayed(() -> showDescription(), 50);
            }
        });

        animator.start();
    }

    private void showDescription() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(description, "alpha", 0, 1);
        animator.setDuration(Constant.DEFAULT_ANIM_HALF_TIME);
        animator.start();
    }

}
