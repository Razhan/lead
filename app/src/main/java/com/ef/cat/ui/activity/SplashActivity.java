package com.ef.cat.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.cat.CatApplication;
import com.ef.cat.R;
import com.ef.cat.injector.components.DaggerInitializationComponent;
import com.ef.cat.injector.components.InitializationComponent;
import com.ef.cat.presenter.SplashPresenter;
import com.ef.cat.ui.widget.TransmutableView;
import com.ef.cat.utils.SystemText;
import com.ef.cat.view.SplashView;
import com.ran.delta.presentation.ui.activity.MvpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @BindView(R.id.splash_bottom_bar)       LinearLayout bottomBar;
    @BindView(R.id.splash_title)            TextView title;
    @BindView(R.id.splash_header)           TextView header;
    @BindView(R.id.splash_intro)            TextView intro;
    @BindView(R.id.splash_indicator)        TransmutableView indicator;

    @Inject    SystemText systemText;

    private InitializationComponent initComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAllowFullScreen(true);
        super.onCreate(savedInstanceState);
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

    private void showBottomBar() {
        bottomBar.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBar.getHeight(), 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(600).start();
    }

    @OnClick(R.id.splash_indicator)
    public void onClick() {
        indicator.startAnim();
        showBottomBar();
    }
}
