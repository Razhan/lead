package com.ef.newlead.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Age;
import com.ef.newlead.data.model.GradientBackground;
import com.ef.newlead.ui.adapter.AgeAdapter;
import com.ef.newlead.ui.widget.flowview.FlowView;
import com.ef.newlead.Constant;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class AgeFragment extends BaseFragment implements FlowView.CoverFlowItemListener {

    private static final int DEFAULT_POSITION = 3;

    @BindView(R.id.age_cover_flow)
    FlowView ageCoverFlow;
    @BindView(R.id.age_wrapper)
    FrameLayout ageWrapper;
    @BindView(R.id.age_title)
    TextView title;
    @BindView(R.id.age_next_button)
    Button next;

    private AgeAdapter mAdapter;

    public static AgeFragment newInstance() {
        return new AgeFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_age;
    }

    @Override
    public void initView() {
        setBackground();

        title.setText(SystemText.getSystemText(getContext(), "age_select_title"));
        next.setText(SystemText.getSystemText(getContext(), "age_select_next"));

        String ageStr = SystemText.getSystemText(getContext(), "age_select");
        List<Age> ages = new Gson().fromJson(ageStr, new TypeToken<LinkedList<Age>>() {}.getType());

        ageCoverFlow.setOrientation(FlowView.VERTICAL);
        mAdapter = new AgeAdapter(getContext(), ages);
        mAdapter.setClickListener((v, pos, item) -> ageCoverFlow.scrollToCenter(pos));

        ageCoverFlow.setAdapter(mAdapter);
        ageCoverFlow.setCoverFlowListener(this);
        ageCoverFlow.postDelayed(() -> ageCoverFlow.scrollToCenter(DEFAULT_POSITION), Constant.DEFAULT_ANIM_HALF_TIME);
    }

    private void setBackground() {
        String backgroundStr = SystemText.getSystemText(getContext(), "age_select_gradient_color");
        GradientBackground background = new Gson().fromJson(backgroundStr,
                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{background.getBottomGradient().toHex(), background.getTopGradient().toHex()});
        ageWrapper.setBackground(drawable);
    }

    @Override
    public void onItemSelected(int position) {
        Log.i(TAG, "onItemSelectedSelected" + position);
    }

}
