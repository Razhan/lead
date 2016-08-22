package com.ef.newlead.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.transition.Slide;
import android.view.Gravity;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.AgeFragment;
import com.ef.newlead.ui.fragment.CityLocationFragment;
import com.ef.newlead.ui.fragment.LevelFragment;
import com.ef.newlead.ui.fragment.NumberFragment;
import com.ef.newlead.ui.fragment.PurposeFragment;
import com.ef.newlead.util.SharedPreUtils;

import java.lang.reflect.Constructor;
import java.util.Map;


public class CollectInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static int fragmentIndex = 0;
    private Fragment fragment;
    private static Map<String, Class<?>> fragmentMapper;

    static {
        fragmentMapper = new ArrayMap<>();
        fragmentMapper.put("age", AgeFragment.class);
        fragmentMapper.put("purpose", PurposeFragment.class);
        fragmentMapper.put("level", LevelFragment.class);
        fragmentMapper.put("city", CityLocationFragment.class);
        fragmentMapper.put("phone", NumberFragment.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        doubleClickExit = true;

        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_collect_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collect_info_fragment, getNextFragment())
                    .commit();
        }
    }

    public Fragment getNextFragment() {
        String fragmentStr = SharedPreUtils.getString(Constant.USER_RULE, "");

        if (fragmentStr.isEmpty()) {
            return null;
        }

        String[] fragments = fragmentStr.split(" \\| ");
//        try {
//            fragment = (Fragment)fragmentMapper.get(fragments[fragmentIndex++]).newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        fragment = NumberFragment.newInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
            fragment.setExitTransition(new Slide(Gravity.LEFT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
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
