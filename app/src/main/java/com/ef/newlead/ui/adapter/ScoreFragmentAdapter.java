package com.ef.newlead.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ef.newlead.ui.fragment.NextLessonFragment;
import com.ef.newlead.ui.fragment.ScoreFragment;

public class ScoreFragmentAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> fragments = new SparseArray<>();

    private static int ITEM_COUNT = 2;

    public ScoreFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ScoreFragment.newInstance();
            case 1:
                return NextLessonFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}