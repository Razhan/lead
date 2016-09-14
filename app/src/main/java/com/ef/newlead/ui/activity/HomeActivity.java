package com.ef.newlead.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.ef.newlead.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.OnClick;
import timber.log.Timber;

public class HomeActivity extends BaseActivity {

    public static final int CODE_AGE = 0xFF;
    public static final int CODE_NAME = CODE_AGE + 1;
    public static final int CODE_PHONE = CODE_NAME + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);

        types.addAll(
                Arrays.asList(
                        PersonalInfoPickerActivity.TYPE_AGE,
                        PersonalInfoPickerActivity.TYPE_NAME,
                        PersonalInfoPickerActivity.TYPE_PHONE)
        );
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }


    private ArrayList<Integer> types = new ArrayList<>();

    @OnClick(R.id.home_top_view)
    public void onClick() {
        startActivity(new Intent(this, DialogueVideoActivity.class));
    }

    private int index = 0;

    @OnClick(R.id.home_image)
    public void onTest() {
        Intent intent = new Intent(this, PersonalInfoPickerActivity.class);


        if (index == 3) {
            index = 0;
        }

        intent.putExtra("type", types.get(index));

        int reqCode = CODE_AGE;
        if (index == PersonalInfoPickerActivity.TYPE_NAME) {
            reqCode = CODE_NAME;
        } else if (index == PersonalInfoPickerActivity.TYPE_PHONE) {
            reqCode = CODE_PHONE;
        }

        ++index;

        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String msg = "";

        Timber.d(">>> Activity result: reqCode %d | result code: %d | data: %s", requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_AGE:
                    msg = data.getStringExtra("age");
                    break;
                case CODE_NAME:
                    msg = data.getStringExtra("name");
                    break;
                case CODE_PHONE:
                    msg = data.getStringExtra("phone");
                    break;
            }

            Timber.d(">>> msg returned: %s", msg);

            if (!TextUtils.isEmpty(msg))
                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
