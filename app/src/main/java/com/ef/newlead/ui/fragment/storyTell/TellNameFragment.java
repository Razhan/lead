package com.ef.newlead.ui.fragment.storyTell;

import android.support.v4.app.Fragment;

import com.ef.newlead.R;
import com.ef.newlead.ui.fragment.BaseFragment;
import com.ef.newlead.ui.widget.DeletableEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TellNameFragment extends BaseFragment {

    @BindView(R.id.tell_name_input)
    DeletableEditText input;

    public static Fragment newInstance() {
        return new TellNameFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_tell_name;
    }

    @Override
    public void initView() {

    }
}
