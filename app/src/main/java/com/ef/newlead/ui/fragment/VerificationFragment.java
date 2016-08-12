package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.VerificationView;

import butterknife.BindView;
import butterknife.OnClick;

public class VerificationFragment extends BaseFragment {

    private final static long DEFAULT_COUNT_DOWN_TIME = 5 * 1000 + 100;

    @BindView(R.id.verification_number)     TextView number;
    @BindView(R.id.verification_input)      VerificationView input;
    @BindView(R.id.verification_hint)       TextView hint;
    @BindView(R.id.verification_submit)     Button submit;
    @BindView(R.id.verification_wrapper)    RelativeLayout verificationWrapper;
    @BindView(R.id.verification_retry)      Button retry;

    private CountDownTimer timer;

    public static VerificationFragment newInstance() {
        return new VerificationFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_verification;
    }

    @Override
    public void initView() {
        setBackground();
        startCountDown();

        input.setListener(isFull -> {
            if (isFull) {
                submit.setAlpha(1);
            } else {
                submit.setAlpha(0.3f);
            }
        });

        number.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() > (number.getWidth() - number.getTotalPaddingRight())) {
                        Log.d("onTouch", "onTouch");
                    }
                    return true;
                }
                return false;
        });
    }

    private void setBackground() {
//        String backgroundStr = SystemText.getSystemText(getContext(), "age_select_gradient_color");
//        GradientBackground background = new Gson().fromJson(backgroundStr,
//                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#f8c144"), Color.parseColor("#f66f9f")});

        verificationWrapper.setBackground(drawable);
    }

    private void startCountDown() {
        timer = new CountDownTimer(DEFAULT_COUNT_DOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String str = "重新获取" + "(" + (millisUntilFinished / 1000) + ")";
                retry.setText(str);
            }

            @Override
            public void onFinish() {
                retry.setText("重新获取");
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
                input.changeTextColor(Color.RED);
                break;
            case R.id.verification_retry:
                if (timer == null) {
                    startCountDown();
                }
                break;
        }
    }
}
