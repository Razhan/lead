package com.ef.newlead;

import android.app.Application;

import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.SystemText;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public final class NewLeadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SystemText.init(this);
        ErrorHandler.init(this);
        SharedPreUtils.init(this);

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Timber.plant(new Timber.DebugTree());
        }
    }

}