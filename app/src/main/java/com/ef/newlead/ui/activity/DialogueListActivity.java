package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Dialogue;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.adapter.SummaryDialogueAdapter;
import com.ef.newlead.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogueListActivity extends BaseActivity {

    public final static String DIALOGUE_KEY = "AllDialogueBeans";

    @BindView(R.id.dialogue_list)
    RecyclerView list;
    @BindView(R.id.dialogue_favorite)
    ImageView favorite;
    @BindView(R.id.dialogue_bottom_bar)
    CardView bottomBar;

    private boolean favored = false;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomBar.setCardElevation(ViewUtils.dpToPx(this, 5));
        }

        initData();
        initRecyclerView();
    }

    private void initData() {
        allDialogBeans = getIntent().getParcelableArrayListExtra(DIALOGUE_KEY);
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
        list.setLayoutManager(new LinearLayoutManager(this));
        SummaryDialogueAdapter mAdapter = new SummaryDialogueAdapter(this, allDialogBeans);

        list.setAdapter(mAdapter);
    }

    @OnClick({R.id.dialogue_favorite, R.id.dialogue_start, R.id.dialogue_continue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogue_favorite:
                if (favored) {
                    favorite.setImageResource(R.drawable.ic_favorite_empty);
                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_fill);
                }
                favored = !favored;
                break;
            case R.id.dialogue_start:
                startActivity(new Intent(this, RolePlayActivity.class));
                break;
            case R.id.dialogue_continue:
                onBackPressed();
                break;
        }
    }
}
