package com.ef.newlead.ui.fragment.storyTell;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ef.newlead.R;
import com.ef.newlead.presenter.Presenter;
import com.ef.newlead.ui.adapter.DestinationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TellDestinationFragment extends BaseTellFragment {

    @BindView(R.id.tell_destination_list)
    RecyclerView list;

    public static Fragment newInstance() {
        return new TellDestinationFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_tell_destination;
    }

    @Override
    public void initView() {
        super.initView();
        initListView();
    }

    private void initListView() {
        List<String> des = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            des.add("");
        }

        DestinationAdapter adapter = new DestinationAdapter(getContext(), des);
        adapter.setClickListener((view, pos, item) -> startRecordActivity(
                "I want to travel to %s.",
                "Barcelona",
                "Now try saying it.",
                "An Excellent Choice!",
                R.drawable.bg_story_destination));

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);
    }

    @Override
    protected Presenter createPresent() {
        return null;
    }

    @Override
    protected String[] getHeaderText() {
        return new String[] {
                "I want to travel to…",
                "我想去…旅行。",
                "Talk about your travel plans."};
    }
}
