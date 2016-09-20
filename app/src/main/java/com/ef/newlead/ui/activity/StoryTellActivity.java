package com.ef.newlead.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.storyTell.TellLocationFragment;
import com.ef.newlead.ui.fragment.storyTell.TellNameFragment;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;

public class StoryTellActivity extends BaseActivity {

    private final static int DEFAULT_SHOW_TIME = 3000;

    @BindView(R.id.story_question)
    TextView question;
    @BindView(R.id.story_question_translation)
    TextView translation;
    @BindView(R.id.story_bottom_bar_text)
    TextView bottomBarText;
    @BindView(R.id.story_bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.tell_scroll_view)
    NestedScrollView scrollView;

    private Handler mHandler = new Handler();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_story_tell;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        bottomBar.post(() -> animateBottomBar(true));
        mHandler.postDelayed(() -> animateBottomBar(false), DEFAULT_SHOW_TIME);

        scrollView.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.story_fragment_container, TellNameFragment.newInstance())
                    .commit();
        }
    }

    private void animateBottomBar(boolean isEntry) {
        ObjectAnimator animator;

        if (isEntry) {
            animator = ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBar.getHeight(), 0);
            animator.setInterpolator(new LinearInterpolator());
        } else {
            animator = ObjectAnimator.ofFloat(bottomBar, "translationY", 0, bottomBar.getHeight());
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
