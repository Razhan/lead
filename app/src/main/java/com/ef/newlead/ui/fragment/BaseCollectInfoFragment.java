package com.ef.newlead.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.presenter.Presenter;
import com.ef.newlead.ui.activity.CollectInfoActivity;
import com.ef.newlead.util.SystemText;

import butterknife.BindView;

public abstract class BaseCollectInfoFragment<P extends Presenter> extends BaseMVPFragment<P> {

    protected FragmentManager fragmentManager;

    protected boolean inProgress = false;

    @BindView(R.id.wrapper)
    View rootView;

    private CollectInfoActivity getCollectInfoActivity() {
        return (CollectInfoActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @CallSuper
    @Override
    public void initView() {
        rootView.setBackground(getGradientDrawable());
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
            return SystemText.getSystemText(getContext(), "info_collection_submit");
        } else {
            return SystemText.getSystemText(getContext(), "info_collection_next");
        }
    }

    private GradientColor getColor() {
        return getCollectInfoActivity().getColor();
    }

    @NonNull
    private GradientDrawable getGradientDrawable() {
        GradientColor color = getColor();
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{color.getTopGradient().toHex(), color.getBottomGradient().toHex()});
    }

}
