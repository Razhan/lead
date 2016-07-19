package com.ran.delta.presentation.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;

import com.ran.delta.presentation.presenter.MvpPresenter;
import com.ran.delta.presentation.ui.view.MvpLceView;

public abstract class MvpLceRefreshActivity<M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceActivity<SwipeRefreshLayout, M, V, P>
        implements SwipeRefreshLayout.OnRefreshListener {


    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

}
