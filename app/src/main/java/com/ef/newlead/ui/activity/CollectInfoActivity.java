package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.CityLocationFragment;

public class CollectInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_collect_info;
    }

    @Override
    public void initView() {
        super.initView();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment = new CityLocationFragment();
        transaction.replace(R.id.collect_fragment, fragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
