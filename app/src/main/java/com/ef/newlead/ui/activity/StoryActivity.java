package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ef.newlead.R;

import butterknife.BindView;
import butterknife.OnClick;

public class StoryActivity extends BaseActivity {
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    @BindView(R.id.buttonNext)
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_story;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        StoryActivityFragment fragment = new StoryActivityFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.rootLayout, fragment).commit();
        }
    }

    @OnClick(R.id.buttonNext)
    void onNext() {
        /*Fragment fragment = new TextFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.setTransition(TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null).commitAllowingStateLoss();*/

        /*Intent i = new Intent(this, TextActivity.class);
        startActivity(i);*/
    }
}
