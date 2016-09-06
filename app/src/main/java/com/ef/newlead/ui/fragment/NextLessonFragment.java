package com.ef.newlead.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.activity.RemindActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NextLessonFragment extends BaseFragment {

    @BindView(R.id.next_type)
    TextView type;
    @BindView(R.id.next_title)
    TextView header;
    @BindView(R.id.next_play)
    ImageView nextPlay;
    @BindView(R.id.next_body_title)
    TextView title;
    @BindView(R.id.next_body_info)
    TextView info;
    @BindView(R.id.next_button)
    Button next;

    public static NextLessonFragment newInstance() {
        return new NextLessonFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_next_lesson;
    }

    @Override
    public void initView() {
        next.getBackground().setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);
    }

    @OnClick({R.id.next_play, R.id.next_button, R.id.next_close})
    public void onClick(View view) {

        if (view.getId() == R.id.next_close) {
            getActivity().onBackPressed();
        } else {
            Intent i = new Intent(getContext(), RemindActivity.class);
            i.putExtra(RemindActivity.REMIND_TYPE_KEY, RemindActivity.TYPE_HOLD);
            startActivity(i);
        }

    }

}
