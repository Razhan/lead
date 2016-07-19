package com.ef.cat.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ef.cat.R;
import com.ef.cat.ui.fragment.TestFragment;
import com.ef.cat.ui.widget.CollectInfoAdapter;
import com.ef.cat.ui.widget.FixedViewPager;
import com.ef.cat.ui.widget.ZoomOutSlideTransformer;
import com.ran.delta.presentation.ui.activity.BaseActivity;
import com.ran.delta.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectInfoActivity extends BaseActivity {

    @BindView(R.id.collect_info_viewpager)
    FixedViewPager viewpager;
    @BindView(R.id.collect_info_indicator)
    CircleIndicator indicator;

    private List<Fragment> fragmentList;

    @Override
    public int bindLayout() {
        return R.layout.activity_collect_info;
    }

    @Override
    public void initView() {
        super.initView();
        fragmentList = new ArrayList<>();
        fragmentList.add(TestFragment.newInstance("1"));
        fragmentList.add(TestFragment.newInstance("2"));
        fragmentList.add(TestFragment.newInstance("3"));
        fragmentList.add(TestFragment.newInstance("4"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewpager.setAdapter(new CollectInfoAdapter(fragmentManager, fragmentList));
        viewpager.setPageTransformer(true, new ZoomOutSlideTransformer());
        indicator.setViewPager(viewpager);
    }

    @OnClick(R.id.collect_info_next)
    public void onClick() {
        if (viewpager.getCurrentItem() < fragmentList.size() - 1) {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
        }
    }

//    ActionBar actionbar = getSupportActionBar();
//    if (actionbar != null) {
//        actionbar.setDisplayHomeAsUpEnabled(count > 0);
//        actionbar.setDisplayShowHomeEnabled(count > 0);
//    }

}
