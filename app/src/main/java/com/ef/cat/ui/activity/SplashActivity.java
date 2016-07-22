package com.ef.cat.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.cat.CatApplication;
import com.ef.cat.Constant;
import com.ef.cat.R;
import com.ef.cat.injector.components.DaggerInitializationComponent;
import com.ef.cat.injector.components.InitializationComponent;
import com.ef.cat.presenter.SplashPresenter;
import com.ef.cat.ui.fragment.InterestFragment;
import com.ef.cat.ui.fragment.PurposeFragment;
import com.ef.cat.ui.widget.TransmutableView;
import com.ef.cat.utils.SystemText;
import com.ef.cat.view.SplashView;
import com.ran.delta.presentation.ui.activity.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @BindView(R.id.splash_bottom_bar)       LinearLayout bottomBar;
    @BindView(R.id.splash_title)            TextView title;
    @BindView(R.id.splash_header)           TextView header;
    @BindView(R.id.splash_intro)            TextView intro;
    @BindView(R.id.splash_indicator)        TransmutableView indicator;

    @Inject SystemText systemText;

    private InitializationComponent initComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAllowFullScreen(true);
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            indicator.startAnim();
            startBottomBarAnim(true, Constant.DEFAULT_ANIM_FULL_TIME);
        }, 2000);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        super.initView();

        ViewTreeObserver observer = indicator.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicator.startAnim();
                indicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void initDependencyInjector() {
        initComponent = DaggerInitializationComponent
                .builder()
                .applicationComponent(((CatApplication) getApplication()).getApplicationComponent())
                .build();
        initComponent.inject(this);
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return initComponent.presenter();
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

            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(PurposeFragment.newInstance());
            fragmentList.add(InterestFragment.newInstance());

            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, CollectInfoActivity.class));
                finish();
            }, Constant.DEFAULT_ANIM_HALF_TIME);
        }
    }
}
