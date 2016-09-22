package com.ef.newlead.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.storyTell.TellDestinationFragment;
import com.ef.newlead.ui.fragment.storyTell.TellLocationFragment;
import com.ef.newlead.ui.fragment.storyTell.TellNameFragment;
import com.ef.newlead.util.ViewUtils;

import butterknife.BindView;

public class StoryTellActivity extends BaseActivity {

    private final static int DEFAULT_SHOW_TIME = 3000;
    private static ArrayMap<Integer, Class<?>> fragmentMapper;

    private int fragmentIndex = 0;
    private Fragment fragment;

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
    @BindView(R.id.story_question_wrapper)
    RelativeLayout questionWrapper;
    @BindView(R.id.story_error_title)
    TextView errorTitle;
    @BindView(R.id.story_error_info)
    TextView errorInfo;
    @BindView(R.id.story_error_wrapper)
    LinearLayout errorWrapper;
    @BindView(R.id.story_error_retry)
    Button errorRetry;

    private Handler mHandler = new Handler();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    static {
        fragmentMapper = new ArrayMap<>();
        fragmentMapper.put(0, TellNameFragment.class);
        fragmentMapper.put(1, TellLocationFragment.class);
        fragmentMapper.put(2, TellDestinationFragment.class);
        fragmentMapper.put(3, TellDestinationFragment.class);
    }

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

        scrollView.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });

        if (savedInstanceState == null) {
            loadNextFragment();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            }
        });

        loadNextFragment();
    }

    private void loadNextFragment() {
        if (fragmentIndex > fragmentMapper.size() - 1) {
            startActivity(new Intent(this, ScoreActivity.class));
            return;
        }

        try {
            fragment = (Fragment) fragmentMapper.get(fragmentIndex++).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.story_fragment_container, fragment)
                .commit();

        bottomBar.post(() -> animateBottomBar(true));
        mHandler.postDelayed(() -> animateBottomBar(false), DEFAULT_SHOW_TIME);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
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

    public NestedScrollView getNestedScrollView() {
        return scrollView;
    }

    public View getHeaderView() {
        return questionWrapper;
    }

    public void showPermissionDeniedView(String[] permissions) {
        errorWrapper.setVisibility(View.VISIBLE);

        errorRetry.setOnClickListener(v ->
            askForPermissions(new PermissionListener() {
                @Override
                public void permissionGranted() {
                    errorWrapper.setVisibility(View.GONE);
                }

                @Override
                public void permissionDenied() {

                }
            }, permissions)
        );
    }

    public void initHeaderView(String[] headerStr) {
        if (headerStr.length < 3) {
            return;
        }

        this.question.setText(headerStr[0]);
        this.translation.setText(headerStr[1]);
        this.bottomBarText.setText(headerStr[2]);
    }

}
