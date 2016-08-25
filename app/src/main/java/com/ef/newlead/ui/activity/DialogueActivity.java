package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.ui.adapter.DialogueAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DialogueActivity extends BaseActivity {

    @BindView(R.id.dialogue_list)
    RecyclerView list;

    private DialogueAdapter mAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_dialogue;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initRecyclerView();
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
