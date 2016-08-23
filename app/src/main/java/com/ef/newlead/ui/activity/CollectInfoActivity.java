package com.ef.newlead.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.transition.Slide;
import android.view.Gravity;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.fragment.AgeFragment;
import com.ef.newlead.ui.fragment.CityLocationFragment;
import com.ef.newlead.ui.fragment.LevelFragment;
import com.ef.newlead.ui.fragment.NumberFragment;
import com.ef.newlead.ui.fragment.PurposeFragment;
import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class CollectInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private int fragmentIndex = 0;
    private Fragment fragment;

    private String[] fragmentKeys;
    private static Map<String, Class<?>> fragmentMapper;
    private List<GradientColor> colors;
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

        String background = SystemText.getSystemText(this, "info_color_gradients");
        String fragmentStr = SharedPreUtils.getString(Constant.USER_RULE, "");

        colors = new Gson().fromJson(background,
                new TypeToken<List<GradientColor>>() {
                }.getType());

        fragmentKeys = fragmentStr.split(" \\| ");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collect_info_fragment, getNextFragment())
                    .commit();
        }
    }

    public Fragment getNextFragment() {
        if (fragmentIndex >= fragmentKeys.length) {
            finish();
        }

        try {
            fragment = (Fragment)fragmentMapper.get(fragmentKeys[fragmentIndex++]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
            fragment.setExitTransition(new Slide(Gravity.LEFT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
            fragment.setAllowReturnTransitionOverlap(true);
        }

        return fragment;
    }

    public boolean isLastFragment() {
        if (fragmentIndex == fragmentMapper.size()) {
            return true;
        }

        return false;
    }

    public GradientColor getColor() {
        return colors.get(fragmentIndex - 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
