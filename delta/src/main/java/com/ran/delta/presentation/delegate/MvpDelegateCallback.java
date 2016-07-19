package com.ran.delta.presentation.delegate;

import android.support.annotation.NonNull;

import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpView;

public interface MvpDelegateCallback<V extends MvpView, P extends MvpPresenter<V>> {

    @NonNull
    P createPresenter();

    P getPresenter();

    void setPresenter(P presenter);

    V getMvpView();

    boolean isRetainInstance();

    void setRetainInstance(boolean retainingInstance);

    boolean shouldInstanceBeRetained();
}

