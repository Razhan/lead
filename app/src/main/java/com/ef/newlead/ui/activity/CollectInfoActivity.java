package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.transition.Slide;
import android.view.Gravity;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.domain.usecase.CollectInfoUseCase;
import com.ef.newlead.presenter.CollectInfoPresenter;
import com.ef.newlead.ui.fragment.AgeFragment;
import com.ef.newlead.ui.fragment.BaseCollectInfoFragment;
import com.ef.newlead.ui.fragment.CityLocationFragment;
import com.ef.newlead.ui.fragment.LevelFragment;
import com.ef.newlead.ui.fragment.NumberFragment;
import com.ef.newlead.ui.fragment.PurposeFragment;
import com.ef.newlead.ui.fragment.VerificationFragment;
import com.ef.newlead.ui.view.CollectInfoView;
import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.SystemText;
import com.google.android.exoplayer.util.Assertions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CollectInfoActivity extends BaseMVPActivity<CollectInfoPresenter>
        implements ActivityCompat.OnRequestPermissionsResultCallback, CollectInfoView,
        VerificationFragment.VerificationResultListener,
        AgeFragment.AgeSelectionListener,
        NumberFragment.PhoneNumberInputListener, BaseCollectInfoFragment.InfoCollectionListener {

    private static final String CURRENT_FRAGMENT = "currentFragment";
    private static ArrayMap<String, Class<?>> fragmentMapper;

    static {
        fragmentMapper = new ArrayMap<>();
        fragmentMapper.put("age", AgeFragment.class);
        fragmentMapper.put("purpose", PurposeFragment.class);
        fragmentMapper.put("level", LevelFragment.class);
        fragmentMapper.put("city", CityLocationFragment.class);
        fragmentMapper.put("phone", NumberFragment.class);
    }

    private int fragmentIndex;
    private Fragment fragment;
    private String[] fragmentKeys;
    private List<GradientColor> colors;
    private FragmentManager fragmentManager;

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

        fragmentIndex = Math.min(SharedPreUtils.getInt(CURRENT_FRAGMENT, 0), fragmentKeys.length - 1);

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.collect_info_fragment, getNextFragment(null))
                    .commit();
        }
    }

    @NonNull
    @Override
    protected CollectInfoPresenter createPresenter() {
        return new CollectInfoPresenter(this, this, new CollectInfoUseCase());
    }

    @Override
    public void afterSubmitInfo() {
        SharedPreUtils.putBoolean(Constant.USER_SAVED, true);

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public Fragment getNextFragment(Fragment targetFragment) {
        /*if (fragmentIndex >= fragmentKeys.length) {
            return null;
        }*/

        SharedPreUtils.putInt(CURRENT_FRAGMENT, fragmentIndex);

        if (targetFragment == null) {
            try {
                fragment = (Fragment) fragmentMapper.get(fragmentKeys[fragmentIndex++]).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            fragment = targetFragment;
        }

        if (fragment instanceof VerificationFragment) {
            ((VerificationFragment) fragment).setVerificationResultListener(this)
                    .setLastPage(true)
                    .setInfoCollectionListener(this);
        } else if (fragment instanceof AgeFragment) {
            ((AgeFragment) fragment).setAgeSelectionListener(this);
        } else if (fragment instanceof NumberFragment) {
            ((NumberFragment) fragment).setPhoneNumberInputListener(this);
        }

        Assertions.checkNotNull(fragment, "Invalid Fragment found");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Slide(Gravity.RIGHT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
            fragment.setExitTransition(new Slide(Gravity.LEFT).setDuration(Constant.DEFAULT_ANIM_FULL_TIME));
            fragment.setAllowReturnTransitionOverlap(true);
        }

        return fragment;
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

    protected final void startNextFragment(Fragment fragment) {
        Fragment nextFragment = getNextFragment(fragment);

        if (nextFragment == null) {
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.collect_info_fragment, nextFragment).commit();
    }

    @Override
    public void onPhoneNumVerified(String phoneNum) {
        //presenter.submitInfo();
    }

    @Override
    public void onAge(String value) {
        startNextFragment(null);
    }

    @Override
    public void onInputComplete(String phone) {
        Fragment fragment = getVerificationFragment(false, phone);
        startNextFragment(fragment);
    }

    public static Fragment getVerificationFragment(boolean standalone, String phone) {
        Fragment fragment = VerificationFragment.newInstance(standalone, phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.RIGHT);
            slide.setDuration(Constant.DEFAULT_ANIM_FULL_TIME);

            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
        }

        return fragment;
    }

    @Override
    public void onComplete() {
        presenter.submitInfo();
    }
}
