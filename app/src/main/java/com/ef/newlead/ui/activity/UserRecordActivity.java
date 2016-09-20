package com.ef.newlead.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.ASRProgressView;
import com.ef.newlead.util.MiscUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRecordActivity extends BaseActivity {

    public final static String KEY_COLORFUL_STRING = "colorfulString";
    public final static String KEY_FULL_STRING = "fullString";

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

//        sentence.setText(MiscUtils.getSpannableText(getIntent().getStringExtra(KEY_FULL_STRING),
//                getIntent().getStringExtra(KEY_COLORFUL_STRING),
//                Color.parseColor("#0078ff")));

        sentence.setText(MiscUtils.getSpannableText("Hello, My name is Sissi.", "Sissi",
                Color.parseColor("#0078ff")));

        indicator.setInitText("Now record your introduction.");
        indicator.post(() -> indicator.show());

    }

    @OnClick(R.id.record_button)
    public void onClick() {
        indicator.setResult(true, "cooooool");
    }


}
