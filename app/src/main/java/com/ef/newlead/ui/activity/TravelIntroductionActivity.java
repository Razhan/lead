package com.ef.newlead.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.util.SystemText;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;

public class TravelIntroductionActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewNote)
    TextView textViewNote;

    @BindView(R.id.activity_travel_introduction)
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar = true;

        super.onCreate(savedInstanceState);

        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);

        int bkgColor = Color.parseColor("#4e88cd");
        rootLayout.setBackgroundColor(bkgColor);

        textViewTitle.setText(SystemText.getSystemText(this, "travel_introduction_title"));
        textViewNote.setText(SystemText.getSystemText(this, "travel_introduction_body"));
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_travel_introduction;
    }
}
