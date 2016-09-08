package com.ef.newlead.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ef.newlead.R;

public class CenterDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_center_detail;
    }

    @Override
    protected int getStatusColor() {
        return Color.BLACK;
    }

    @Override
    protected String setToolBarText() {
        return "徐家汇中心";
    }
}
