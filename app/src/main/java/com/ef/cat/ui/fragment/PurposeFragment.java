package com.ef.cat.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ef.cat.R;
import com.ran.delta.presentation.ui.fragment.BaseFragment;

public class PurposeFragment extends BaseFragment {

    public static PurposeFragment newInstance() {
        return new PurposeFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_purpose;
    }

    @Override
    public void initView() {

    }
}