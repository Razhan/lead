package com.ran.delta.presentation.delegate;

import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpView;

class ActivityMvpNonConfigurationInstances<V extends MvpView, P extends MvpPresenter<V>> {

    P presenter;

    Object configurationInstance;

    ActivityMvpNonConfigurationInstances(P presenter, Object configurationInstance) {
        this.presenter = presenter;
        this.configurationInstance = configurationInstance;
    }
}
