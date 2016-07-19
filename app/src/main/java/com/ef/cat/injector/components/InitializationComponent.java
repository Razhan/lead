package com.ef.cat.injector.components;

import com.ef.cat.injector.PerActivity;
import com.ef.cat.injector.modules.InitializationModule;
import com.ef.cat.presenter.SplashPresenter;
import com.ef.cat.ui.activity.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = InitializationModule.class)
public interface InitializationComponent {

    void inject(SplashActivity activity);

    SplashPresenter presenter();
}