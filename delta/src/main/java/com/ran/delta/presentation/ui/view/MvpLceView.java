package com.ran.delta.presentation.ui.view;

import android.support.annotation.UiThread;

public interface MvpLceView<M> extends MvpView {

    @UiThread
    void showLoading(boolean pullToRefresh);

    @UiThread
    void showContent();

    @UiThread
    void showError(Throwable e, boolean pullToRefresh);

    @UiThread
    void setData(M data);

    @UiThread
    void loadData(boolean pullToRefresh);

}
