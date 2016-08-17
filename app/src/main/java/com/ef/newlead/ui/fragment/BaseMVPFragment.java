package com.ef.newlead.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ef.newlead.presenter.Presenter;

public abstract class BaseMVPFragment<P extends Presenter> extends BaseFragment {

    protected P presenter;

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        presenter = createPresent();
        presenter.onCreate();
    }

    protected abstract P createPresent();

    @Override
    public void onStart() {
        if (presenter != null) {
            presenter.onStart();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (presenter != null) {
            presenter.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (presenter != null) {
            presenter.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
