package com.ef.newlead.ui.fragment;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ef.newlead.data.model.GradientBackground;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private static final String SAVED_STATE = "savedState";

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(SAVED_STATE);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(bindLayout(), container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public abstract int bindLayout();

    public abstract void initView();

    public Context getContext() {
        return getActivity();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_STATE, isHidden());
    }

    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    protected GradientDrawable getGradientDrawable(String colorNode) {
        String backgroundStr = SystemText.getSystemText(getContext(), colorNode);
        GradientBackground background = new Gson().fromJson(backgroundStr,
                new TypeToken<GradientBackground>() {}.getType());
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{background.getBottomGradient().toHex(), background.getTopGradient().toHex()});
    }
}
