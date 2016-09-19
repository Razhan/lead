package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ef.newlead.R;

import butterknife.BindView;
import butterknife.OnClick;

public class RemindActivity extends BaseActivity {

    public static final String REMIND_TYPE_KEY = "remindType";
    public static final int TYPE_LEAVE = 0;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_HOLD = 2;

    @BindView(R.id.remind_title)
    TextView title;
    @BindView(R.id.remind_info)
    TextView info;
    @BindView(R.id.remind_b1)
    Button firstButton;
    @BindView(R.id.find_center_more)
    Button secondButton;
    @BindView(R.id.remind_b3)
    Button thirdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_remind;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        int type = getIntent().getIntExtra(REMIND_TYPE_KEY, 0);

        switch (type) {
            case TYPE_LEAVE:
                initLeaveView();
                break;
            case TYPE_ADD:
                initAddView();
                break;
            case TYPE_HOLD:
                initHoldView();
                break;
        }

        thirdButton.setText("CANCEL");
    }

    private void initLeaveView() {
        title.setText("Wait!");
        info.setText("Are you sure you want to leave? \n" + "Your progress will be lost! \n" + "Complete your info to save it.");
        firstButton.setText("SAVE MY PROGRESS");
        secondButton.setText("LEAVE");

        firstButton.setOnClickListener(v -> {

        });

        secondButton.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }

    private void initAddView() {
        title.setText("Add to Phrasebook");
        info.setText("It’s awesome you want to reference these later in the real world.\n" + "For that you need to complete your info.");
        firstButton.setVisibility(View.INVISIBLE);
        secondButton.setText("ADD TO PHRASEBOOK");

        firstButton.setOnClickListener(v -> {

        });

        secondButton.setOnClickListener(v -> {

        });
    }

    private void initHoldView() {
        title.setText("Hold up!");
        info.setText("Do you want to save your progress first? You can if you complete your info.");
        firstButton.setText("SAVE MY PROGRESS");
        secondButton.setText("CONTINUE");

        firstButton.setOnClickListener(v -> {

        });

        secondButton.setOnClickListener(v -> {

        });
    }

    @OnClick(R.id.remind_b3)
    public void onClick() {
        finish();
    }
}
