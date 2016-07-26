package com.ef.cat.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ef.cat.R;
import com.ef.cat.data.model.Level;
import com.ef.cat.ui.adapter.LevelAdapter;
import com.ef.cat.ui.widget.coverFlowView.CoverFlowView;
import com.ran.delta.presentation.ui.fragment.BaseFragment;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class LevelFragment extends BaseFragment implements CoverFlowView.CoverFlowItemListener {

    private LevelAdapter mAdapter;

    @BindView(R.id.cover_flow)  CoverFlowView coverFlow;

    public static LevelFragment newInstance() {
        return new LevelFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_level;
    }

    @Override
    public void initView() {

        List<Level> levels = new LinkedList<>();
        levels.add(new Level("1"));
        levels.add(new Level("2"));
        levels.add(new Level("3"));
        levels.add(new Level("4"));
        levels.add(new Level("5"));
        levels.add(new Level("6"));
        levels.add(new Level("7"));

        coverFlow.setOrientation(CoverFlowView.HORIZONTAL);
        mAdapter = new LevelAdapter(getContext(), levels);
        mAdapter.setClickListener((v, pos, item) -> coverFlow.scrollToCenter(pos));

        coverFlow.setAdapter(mAdapter);
        coverFlow.setCoverFlowListener(this);
        new Handler().post(() -> coverFlow.scrollToCenter(1));
    }

    @Override
    public void onItemChanged(int position) {
        Log.i(TAG, "onItemChanged" + position);
    }

    @Override
    public void onItemSelected(int position) {
        Log.i(TAG, "onItemSelectedSelected" + position);
    }

}
