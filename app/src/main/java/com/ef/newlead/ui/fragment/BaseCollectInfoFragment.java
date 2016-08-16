package com.ef.newlead.ui.fragment;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ef.newlead.R;
import com.ef.newlead.ui.activity.CollectInfoActivity;

public abstract class BaseCollectInfoFragment extends BaseFragment {

    protected boolean inProgress = false;

    protected Drawable getBackgroundDrawable(String key) {
        return getGradientDrawable(key);
    }

    protected final void startNextFragment(boolean addToBackStack) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.collect_info_fragment, getFragment());

        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        ft.commit();
    }

    protected Fragment getFragment() {
        return ((CollectInfoActivity) getActivity()).getFragment();
    }
}
