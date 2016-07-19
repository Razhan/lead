package com.ran.delta.presentation.presenter;

import android.support.annotation.UiThread;

import com.ran.delta.presentation.ui.view.MvpView;

public interface MvpPresenter<V extends MvpView> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView(boolean retainInstance);
}
