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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }

    @OnClick(R.id.home_top_view)
    public void onClick() {
        startActivity(new Intent(this, DialogueVideoActivity.class));
    }

}
