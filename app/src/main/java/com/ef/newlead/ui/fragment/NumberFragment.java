package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.activity.BaseActivity;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.ui.widget.IndicatedProgressView;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NumberFragment extends BaseFragment {

    @BindView(R.id.number_wrapper)          RelativeLayout numberWrapper;
    @BindView(R.id.number_progress_view)    IndicatedProgressView progressView;
    @BindView(R.id.number_input)            DeletableEditText input;
    @BindView(R.id.number_hint)             TextView numberHint;
    @BindView(R.id.number_bottom_bar)       RelativeLayout submit;
    @BindView(R.id.number_next)             TextView next;

    public static NumberFragment newInstance() {
        return new NumberFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public void initView() {
        setBackground();

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
                    submit.setEnabled(true);
                    submit.setAlpha(1);
                } else {
                    submit.setEnabled(false);
                    submit.setAlpha(0.2f);

                    if (progressView.getState() == IndicatedProgressView.STATE_ANIM_STOP) {
                        progressView.startAnim();
                        next.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setBackground() {
//        String backgroundStr = SystemText.getSystemText(getContext(), "age_select_gradient_color");
//        GradientBackground background = new Gson().fromJson(backgroundStr,
//                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#f8c144"), Color.parseColor("#f66f9f")});
        numberWrapper.setBackground(drawable);
    }

    @OnClick(R.id.number_bottom_bar)
    public void onClick() {
        if (progressView.getState() != IndicatedProgressView.STATE_ANIM_STOP) {
            ViewUtils.hideKeyboard(getActivity());
            next.setVisibility(View.GONE);
            progressView.startAnim();

            new Handler().postDelayed(() -> progressView.startAnim(), 1000);
        } else {
            ((BaseActivity)getActivity()).switchFragment(VerificationFragment.newInstance(), true, null);
        }
    }

}
