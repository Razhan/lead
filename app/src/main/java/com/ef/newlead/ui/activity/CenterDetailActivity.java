package com.ef.newlead.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.ui.adapter.TelNumberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CenterDetailActivity extends BaseActivity {

    @BindView(R.id.center_detail_pic)
    ImageView pic;
    @BindView(R.id.center_detail_place_text)
    TextView placeText;
    @BindView(R.id.center_detail_place)
    LinearLayout place;
    @BindView(R.id.center_detail_tel_text)
    TextView telText;
    @BindView(R.id.center_detail_tel)
    LinearLayout tel;
    @BindView(R.id.center_detail_time_text)
    TextView timeText;
    @BindView(R.id.center_detail_time)
    LinearLayout time;
    @BindView(R.id.center_detail_bus_text)
    TextView busText;
    @BindView(R.id.center_detail_book)
    Button book;
    @BindView(R.id.center_detail_mark_text)
    TextView markText;
    @BindView(R.id.center_detail_mark)
    ImageView mark;
    @BindView(R.id.center_detail_mark_wrapper)
    RelativeLayout markWrapper;

    private Dialog bottomDialog;

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

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        book.getBackground().setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);
        initBottomDialog();
    }

    private void initBottomDialog() {
        View bottomView = getLayoutInflater().inflate(R.layout.view_bottom_center, null);
        RecyclerView list = (RecyclerView) bottomView.findViewById(R.id.center_tel_list);

        List<String> tels = new ArrayList<>();
        tels.add("1111111");
        tels.add("2222222");

        TelNumberAdapter mAdapter = new TelNumberAdapter(this, tels);

        mAdapter.setClickListener((view, pos, item) -> {
            Uri uri = Uri.parse("tel:" + item);
            Intent i = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(i);
            bottomDialog.hide();
        });

        list.setAdapter(mAdapter);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        bottomDialog = getDialog(bottomView);
    }

    @OnClick({R.id.center_detail_place, R.id.center_detail_tel, R.id.center_detail_time, R.id.center_detail_book, R.id.center_detail_mark_wrapper})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.center_detail_place:
                Uri uri = Uri.parse("geo:22.9621107600,113.9826665700");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                if (isIntentAvailable(i)) {
                    startActivity(i);
                } else {
                    showMessage("没有发现地图应用");
                }
                break;
            case R.id.center_detail_tel:
                if (bottomDialog != null) {
                    bottomDialog.show();
                }
                break;
            case R.id.center_detail_book:
                break;
            case R.id.center_detail_mark_wrapper:
                if (mark.getVisibility() != View.VISIBLE) {
                    mark.setVisibility(View.VISIBLE);
                    markText.setText("MY CENTER");
                    markText.setTextColor(Color.BLACK);

                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rounded_corners_stroke_black, null);
                    markWrapper.setBackground(drawable);
                } else {
                    mark.setVisibility(View.INVISIBLE);
                    markText.setText("MAKE MY CENTER");
                    markText.setTextColor(Color.WHITE);
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rounded_corners_black, null);
                    markWrapper.setBackground(drawable);
                }
                break;
        }
    }
}
