package com.ef.newlead.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.domain.usecase.VerificationUseCase;
import com.ef.newlead.presenter.VerificationPresenter;
import com.ef.newlead.ui.view.VerificationView;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.ui.widget.CheckProgressView;
import com.ef.newlead.util.KeyBoardVisibilityMonitor;
import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NumberFragment extends BaseCollectInfoFragment<VerificationPresenter>
        implements VerificationView {

    private final static String NUMBER_KEY = "phone_number";

    @BindView(R.id.number_progress_view)
    CheckProgressView progressView;
    @BindView(R.id.number_input)
    DeletableEditText input;
    @BindView(R.id.number_hint)
    TextView hint;
    @BindView(R.id.number_bottom_bar)
    RelativeLayout submit;
    @BindView(R.id.number_next)
    TextView next;
    @BindView(R.id.number_title)
    TextView title;

    private boolean clickable = false;
    private String phone_number;

    public static Fragment newInstance() {
        return new NumberFragment();
    }

    public static Fragment newInstance(String number) {
        NumberFragment fragment = new NumberFragment();

        Bundle args = new Bundle();
        args.putString(NUMBER_KEY, number);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            phone_number = getArguments().getString(NUMBER_KEY, "");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public void initView() {
        super.initView();

        hint.setText(getLocaleText("phone_select_subtitle_1"));
        input.setText(phone_number);
        input.setHint(getLocaleText("phone_select_phone_placeholder"));
        title.setText(getLocaleText("phone_select_title"));
        next.setText(getLocaleText("info_collection_next"));

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

                    if (progressView.getState() == CheckProgressView.STATE_ANIM_STOP) {
                        progressView.startAnim();
                        next.setVisibility(View.VISIBLE);
                        clickable = false;
                    }
                }
            }
        });

        progressView.setEndAnimationListener(() -> {
            inProgress = false;
            hint.setText(getLocaleText("phone_select_subtitle_3"));
            SharedPreUtils.putString(Constant.USER_PHONE, input.getText().toString());

            startNextFragment();
        });
    }

    @Override
    protected VerificationPresenter createPresent() {
        return new VerificationPresenter(getContext(), this, new VerificationUseCase());
    }

    @Override
    public void afterCodeVerified(boolean isSucceed) {

    }

    @OnClick(R.id.number_bottom_bar)
    public void onClick() {
        if (!clickable || inProgress) {
            return;
        }

        presenter.getVerificationCode(input.getText().toString());

        ViewUtils.hideKeyboard(getActivity());
        next.setVisibility(View.GONE);
        inProgress = true;
        progressView.startAnim();
        hint.setText(getLocaleText("phone_select_subtitle_2"));
    }

    @Override
    public void afterNumberSubmit(boolean isSucceed) {
        if (isSucceed) {
            progressView.startAnim();
        } else {
            progressView.setState(CheckProgressView.STATE_ANIM_NONE);
            next.setVisibility(View.VISIBLE);
            inProgress = false;
        }
    }

    @Override
    protected Fragment getNextFragment() {
        Fragment fragment = VerificationFragment.newInstance(input.getText().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.RIGHT);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
        }

        return fragment;
    }

}
