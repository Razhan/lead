package com.ran.delta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ran.delta.R;
import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpLceView;
import com.ran.delta.utils.LceUtils;

public abstract class MvpLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements MvpLceView<M> {

    protected View loadingView;
    protected CV contentView;
    protected TextView errorView;

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingView = view.findViewById(R.id.loadingView);
        contentView = (CV) view.findViewById(R.id.contentView);
        errorView = (TextView) view.findViewById(R.id.errorView);

        if (loadingView == null) {
            throw new NullPointerException(
                    "Loading view is null! Have you specified a loading view in your layout xml file?"
                            + " You have to give your loading View the id R.id.loadingView");
        }

        if (contentView == null) {
            throw new NullPointerException(
                    "Content view is null! Have you specified a content view in your layout xml file?"
                            + " You have to give your content View the id R.id.contentView");
        }

        if (errorView == null) {
            throw new NullPointerException(
                    "Error view is null! Have you specified a content view in your layout xml file?"
                            + " You have to give your error View the id R.id.errorView");
        }

        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorViewClicked();
            }
        });
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

        if (!pullToRefresh) {
            animateLoadingViewIn();
        }
    }

    protected void animateLoadingViewIn() {
        LceUtils.showLoading(loadingView, contentView, errorView);
    }

    @Override
    public void showContent() {
        animateContentViewIn();
    }

    protected void animateContentViewIn() {
        LceUtils.showContent(loadingView, contentView, errorView);
    }

    protected abstract String getErrorMessage(Throwable e, boolean pullToRefresh);

    protected void showLightError(String msg) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onErrorViewClicked() {
        loadData(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

        String errorMsg = getErrorMessage(e, pullToRefresh);

        if (pullToRefresh) {
            showLightError(errorMsg);
        } else {
            errorView.setText(errorMsg);
            animateErrorViewIn();
        }
    }

    protected void animateErrorViewIn() {
        LceUtils.showErrorView(loadingView, contentView, errorView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingView = null;
        contentView = null;
        errorView = null;
    }
}
