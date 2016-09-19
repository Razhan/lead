package com.ef.newlead.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.domain.usecase.InitializationUseCase;
import com.ef.newlead.presenter.SplashPresenter;
import com.ef.newlead.ui.view.SplashView;
import com.ef.newlead.ui.widget.TransmutableView;
import com.ef.newlead.util.SharedPreUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseMVPActivity<SplashPresenter> implements SplashView {

    @BindView(R.id.splash_bottom_bar)
    LinearLayout bottomBar;
    @BindView(R.id.splash_title)
    TextView title;
    @BindView(R.id.splash_header)
    TextView header;
    @BindView(R.id.splash_intro)
    TextView intro;
    @BindView(R.id.splash_indicator)
    TransmutableView indicator;
    @BindView(R.id.splash_sign_up)
    TextView signUp;
    @BindView(R.id.splash_ef_center)
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setText();
        indicator.post(() -> indicator.startAnim());
    }

    private void setText() {
        header.setText(getLocaleText("splash_header"));
        title.setText(getLocaleText("splash_title"));

        intro.setText(getLocaleText("splash_detail"));

        center.setText(getLocaleText("splash_action_find_center"));
        signUp.setText(getLocaleText("splash_create_account"));

        indicator.setTitle(getLocaleText("splash_action_start"));
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter(this, this, new InitializationUseCase());
    }

    private void startBottomBarAnim(boolean isEntry, long duration) {
        bottomBar.setVisibility(View.VISIBLE);
        ObjectAnimator animator;

        if (isEntry) {
            animator = ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBar.getHeight(), 0);
            animator.setInterpolator(new DecelerateInterpolator());
        } else {
            animator = ObjectAnimator.ofFloat(bottomBar, "translationY", 0, bottomBar.getHeight());
            animator.setInterpolator(new AccelerateInterpolator());
        }
        animator.setDuration(duration).start();
    }

    @OnClick(R.id.splash_indicator)
    public void onClick() {
        if (indicator.getState() == TransmutableView.STATE_ANIM_START) {
            indicator.startAnim();
            startBottomBarAnim(false, Constant.DEFAULT_ANIM_HALF_TIME);

            new Handler().postDelayed(() -> {
                if (!SharedPreUtils.getBoolean(Constant.USER_SAVED, false)) {
                    startActivity(new Intent(this, CollectInfoActivity.class));
                } else {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
            }, Constant.DEFAULT_ANIM_HALF_TIME);
        }
    }

    @Override
    public void afterInit() {
        setText();
        indicator.startAnim();
        startBottomBarAnim(true, Constant.DEFAULT_ANIM_FULL_TIME);
    }

    @OnClick({R.id.splash_sign_up, R.id.splash_ef_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_sign_up:
                break;
            case R.id.splash_ef_center:
                startActivity(new Intent(this, FindCenterActivity.class));
                break;
        }
    }
}