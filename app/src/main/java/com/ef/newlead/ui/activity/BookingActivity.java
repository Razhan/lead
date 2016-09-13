package com.ef.newlead.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.adapter.BookItemAdapter;
import com.ef.newlead.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BookingActivity extends BaseActivity {

    @BindView(R.id.book_place_text)
    TextView placeText;
    @BindView(R.id.book_date_text)
    TextView dateText;
    @BindView(R.id.book_clock_text)
    TextView clockText;
    @BindView(R.id.book_user_text)
    TextView userText;
    @BindView(R.id.book_user_age)
    TextView age;
    @BindView(R.id.book_user_phone)
    TextView phone;
    @BindView(R.id.book_date_list)
    RecyclerView dateList;
    @BindView(R.id.book_place)
    LinearLayout bookPlace;
    @BindView(R.id.book_date)
    LinearLayout date;
    @BindView(R.id.book_clock)
    LinearLayout clock;
    @BindView(R.id.book_user)
    LinearLayout user;
    @BindView(R.id.book_clock_list)
    RecyclerView clockList;

    private String defaultDate = "Select the date";
    private String defaultClock = "Select the time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_booking;
    }

    @Override
    protected int getStatusColor() {
        return Color.BLACK;
    }

    @Override
    protected String setToolBarText() {
        return "Book A Session";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        placeText.setText("上海市，徐家汇中心");
        dateText.setText(defaultDate);
        clockText.setText(defaultClock);
        userText.setText("Name");
        age.setText("Age group");
        phone.setText("Phone number");

        initDateList();
        initClockList();

        date.setOnClickListener(v -> {
            if (clockList.getVisibility() == View.VISIBLE) {
                triggerListView(clockList, clockText, defaultClock, false);
            }

            triggerListView(dateList, dateText, defaultDate, false);
        });

        clock.setOnClickListener(v -> {
            if (dateList.getVisibility() == View.VISIBLE) {
                triggerListView(dateList, dateText, defaultDate, false);
            }

            triggerListView(clockList, clockText, defaultClock, false);
        });

        dateList.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(dateList));
        clockList.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(clockList));
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(View view) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getHeight() <= 0) {
                    return;
                }

                int height = view.getHeight() - date.getHeight();
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                view.setLayoutParams(lp);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
    }

    private void initDateList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("今天 09月08日" + String.valueOf(i));
        }

        BookItemAdapter dateAdapter = new BookItemAdapter(this, list);
        dateAdapter.setClickListener((v, pos, item) -> triggerListView(dateList, dateText, item, true));

        dateList.setLayoutManager(new LinearLayoutManager(this));
        dateList.setAdapter(dateAdapter);
    }

    private void initClockList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("上午 10:00" + String.valueOf(i));
        }

        BookItemAdapter clockAdapter = new BookItemAdapter(this, list);
        clockAdapter.setClickListener((v, pos, item) -> triggerListView(clockList, clockText, item, true));

        clockList.setLayoutManager(new LinearLayoutManager(this));
        clockList.setAdapter(clockAdapter);
    }

    private void triggerListView(View view, TextView textView, String text, boolean selected) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

        if (selected) {
            textView.setTextColor(Color.BLACK);
        } else {
            textView.setTextColor(Color.parseColor("#4c000000"));
        }

        textView.setText(text);
    }
}
