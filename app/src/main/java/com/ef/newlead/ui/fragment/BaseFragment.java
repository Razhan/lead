package com.ef.newlead.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.activity.PermissionListener;
import com.ef.newlead.util.SystemText;
import com.tbruyelle.rxpermissions.RxPermissions;

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

    protected String getLocaleText(String key) {
        return SystemText.getSystemText(getContext(), key);
    }

    @NonNull
    protected GradientColor getDefaultGradientColor() {
        int color = 0x6194D0;

        int a = 1;
        int r = 0xFF & color >> 16;
        int g = 0xFF & color >> 8;
        int b = 0xFF & color;
        GradientColor.GradientBean gradient = new GradientColor.GradientBean(r, g, b, a);
        return new GradientColor(gradient, gradient, 1);
    }

    protected void askForPermissions(PermissionListener listener, String... permissions) {
        RxPermissions.getInstance(getActivity().getApplicationContext())
                .request(permissions)
                .subscribe(granted -> {
                    if (granted) {
                        listener.permissionGranted();
                    } else {
                        listener.permissionDenied();
                    }
                });
    }
}
