package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.ASRProgressView;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRecordActivity extends BaseActivity {

    @BindView(R.id.record_image)
    ImageView image;
    @BindView(R.id.record_indicator)
    ASRProgressView indicator;
    @BindView(R.id.record_sentence)
    TextView sentence;
    @BindView(R.id.record_button)
    ImageView button;
    @BindView(R.id.record_replay)
    ImageView replay;
    @BindView(R.id.record_next)
    ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        indicator.setInitText("Now record your introduction.");
        indicator.post(() -> indicator.show());

    }

    @OnClick(R.id.record_button)
    public void onClick() {
        indicator.setResult(true, "cooooool");
    }
}
