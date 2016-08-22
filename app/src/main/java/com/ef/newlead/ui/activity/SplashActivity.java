package com.ef.newlead.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
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

        indicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicator.startAnim();
                indicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
                startActivity(
                    new Intent(this, CollectInfoActivity.class));
                    finish();
            }, Constant.DEFAULT_ANIM_HALF_TIME);
        }
    }

    @Override
    public void afterInit() {
        indicator.startAnim();
        startBottomBarAnim(true, Constant.DEFAULT_ANIM_FULL_TIME);
    }
}