package com.ef.cat;

import android.app.Application;
import android.content.ContextWrapper;

import com.ef.cat.injector.components.ApplicationComponent;
import com.ef.cat.injector.components.DaggerApplicationComponent;
import com.ef.cat.injector.modules.ApplicationModule;
import com.ran.delta.utils.PrefUtils;
import com.squareup.leakcanary.LeakCanary;

public class CatApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        initPrefs();
        LeakCanary.install(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initPrefs() {
        new PrefUtils.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("PREFERENCE")
                .setUseDefaultSharedPreference(false)
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}