package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ef.newlead.R;

import butterknife.BindView;
import butterknife.OnClick;

public class BookResultActivity extends BaseActivity {

    public final static String BOOK_CENTER = "bookCenter";
    public final static String BOOK_DATE = "bookDate";
    public final static String BOOK_TIME = "bookTime";
    public final static String BOOK_NAME = "bookName";

    @BindView(R.id.book_result_info)
    TextView info;
    @BindView(R.id.book_result_address)
    TextView address;
    @BindView(R.id.book_result_date)
    TextView date;
    @BindView(R.id.book_result_time)
    TextView time;
    @BindView(R.id.book_result_button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_book_result;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        Intent intent = getIntent();

        info.setText(getLocaleText("ef_appointment_confirmation_title") + intent.getStringExtra(BOOK_NAME));
        address.setText(intent.getStringExtra(BOOK_CENTER));
        date.setText(intent.getStringExtra(BOOK_DATE));
        time.setText(intent.getStringExtra(BOOK_TIME));
        button.setText(getLocaleText("ef_appointment_confirmation_button"));
    }

    @OnClick(R.id.book_result_button)
    public void onClick() {
        onBackPressed();
    }

}
