package com.ef.newlead.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.IndicatedProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneNumberFragment extends BaseFragment {

    @BindView(R.id.phone_progress_view)     IndicatedProgressView progressView;

    public static PhoneNumberFragment newInstance() {
        return new PhoneNumberFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_phone_number;
    }

    @Override
    public void initView() {
        progressView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                progressView.startAnim();
                progressView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

}
