package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ef.newlead.presenter.Presenter;

public abstract class BaseMVPActivity<P extends Presenter> extends BaseActivity {

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();

        if (presenter != null) {
            presenter.onCreate();
        }
    }

    @NonNull
    protected abstract P createPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
