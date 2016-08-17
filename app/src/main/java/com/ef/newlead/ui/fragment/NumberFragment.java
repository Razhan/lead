package com.ef.newlead.ui.fragment;

import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.ui.widget.IndicatedProgressView;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NumberFragment extends BaseCollectInfoFragment {

    @BindView(R.id.number_wrapper)
    RelativeLayout numberWrapper;
    @BindView(R.id.number_progress_view)
    IndicatedProgressView progressView;
    @BindView(R.id.number_input)
    DeletableEditText input;
    @BindView(R.id.number_hint)
    TextView hint;
    @BindView(R.id.number_bottom_bar)
    RelativeLayout submit;
    @BindView(R.id.number_next)
    TextView next;

    private boolean clickable = false;

    public static Fragment newInstance() {
        return new NumberFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public void initView() {
        numberWrapper.setBackground(getBackgroundDrawable("age_select_gradient_color"));
        hint.setText("A code will be sent to you in order to verify your phone number.");
        input.addTextChangedListener(new TextWatcher() {
            String lastChar = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = input.getText().toString();
                if (str.length() >= 1)
                    lastChar = str.substring(str.length() - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = input.getText().toString().length();

                if (!lastChar.equals(" ")) {
                    if (length == 3 || length == 8) {
                        input.append(" ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 13) {
                    submit.setAlpha(1);
                    clickable = true;
                } else {
                    submit.setAlpha(0.3f);

                    if (progressView.getState() == IndicatedProgressView.STATE_ANIM_STOP) {
                        progressView.startAnim();
                        next.setVisibility(View.VISIBLE);
                        clickable = false;
                    }
                }
            }
        });

        progressView.setEndAnimationListener(() -> {
            inProgress = false;
            hint.setText("Code already sent to you.");
            startNextFragment(true);
        });
    }

    @OnClick(R.id.number_bottom_bar)
    public void onClick() {
        if (!clickable || inProgress) {
            return;
        }

        ViewUtils.hideKeyboard(getActivity());
        next.setVisibility(View.GONE);
        inProgress = true;
        progressView.startAnim();
        hint.setText("Your code is on its way..");

        new Handler().postDelayed(() -> afterSubmitNumber(), 1000);
    }

    private void afterSubmitNumber() {
        progressView.startAnim();
    }

    @Override
    protected Fragment getFragment() {
        Fragment fragment = VerificationFragment.newInstance(input.getText().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
        }
        return fragment;
    }

}
