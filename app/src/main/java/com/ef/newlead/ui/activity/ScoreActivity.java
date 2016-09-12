package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ef.newlead.R;
import com.ef.newlead.ui.adapter.ScoreFragmentAdapter;

import butterknife.BindView;

public class ScoreActivity extends BaseActivity {

    @BindView(R.id.score_viewpager)
    ViewPager viewpager;
    @BindView(R.id.score_indicator_wrapper)
    RelativeLayout Indicators;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_score;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        viewpager.setAdapter(new ScoreFragmentAdapter(getSupportFragmentManager()));

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                int otherPos = position == 0 ? 1 : 0;

                ((ImageView) Indicators.getChildAt(position)).setColorFilter(Color.BLACK);
                ((ImageView) Indicators.getChildAt(otherPos)).setColorFilter(Color.parseColor("#66000000"));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ScoreFragmentAdapter) viewpager.getAdapter()).getFragment(currentPage).onPause();
    }

    @Override
    protected void onDestroy() {
        ((ScoreFragmentAdapter) viewpager.getAdapter()).getFragment(currentPage).onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, RemindActivity.class);
        i.putExtra(RemindActivity.REMIND_TYPE_KEY, RemindActivity.TYPE_LEAVE);
        startActivity(i);
    }
}
