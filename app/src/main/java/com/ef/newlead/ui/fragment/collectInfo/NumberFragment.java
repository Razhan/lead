package com.ef.newlead.ui.fragment.collectInfo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.domain.usecase.VerificationUseCase;
import com.ef.newlead.presenter.VerificationPresenter;
import com.ef.newlead.ui.view.VerificationView;
import com.ef.newlead.ui.widget.CheckProgressView;
import com.ef.newlead.ui.widget.DeletableEditText;
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

    @BindView(R.id.close)
    ViewGroup closeImage;

    private boolean clickable = false;
    private String phone_number;

    public interface PhoneNumberInputListener {
        void onInputComplete(String phone);
    }

    private PhoneNumberInputListener phoneNumberInputListener;

    public NumberFragment setPhoneNumberInputListener(PhoneNumberInputListener inputListener) {
        this.phoneNumberInputListener = inputListener;
        return this;
    }

    public static NumberFragment newInstance(boolean standalone) {
        Bundle args = new Bundle();
        args.putBoolean(STANDALONE, standalone);

        NumberFragment numberFragment = new NumberFragment();
        numberFragment.setArguments(args);
        return numberFragment;
    }

    public static NumberFragment newInstance(boolean standalone, String number) {
        NumberFragment fragment = new NumberFragment();

        Bundle args = new Bundle();
        args.putString(NUMBER_KEY, number);
        args.putBoolean(STANDALONE, standalone);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected GradientColor getColor() {
        if (isStandalone()) {
            return getDefaultGradientColor();
        } else {
            return super.getColor();
        }
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

        if (!TextUtils.isEmpty(phone_number)) {
            ViewUtils.hideKeyboard(getActivity());
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public void initView() {
        super.initView();

        hint.setText(getLocaleText("phone_select_subtitle_1"));

        input.setHint(getLocaleText("phone_select_phone_placeholder"));
        title.setText(getLocaleText("phone_select_title"));
        next.setText(getLocaleText("phone_select_action_getcode"));

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

                    next.setVisibility(View.VISIBLE);
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

        input.setText(phone_number);

        progressView.setEndAnimationListener(() -> {
            inProgress = false;
            hint.setText(getLocaleText("phone_select_subtitle_3"));
            String phone = input.getText().toString();
            SharedPreUtils.putString(Constant.USER_PHONE, phone);

            continueWithPhoneNum();
        });

        if (isStandalone()) {
            closeImage.setVisibility(View.VISIBLE);
            ((RelativeLayout.LayoutParams) title.getLayoutParams()).topMargin = 0;
        } else {
            closeImage.setVisibility(View.GONE);
        }
    }

    @Override
    protected VerificationPresenter createPresent() {
        return new VerificationPresenter(getContext(), this);
    }

    @Override
    public void afterCodeVerified(boolean isSucceed) {

    }

    protected void continueWithPhoneNum() {
        getActivity().runOnUiThread(() -> {
            if (phoneNumberInputListener != null) {
                String phone = input.getText().toString();
                phoneNumberInputListener.onInputComplete(phone);
            }

        });
    }

    @OnClick(R.id.number_bottom_bar)
    public void onClick() {
        if (!clickable || inProgress) {
            return;
        }

        input.setClearIconVisible(false);

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
            input.setClearIconVisible(true);

            hint.setText(getLocaleText("phone_select_subtitle_1"));
            progressView.setState(CheckProgressView.STATE_ANIM_NONE);
            next.setVisibility(View.VISIBLE);
            inProgress = false;
        }
    }

    @Override
    protected Fragment getNextFragment() {
        Fragment fragment = VerificationFragment.newInstance(false, input.getText().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.RIGHT);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
        }

        return fragment;
    }

    @OnClick(R.id.close)
    public void onClose() {
        getActivity().finish();
    }
}
