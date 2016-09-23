package com.ef.newlead.ui.fragment.collectInfo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Age;
import com.ef.newlead.data.model.GradientColor;
import com.ef.newlead.ui.adapter.AgeAdapter;
import com.ef.newlead.ui.widget.flowview.FlowView;
import com.ef.newlead.util.SharedPreUtils;
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

    @BindView(R.id.age_title)
    TextView title;

    @BindView(R.id.age_next_button)
    Button next;

    @BindView(R.id.close)
    ViewGroup closeImage;

    private AgeAdapter mAdapter;
    private String ageSelected;

    public interface AgeSelectionListener {
        void onAge(String value);
    }

    private AgeSelectionListener ageSelectionListener;

    public AgeFragment setAgeSelectionListener(AgeSelectionListener ageSelectionListener) {
        this.ageSelectionListener = ageSelectionListener;
        return this;
    }

    public static AgeFragment newInstance(boolean standalone) {
        AgeFragment ageFragment = new AgeFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(STANDALONE, standalone);
        ageFragment.setArguments(bundle);

        return ageFragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_age;
    }

    @Override
    public void initView() {
        super.initView();
        title.setText(getLocaleText("age_select_title"));
        next.setText(getContinueText());

        String ageStr = getLocaleText("age_select");
        List<Age> ages = new Gson().fromJson(ageStr, new TypeToken<LinkedList<Age>>() {
        }.getType());

        ageCoverFlow.setOrientation(FlowView.VERTICAL);
        mAdapter = new AgeAdapter(getContext(), ages);
        mAdapter.setClickListener((v, pos, item) -> ageCoverFlow.scrollToCenter(pos));

        ageCoverFlow.setAdapter(mAdapter);
        ageCoverFlow.setCoverFlowListener(this);
        ageCoverFlow.postDelayed(() -> ageCoverFlow.scrollToCenter(DEFAULT_POSITION), 400);

        if (isStandalone()) {
            closeImage.setVisibility(View.VISIBLE);
            ((RelativeLayout.LayoutParams) title.getLayoutParams()).topMargin = 0;
        } else {
            closeImage.setVisibility(View.GONE);
        }
    }

    @Override
    protected GradientColor getColor() {
        if (isStandalone()) {
            return getDefaultGradientColor();
        } else {
            return super.getColor();
        }
    }

    @Override
    public void onItemSelected(int position) {
        SharedPreUtils.putString(Constant.USER_AGE_INDEX, String.valueOf(position - mAdapter.getBorder()));
        SharedPreUtils.putString(Constant.USER_AGE_VALUE, mAdapter.getItem(position).getAge());

        ageSelected = mAdapter.getItem(position).getAge();
    }

    @OnClick(R.id.age_next_button)
    public void OnClick() {
        if (ageSelectionListener != null) {
            ageSelectionListener.onAge(ageSelected);
        }
    }

    @OnClick(R.id.close)
    public void onClose() {
        getActivity().finish();
    }

}
