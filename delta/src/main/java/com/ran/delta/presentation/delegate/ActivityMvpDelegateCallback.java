package com.ran.delta.presentation.delegate;

import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpView;

public interface ActivityMvpDelegateCallback<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpDelegateCallback<V, P> {

    Object setExtraInstance();

    Object getExtraInstance();

    Object getAllInstance();

    Object setAllInstance();

}
