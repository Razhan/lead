package com.ran.delta.presentation.delegate;

import android.os.Bundle;

import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpView;

public interface ActivityMvpDelegate<V extends MvpView, P extends MvpPresenter<V>> {

    void onCreate(Bundle bundle);

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void onRestart();

    void onContentChanged();

    void onSaveInstanceState(Bundle outState);

    void onPostCreate(Bundle savedInstanceState);

    Object setAllInstance();

    Object getExtraInstance();
}
