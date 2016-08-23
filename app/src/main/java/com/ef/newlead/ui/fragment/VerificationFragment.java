package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.domain.usecase.VerificationUseCase;
import com.ef.newlead.presenter.VerificationPresenter;
import com.ef.newlead.ui.widget.IndicatedProgressView;
import com.ef.newlead.ui.widget.VerificationView;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class VerificationFragment extends BaseCollectInfoFragment<VerificationPresenter>
                implements com.ef.newlead.ui.view.VerificationView{

    private final static long DEFAULT_COUNT_DOWN_TIME = 60 * 1000 + 100;
    private final static String NUMBER_KEY = "phone_number";

    @BindView(R.id.verification_number)
    TextView number;
    @BindView(R.id.verification_input)
    VerificationView input;
    @BindView(R.id.verification_hint)
    TextView hint;
    @BindView(R.id.verification_submit)
    Button submit;
    @BindView(R.id.verification_wrapper)
    RelativeLayout verificationWrapper;
    @BindView(R.id.verification_timer)
    TextView countDownText;
    @BindView(R.id.verification_retry)
    RelativeLayout retry;
    @BindView(R.id.verification_progress_view)
    IndicatedProgressView progressView;

    private String phone_number;
    private CountDownTimer timer;

    public static Fragment newInstance(String number) {
        VerificationFragment fragment = new VerificationFragment();

        Bundle args = new Bundle();
        args.putString(NUMBER_KEY, number);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_number = getArguments().getString(NUMBER_KEY, "");
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_verification;
    }

    @Override
    public void initView() {
        number.setText(phone_number);
        submit.setText(getContinueText());
        verificationWrapper.setBackground(getGradientDrawable("age_select_gradient_color"));
        startCountDown();

        input.setFullListener(isFull -> {
            if (isFull) {
                submit.setClickable(true);
                submit.setAlpha(1);
            } else {
                submit.setClickable(false);
                submit.setAlpha(0.3f);
                hint.setText(null);
            }
        });

        number.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() > (number.getWidth() - number.getTotalPaddingRight() - 200)) {
                    backToPreviousFragment();
                }
                return true;
            }
            return false;
        });

        progressView.setEndAnimationListener(() -> {
            inProgress = false;
            startCountDown();
        });

        input.post(() -> {
            input.requestFocus();
            ViewUtils.showKeyboard(getActivity());
        });
    }

    @Override
    protected VerificationPresenter createPresent() {
        return new VerificationPresenter(getContext(), this, new VerificationUseCase());
    }

    @Override
    public void afterCodeVerified(boolean isSucceed) {
        submit.setEnabled(true);

        if (!isSucceed) {
            input.changeTextColor(Color.RED);
            hint.setText("Oh no! it seems the code is wrong");
        } else {
            startNextFragment();
        }
    }

    @Override
    public void afterNumberSubmit(boolean isSucceed) {
    }

    private void startCountDown() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(DEFAULT_COUNT_DOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String str = "重新获取" + "(" + (millisUntilFinished / 1000) + ")";
                countDownText.setText(str);

                if (countDownText.getVisibility() != View.VISIBLE) {
                    countDownText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                countDownText.setText("重新获取");
                timer = null;
            }
        }.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        timer = null;
    }

    @OnClick({R.id.verification_submit, R.id.verification_retry})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verification_submit:
                presenter.VerifyCode(phone_number, input.getInput());
                submit.setClickable(false);
                break;
            case R.id.verification_retry:
                if (timer == null && !inProgress) {
                    countDownText.setVisibility(View.GONE);

                    inProgress = true;
                    progressView.startAnim();
                    new Handler().postDelayed(() -> afterResent(), 1000);
                }
                break;
        }
    }

    public void afterResent() {
        progressView.startAnim();
    }

    private void backToPreviousFragment() {
        Fragment fragment = NumberFragment.newInstance(phone_number);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
        }

        fragmentManager.beginTransaction().replace(R.id.collect_info_fragment, fragment).commit();
    }

}
