package com.ef.newlead.ui.fragment;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientBackground;
import com.ef.newlead.ui.activity.CollectInfoActivity;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class BaseCollectInfoFragment extends BaseFragment {

    protected boolean inProgress = false;

    protected Drawable getBackgroundDrawable(String key) {
        String backgroundStr = SystemText.getSystemText(getContext(), key);
        GradientBackground background = new Gson().fromJson(backgroundStr,
                new TypeToken<GradientBackground>() {
                }.getType());

        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{background.getBottomGradient().toHex(), background.getTopGradient().toHex()});
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
