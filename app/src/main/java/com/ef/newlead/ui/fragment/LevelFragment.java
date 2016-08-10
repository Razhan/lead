package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Level;
import com.ef.newlead.ui.widget.CardSlideView;
import com.ef.newlead.util.SystemText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

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

        String jsonStr = SystemText.getSystemText(getContext(), "level_select");
        List<Level> levels = new Gson().fromJson(jsonStr, new TypeToken<List<Level>>(){}.getType());

        cardSlide.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardSlide.setData(levels);
                cardSlide.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

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
