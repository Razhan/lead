package com.ef.cat.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.cat.R;
import com.ef.cat.data.model.Interest;
import com.ef.cat.ui.adapter.InterestAdapter;
import com.ran.delta.presentation.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterestFragment extends BaseFragment {

    @BindView(R.id.info_back)           ImageView infoBack;
    @BindView(R.id.info_title)          TextView infoTitle;
    @BindView(R.id.interest_list)       RecyclerView interestList;

    InterestAdapter mAdapter;

    public static InterestFragment newInstance() {
        return new InterestFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_interest;
    }

    @Override
    public void initView() {
        interestList.setLayoutManager(new LinearLayoutManager(getContext()));

        String a = "awdawd";
        String b = "ssdd";
        String c = "vhgth";
        String d = "jloo;o";

        List<String> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);

        List<Interest> dd = Interest.generateInterests(list);

        mAdapter = new InterestAdapter(getContext(), dd);
        interestList.setAdapter(mAdapter);
    }
}