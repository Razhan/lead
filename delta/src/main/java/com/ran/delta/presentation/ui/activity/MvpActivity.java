package com.ran.delta.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ran.delta.domain.exception.ErrorMessageFactory;
import com.ran.delta.presentation.delegate.ActivityMvpDelegate;
import com.ran.delta.presentation.delegate.ActivityMvpDelegateCallback;
import com.ran.delta.presentation.delegate.ActivityMvpDelegateImpl;
import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpView;

import javax.inject.Inject;

public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends BaseActivity implements ActivityMvpDelegateCallback<V, P>, MvpView {

    protected ActivityMvpDelegate<V, P> mvpDelegate;
    protected P presenter;
    protected boolean retainInstance;
    @Inject
    ErrorMessageFactory errorMessageFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDependencyInjector();
        getMvpDelegate().onCreate(savedInstanceState);
    }

    protected abstract void initDependencyInjector();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getMvpDelegate().onContentChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMvpDelegate().onPostCreate(savedInstanceState);
    }

    @NonNull
    public abstract P createPresenter();

    @NonNull
    protected ActivityMvpDelegate<V, P> getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ActivityMvpDelegateImpl<>(this);
        }
        return mvpDelegate;
    }

    @NonNull
    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public String getErrorMessage(Throwable e) {
        return errorMessageFactory.getErrorMessage(e);
    }

    @Override
    public boolean isRetainInstance() {
        return retainInstance;
    }

    @Override
    public void setRetainInstance(boolean retainInstance) {
        this.retainInstance = retainInstance;
    }

    @Override
    public boolean shouldInstanceBeRetained() {
        return retainInstance && isChangingConfigurations();
    }

    @Override
    public Object setExtraInstance() {
        return null;
    }

    @Override
    public final Object getExtraInstance() {
        return getMvpDelegate().getExtraInstance();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return setAllInstance();
    }

    @Override
    public Object setAllInstance() {
        return getMvpDelegate().setAllInstance();
    }

    @Override
    public Object getAllInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

}
