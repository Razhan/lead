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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.domain.usecase.VerificationUseCase;
import com.ef.newlead.presenter.VerificationPresenter;
import com.ef.newlead.ui.widget.CheckProgressView;
import com.ef.newlead.ui.widget.VerificationView;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class VerificationFragment extends BaseCollectInfoFragment<VerificationPresenter>
        implements com.ef.newlead.ui.view.VerificationView {

    private final static long DEFAULT_COUNT_DOWN_TIME = 60 * 1000 + 100;
    private final static String NUMBER_KEY = "phoneNumber";

    @BindView(R.id.verification_number)
    TextView number;
    @BindView(R.id.verification_input)
    VerificationView input;
    @BindView(R.id.verification_hint)
    TextView hint;
    @BindView(R.id.verification_submit)
    Button submit;
    @BindView(R.id.verification_timer)
    TextView countDownText;
    @BindView(R.id.verification_retry)
    RelativeLayout retry;
    @BindView(R.id.verification_progress_view)
    CheckProgressView progressView;
    @BindView(R.id.verification_parent)
    ViewGroup verificationParent;

    private String phone_number;
    private CountDownTimer timer;
    private String timerText;

    private boolean hiddenFirstTime = true;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_verification;
    }

    @Override
    public void initView() {
        super.initView();

        timerText = getLocaleText("phone_select_action_retry");
        number.setText(phone_number);
        countDownText.setText(timerText);
        submit.setText(getContinueText());
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
            hint.setText(getLocaleText("phone_select_subtitle_4"));
        } else {
            startNextFragment();
        }
    }

    @Override
    public void afterNumberSubmit(boolean isSucceed) {
        progressView.startAnim();
    }

    private void startCountDown() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(DEFAULT_COUNT_DOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String str = timerText + "(" + (millisUntilFinished / 1000) + ")";
                countDownText.setText(str);

                if (countDownText.getVisibility() != View.VISIBLE) {
                    countDownText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                countDownText.setText(timerText);
                timer = null;
            }
        }.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
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
                    presenter.getVerificationCode(phone_number);
                }
                break;
        }
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
