package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.adapter.SummaryDialogueAdapter;
import com.ef.newlead.ui.adapter.VideoDialogueAdapter;

import java.util.ArrayList;
import java.util.List;

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
    Switch dialogueSwitch;
    @BindView(R.id.video_dialogue_list)
    RecyclerView list;
    private VideoDialogueAdapter mAdapter;

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
       initRecyclerView();


        dialogueSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
            } else {
            }
        });
    }

    private void initRecyclerView() {
        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideoDialogueAdapter(this, null);

        list.setAdapter(mAdapter);
    }

    @OnClick({R.id.b1, R.id.b2, R.id.b3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                List<Dialogue> dialogues = new ArrayList<>();
                dialogues.add(new Dialogue());
                mAdapter.add(dialogues);
                break;
            case R.id.b2:
                break;
            case R.id.b3:
                break;
        }
    }


}
