package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import com.ef.newlead.R;
import com.ef.newlead.ui.adapter.VideoDialogueAdapter;
import com.ef.newlead.ui.widget.SlideAnimator;
import com.ef.newlead.ui.widget.SmoothScrollLayoutManager;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @BindView(R.id.b1)
    Button b1;
    @BindView(R.id.b2)
    Button b2;
    @BindView(R.id.b3)
    Button b3;
    @BindView(R.id.dialogue_switch)
    SwitchCompat dialogueSwitch;
    @BindView(R.id.video_dialogue_list)
    RecyclerView list;

    private VideoDialogueAdapter mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mHandler = new Handler();

        initRecyclerView();

        dialogueSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mAdapter.showTranslation(true);
            } else {
                mAdapter.showTranslation(false);
            }
        });
    }

    private void initRecyclerView() {
        list.setLayoutManager(new SmoothScrollLayoutManager(this));
        list.setItemAnimator(new SlideAnimator());

        mAdapter = new VideoDialogueAdapter(this, null);

        list.setAdapter(mAdapter);
    }

    @OnClick({R.id.b1, R.id.b2, R.id.b3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                mAdapter.add(mAdapter.getItemCount(), null);
                list.smoothScrollToPosition(mAdapter.getItemCount());
                break;
            case R.id.b2:
                mAdapter.removeAll();
                break;
            case R.id.b3:
                break;
        }
    }

}
