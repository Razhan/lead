package com.ran.delta.presentation.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.ran.delta.presentation.ui.view.MvpView;

public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V viewRef;

    @UiThread
    @Override
    public void attachView(V view) {
        viewRef = view;
    }

    @UiThread
    @Nullable
    public V getView() {
        return viewRef;
    }

    @UiThread
    @Override
    public void detachView(boolean retainInstance) {
        if (viewRef != null) {
            viewRef = null;
        }

        if (!retainInstance) {
            unsubscribe();
        }
    }

    protected abstract void unsubscribe();
}
