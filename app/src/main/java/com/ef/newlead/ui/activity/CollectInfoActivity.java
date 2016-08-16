package com.ef.newlead.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.LevelFragment;


public class CollectInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Fragment fragment;

    private static int fragmentIndex = 0;

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.collect_info_fragment, getFragment())
                .commit();
    }

    public Fragment getFragment() {
        //just for temporary test
        Fragment fragment = LevelFragment.newInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
            fragment.setAllowReturnTransitionOverlap(true);
        }

        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
