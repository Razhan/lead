package com.ef.newlead.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Level;
import com.ef.newlead.ui.widget.CardSlideView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LevelFragment extends BaseCollectInfoFragment implements CardSlideView.CardSlideListener {

    @BindView(R.id.level_card_slide)
    CardSlideView cardSlide;
    @BindView(R.id.level_title)
    TextView title;

    public static Fragment newInstance() {
        return new LevelFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_level;
    }

    @Override
    public void initView() {
        super.initView();

        title.setText(getLocaleText("level_select_title"));
        String jsonStr = getLocaleText("level_select");
        List<Level> levels = new Gson().fromJson(jsonStr, new TypeToken<List<Level>>() {
        }.getType());

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

    @Override
    public void onSlide(int count) {
        Log.d("onSlide", String.valueOf(count));
    }

    @Override
    public void onFinish() {
        startNextFragment();
    }

}
