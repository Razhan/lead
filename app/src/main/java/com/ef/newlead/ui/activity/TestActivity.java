package com.ef.newlead.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.ef.newlead.R;
import com.ef.newlead.ui.widget.ColorfulProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ColorfulProgressBar progressBar;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        progressBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float[] array = {1f, 2f, 3f, 5f, 8f, 9f};
                progressBar.setDotsPosition(10f, array);
                setProgress();

                progressBar.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

    }

    private void setProgress() {
        Handler handler = new Handler();

        for (int i = 0; i < 1001; i++) {
            final int j = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(j * 0.001f);
                }
            }, j * 10);
        }
    }

    @OnClick(R.id.button)
    public void onClick() {
        progressBar.setThumb(!progressBar.isShowThumb());
    }
}
