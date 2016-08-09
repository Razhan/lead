package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.CardSlideView;

import butterknife.BindView;

public class LevelFragment extends BaseFragment implements CardSlideView.CardSlideListener{

    @BindView(R.id.level_card_slide)    CardSlideView cardSlide;
    @BindView(R.id.level_wrapper)       LinearLayout levelWrapper;

    public static LevelFragment newInstance() {
        return new LevelFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_level;
    }

    @Override
    public void initView() {
        setBackground();

        cardSlide.setListener(this);
    }

    private void setBackground() {
//        String backgroundStr = SystemText.getSystemText(getContext(), "age_select_gradient_color");
//        GradientBackground background = new Gson().fromJson(backgroundStr,
//                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#33bbeb"), Color.parseColor("#2fd1d6")});
        levelWrapper.setBackground(drawable);
    }

    @Override
    public void onSlide(int count) {
        Log.d("onSlide", String.valueOf(count));
    }
}
