package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.NumberFragment;
import com.ef.newlead.ui.fragment.VerificationFragment;

public class CollectInfoActivity extends BaseActivity {

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
                .replace(R.id.collect_fragment, NumberFragment.newInstance())
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
