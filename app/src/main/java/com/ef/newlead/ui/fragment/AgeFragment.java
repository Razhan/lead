package com.ef.newlead.ui.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Age;
import com.ef.newlead.ui.adapter.AgeAdapter;
import com.ef.newlead.ui.widget.flowview.FlowView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AgeFragment extends BaseCollectInfoFragment implements FlowView.CoverFlowItemListener {

    private static final int DEFAULT_POSITION = 5;

    @BindView(R.id.age_cover_flow)
    FlowView ageCoverFlow;
    @BindView(R.id.age_wrapper)
    FrameLayout ageWrapper;
    @BindView(R.id.age_title)
    TextView title;
    @BindView(R.id.age_next_button)
    Button next;

    private AgeAdapter mAdapter;

    public static Fragment newInstance() {
        return new AgeFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_age;
    }

    @Override
    public void initView() {
        ageWrapper.setBackground(getGradientDrawable("age_select_gradient_color"));

        title.setText(getLocaleText("age_select_title"));
        next.setText(getLocaleText("age_select_next"));

        String ageStr = getLocaleText("age_select");
        List<Age> ages = new Gson().fromJson(ageStr, new TypeToken<LinkedList<Age>>() {
        }.getType());

        ageCoverFlow.setOrientation(FlowView.VERTICAL);
        mAdapter = new AgeAdapter(getContext(), ages);
        mAdapter.setClickListener((v, pos, item) -> ageCoverFlow.scrollToCenter(pos));

        ageCoverFlow.setAdapter(mAdapter);
        ageCoverFlow.setCoverFlowListener(this);
        ageCoverFlow.postDelayed(() -> ageCoverFlow.scrollToCenter(DEFAULT_POSITION), Constant.DEFAULT_ANIM_FULL_TIME);
    }

    @Override
    public void onItemSelected(int position) {
        Log.i(TAG, "onItemSelectedSelected" + position);
    }

    @OnClick(R.id.age_next_button)
    public void OnClick() {
        startNextFragment();
    }
}
