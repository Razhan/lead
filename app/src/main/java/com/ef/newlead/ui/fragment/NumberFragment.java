package com.ef.newlead.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.IndicatedProgressView;

import butterknife.BindView;

public class NumberFragment extends BaseFragment {

    @BindView(R.id.number_wrapper)          RelativeLayout numberWrapper;
    @BindView(R.id.phone_progress_view)     IndicatedProgressView progressView;

    public static NumberFragment newInstance() {
        return new NumberFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public void initView() {
        setBackground();

//        progressView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                progressView.startAnim();
//                progressView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
    }

    private void setBackground() {
//        String backgroundStr = SystemText.getSystemText(getContext(), "age_select_gradient_color");
//        GradientBackground background = new Gson().fromJson(backgroundStr,
//                new TypeToken<GradientBackground>() {}.getType());
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#f8c144"), Color.parseColor("#f66f9f")});
        numberWrapper.setBackground(drawable);
    }

}
