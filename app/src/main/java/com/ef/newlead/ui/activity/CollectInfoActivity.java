package com.ef.newlead.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.NumberFragment;

public class CollectInfoActivity extends BaseActivity {

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
        Fragment fragment = NumberFragment.newInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.START);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
            fragment.setAllowReturnTransitionOverlap(true);
        }

        return fragment;
    }

}
