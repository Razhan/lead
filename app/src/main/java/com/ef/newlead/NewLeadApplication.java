package com.ef.newlead;

import android.app.Application;

import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.SystemText;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public final class NewLeadApplication extends Application {

    /**
     * acoustic model data package name
     */
    public static final String HMM_PACKAGE = "hub4wsj_sc_8k";

    private static NewLeadApplication app;

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

        // should be off the main thread!
        installAcousticModelData();

        app = this;
    }

    public static NewLeadApplication getApp() {
        return app;
    }

    /**
     * Unzip the acoustic model data from the assets onto local storage.
     * if fail then delete the directory
     */
    private void installAcousticModelData() {
        String path = getFilesDir().getAbsolutePath();
        try {
            FileUtils.unpackZip(getAssets().open(HMM_PACKAGE + ".zip"), path);
        } catch (IOException e) {
            e.printStackTrace();
            new File(path + File.separator + HMM_PACKAGE).delete();
        }
    }

}