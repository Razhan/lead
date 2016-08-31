package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.adapter.SummaryDialogueAdapter;

import java.util.List;

import butterknife.BindView;

public class DialogueListActivity extends BaseActivity {

    @BindView(R.id.dialogue_list)
    RecyclerView list;

    private GradientColor gradientColor;
    private List<Dialogue.DialogBean> allDialogBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        gradientColor = new GradientColor(new GradientColor.GradientBean(248, 193, 68, 255),
                new GradientColor.GradientBean(246, 111, 159, 255), 0);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_dialogue_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initData();

        initRecyclerView();
    }

    private void initData() {
        allDialogBeans = getIntent().getParcelableArrayListExtra("AllDialogueBeans");
    }

    @Override
    protected GradientColor getStatusGradientColor() {
        return gradientColor;
    }

    @Override
    protected String setToolBarText() {
        return "At the cafe";
    }

    private void initRecyclerView() {
        allDialogBeans.addAll(allDialogBeans);

        list.setLayoutManager(new LinearLayoutManager(this));
        SummaryDialogueAdapter mAdapter = new SummaryDialogueAdapter(this, allDialogBeans);

        list.setAdapter(mAdapter);
    }


}
