package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.DialogueView;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @BindView(R.id.video_dialogue)
    DialogueView dialogueView;
    @BindView(R.id.b1)
    Button b1;
    @BindView(R.id.b2)
    Button b2;
    @BindView(R.id.b3)
    Button b3;
    @BindView(R.id.dialogue_switch)
    Switch dialogueSwitch;

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

        dialogueView.setChildCount(4);
        dialogueView.setData(null);

        dialogueSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                dialogueView.showTranslation();
            } else {
                dialogueView.hideTranslation();
            }
        });
    }

    @OnClick({R.id.b1, R.id.b2, R.id.b3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                dialogueView.showChild();
                break;
            case R.id.b2:
                dialogueView.hideChildren();
                break;
            case R.id.b3:
                break;
        }
    }


}
