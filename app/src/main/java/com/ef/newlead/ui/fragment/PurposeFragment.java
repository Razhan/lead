package com.ef.newlead.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Purpose;
import com.ef.newlead.ui.widget.BubbleTextVew;
import com.ef.newlead.ui.widget.DiscreteSlider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PurposeFragment extends BaseCollectInfoFragment implements DiscreteSlider.OnSlideListener {

    @BindView(R.id.purpose_hint)
    BubbleTextVew hint;
    @BindView(R.id.purpose_slider)
    DiscreteSlider slider;
    @BindView(R.id.purpose_title)
    TextView title;
    @BindView(R.id.purpose_next_button)
    Button next;
    @BindView(R.id.purpose_description)
    TextView description;

    private List<Purpose> purposes;

    public static Fragment newInstance() {
        return new PurposeFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_purpose;
    }

    @Override
    public void initView() {
        super.initView();

        String descriptionStr = getLocaleText("purpose_selections");
        purposes = new Gson().fromJson(descriptionStr, new TypeToken<List<Purpose>>() {
        }.getType());

        title.setText(getLocaleText("purpose_select_title"));
        next.setText(getContinueText());
        hint.setText(getLocaleText("purpose_tip_label"));

        slider.setOnSlideListener(this);
        new Handler().postDelayed(() -> animateHint(true, 0, 1), 100);
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

    @OnClick(R.id.purpose_next_button)
    public void OnClick() {
        startNextFragment();
    }

}
