package com.ef.newlead;

import android.app.Application;

import com.ef.newlead.util.SystemText;
import com.squareup.leakcanary.LeakCanary;

public class NewLeadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemText.init(this);

        LeakCanary.install(this);
    }

}