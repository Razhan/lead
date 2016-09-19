package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.ui.adapter.BookItemAdapter;
import com.ef.newlead.util.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BookActivity extends BaseActivity {

    public static final int CODE_AGE = 0xFF;
    public static final int CODE_NAME = CODE_AGE + 1;
    public static final int CODE_PHONE = CODE_NAME + 1;

    private static final int DEFAULT_STEP = 3;
    private int currentStep = 0;

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
    @BindView(R.id.book_button)
    Button book;

    private String defaultDateText = "Select the date";
    private String defaultClockText = "Select the time";
    private String defaultNameText = "Complete information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_book;
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

        book.getBackground().setColorFilter(Color.parseColor("#B3D8FD"), PorterDuff.Mode.MULTIPLY);

        placeText.setText("上海市，徐家汇中心");
        dateText.setText(defaultDateText);
        clockText.setText(defaultClockText);
        userText.setText(defaultNameText);

        age.setText(SharedPreUtils.getString(Constant.USER_AGE_VALUE, "Age group"));
        phone.setText(SharedPreUtils.getString(Constant.USER_PHONE, "Phone number"));
        book.setText("BOOK SESSION");

        initDateList();
        initClockList();

        date.setOnClickListener(v -> {
            if (clockList.getVisibility() == View.VISIBLE) {
                triggerListView(clockList, clockText, defaultClockText, false);
            }

            triggerListView(dateList, dateText, defaultDateText, false);
        });

        clock.setOnClickListener(v -> {
            if (dateText.getText().toString().equals(defaultDateText)) {
                return;
            }

            if (dateList.getVisibility() == View.VISIBLE) {
                triggerListView(dateList, dateText, defaultDateText, false);
            }

            triggerListView(clockList, clockText, defaultClockText, false);
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

        setButton();
    }

    @OnClick({R.id.book_user_text, R.id.book_user_age, R.id.book_user_phone, R.id.book_button})
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.book_user_text:
                intent = new Intent(this, BookInfoActivity.class);
                intent.putExtra("type", BookInfoActivity.TYPE_NAME);

                startActivityForResult(intent, CODE_NAME);
                break;
            case R.id.book_user_age:
                intent = new Intent(this, BookInfoActivity.class);
                intent.putExtra("type", BookInfoActivity.TYPE_AGE);

                startActivityForResult(intent, CODE_AGE);
                break;
            case R.id.book_user_phone:
                intent = new Intent(this, BookInfoActivity.class);
                intent.putExtra("type", BookInfoActivity.TYPE_PHONE);

                startActivityForResult(intent, CODE_PHONE);
                break;
            case R.id.book_button:
                if (currentStep >= DEFAULT_STEP
                        && !dateText.getText().toString().equals(defaultDateText)
                        && !clockText.getText().toString().equals(defaultClockText)) {
                    intent = new Intent(this, BookResultActivity.class);
                    intent.putExtra(BookResultActivity.BOOK_CENTER, placeText.getText());
                    intent.putExtra(BookResultActivity.BOOK_DATE, dateText.getText());
                    intent.putExtra(BookResultActivity.BOOK_TIME, clockText.getText());

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_AGE:
                    renderResult(data, age, "age");
                    break;
                case CODE_NAME:
                    renderResult(data, userText, "name");
                    break;
                case CODE_PHONE:
                    renderResult(data, phone, "phone");
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void renderResult(Intent data, TextView view, String key) {
        view.setText(data.getStringExtra(key));
        view.setTextColor(Color.BLACK);

        if (view.getTag() == null) {
            currentStep++;
            setButton();
            view.setTag("checked");
        }
    }

    private void setButton() {
        if (currentStep == DEFAULT_STEP
                && !dateText.getText().toString().equals(defaultDateText)
                && !clockText.getText().toString().equals(defaultClockText)) {
            Log.d("setButton", "setButton");
            book.getBackground().setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);
        } else {
            book.getBackground().setColorFilter(Color.parseColor("#B3D8FD"), PorterDuff.Mode.MULTIPLY);
        }
    }

}
