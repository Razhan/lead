package com.ef.newlead.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ef.newlead.R;
import com.ef.newlead.presenter.Presenter;
import com.ef.newlead.ui.activity.CollectInfoActivity;

public abstract class BaseCollectInfoFragment<P extends Presenter> extends BaseMVPFragment<P> {

    protected FragmentManager fragmentManager;

    protected boolean inProgress = false;

    private CollectInfoActivity getCollectInfoActivity() {
        return (CollectInfoActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    protected P createPresent() {
        return null;
    }

    protected final void startNextFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.collect_info_fragment, getNextFragment()).commit();
    }

    protected Fragment getNextFragment() {
        return getCollectInfoActivity().getNextFragment();
    }

    protected String getContinueText() {
        if (getCollectInfoActivity().isLastFragment()) {
            return "提交";
        } else {
            return "继续";
        }
    }



}
