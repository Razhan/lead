package com.ef.newlead.ui.fragment.collectInfo;

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
import com.ef.newlead.ui.fragment.BaseMVPFragment;
import com.ef.newlead.util.SystemText;

import butterknife.BindView;

public abstract class BaseCollectInfoFragment<P extends Presenter> extends BaseMVPFragment<P> {

    protected FragmentManager fragmentManager;

    protected boolean inProgress = false;

    public static final String STANDALONE = "standalone";

    private boolean lastPage = false;

    /***
     * Listener for tracing information collection actions.
     */
    public interface InfoCollectionListener {
        /***
         * Reaches the end of information collection.
         */
        void onComplete();
    }

    protected InfoCollectionListener infoCollectionListener;

    public BaseCollectInfoFragment setInfoCollectionListener(InfoCollectionListener listener) {
        this.infoCollectionListener = listener;
        return this;
    }

    public BaseCollectInfoFragment setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
        return this;
    }

    /***
     * Marks the current action as done.
     */
    protected void setActionCompletion() {
        if (isLastFragment() && infoCollectionListener != null) {
            infoCollectionListener.onComplete();
        }
    }

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

    // FIXME: Logic about switching fragment could be put into host activity
    protected final void startNextFragment() {
        Fragment nextFragment = getNextFragment();

        if (nextFragment == null) {
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.collect_info_fragment, nextFragment).commit();
    }

    protected Fragment getNextFragment() {
        return getCollectInfoActivity().getNextFragment(null);
    }

    protected String getContinueText() {
        if (isLastFragment()) {
            return SystemText.getSystemText(getContext(), "info_collection_submit");
        } else {
            return SystemText.getSystemText(getContext(), "info_collection_next");
        }
    }

    protected boolean isLastFragment() {
        return lastPage;
    }

    protected GradientColor getColor() {
        return getCollectInfoActivity().getColor();
    }

    @NonNull
    private GradientDrawable getGradientDrawable() {
        GradientColor color = getColor();
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{color.getTopGradient().toHex(), color.getBottomGradient().toHex()});
    }

    protected boolean isStandalone() {
        return getArguments() != null && getArguments().getBoolean(STANDALONE);
    }
}
