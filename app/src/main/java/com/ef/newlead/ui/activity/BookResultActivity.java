package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ef.newlead.R;

import butterknife.BindView;
import butterknife.OnClick;

public class BookResultActivity extends BaseActivity {

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

        info.setText("You’re on your way to better English! See you soon Sissita Shen!");
        address.setText("上海市，徐家汇中心");
        date.setText("周六 09月10日");
        time.setText("下午 02:00-03:00");
        button.setText("GOT IT");
    }

    @OnClick(R.id.book_result_button)
    public void onClick() {
    }
}
