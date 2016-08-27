package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.adapter.DialogueAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DialogueActivity extends BaseActivity {

    @BindView(R.id.dialogue_list)
    RecyclerView list;

    private DialogueAdapter mAdapter;
    private GradientColor gradientColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        gradientColor = new GradientColor(new GradientColor.GradientBean(248, 193, 68, 255),
                new GradientColor.GradientBean(246, 111, 159, 255), 0);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_dialogue;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initRecyclerView();
    }

    @Override
    protected GradientColor getStatusGradientColor() {
        return gradientColor;
    }

    @Override
    protected boolean showBackIcon() {
        return true;
    }

    @Override
    protected String setToolBarText() {
        return "At the cafe";
    }

    private void initRecyclerView() {
        List<Dialogue> dialogues = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dialogues.add(new Dialogue());
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DialogueAdapter(this, dialogues);

        list.setAdapter(mAdapter);
    }


}
