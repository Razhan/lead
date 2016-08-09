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
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }
}
