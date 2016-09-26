package com.ef.newlead.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.Center;
import com.ef.newlead.data.model.DataBean.CenterTimeBean;
import com.ef.newlead.domain.location.GeoPosition;
import com.ef.newlead.presenter.CenterPresenter;
import com.ef.newlead.ui.adapter.TelNumberAdapter;
import com.ef.newlead.ui.view.CenterBookView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class CenterDetailActivity extends BaseMVPActivity<CenterPresenter> implements CenterBookView {

    public final static String SELECTED_CENTER = "selectedCenter";

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
    @BindView(R.id.center_detail_star)
    ImageView star;
    @BindView(R.id.center_detail_booked_date)
    TextView bookedDate;
    @BindView(R.id.center_detail_booked_time)
    TextView bookedTime;
    @BindView(R.id.center_detail_booked_wrapper)
    RelativeLayout bookedWrapper;

    private Dialog bottomDialog;
    private Center mCenter;
    private String mCity;
    private boolean starred = false;
    private GeoPosition geoPosition;
    private boolean isBooked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);

        presenter.getBookInfo(String.valueOf(mCenter.getId()));
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
        return mCenter.getSchoolName();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initData();
        super.initView(savedInstanceState);

        placeText.setText(mCenter.getAddress());
        telText.setText(mCenter.getPhones());
        timeText.setText(mCenter.getOpenTime());
        busText.setText(mCenter.getTraffic());

        geoPosition = mCenter.getGeoPosition();

        if (mCenter.getPhones().split(", ").length > 1) {
            initBottomDialog();
        }
    }

    private void initData() {
        mCenter = (Center) getIntent().getExtras().getSerializable(SELECTED_CENTER);
        mCity = getIntent().getStringExtra(BookActivity.KEY_CENTER_ADDRESS);
    }

    @NonNull
    @Override
    protected CenterPresenter createPresenter() {
        return new CenterPresenter(this, this);
    }

    private void initBottomDialog() {
        View bottomView = getLayoutInflater().inflate(R.layout.view_bottom_center, null);
        RecyclerView list = (RecyclerView) bottomView.findViewById(R.id.center_tel_list);

        String[] tels = mCenter.getPhones().split(", ");
        TelNumberAdapter mAdapter = new TelNumberAdapter(this, Arrays.asList(tels));

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

    @Override
    protected void onResume() {
        super.onResume();

        presenter.checkCenterBooked(String.valueOf(mCenter.getId()));
    }

    @Override
    public void hasBooked(boolean isBooked, Center.BookInfo bookInfo) {
        this.isBooked = isBooked;

        if (isBooked) {
            bookedDate.setText(bookInfo.getDate());
            bookedTime.setText(bookInfo.getTime());
            bookedWrapper.animate().alpha(1).setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();

            book.setBackgroundResource(R.drawable.bg_rounded_corners_stroke_black);
            book.setText(getLocaleText("ef_center_book_cancel"));
            book.setTextColor(Color.BLACK);
        } else {
            bookedWrapper.animate().alpha(0).setDuration(Constant.DEFAULT_ANIM_FULL_TIME).start();

            book.setBackgroundResource(R.drawable.bg_rounded_corners_white);
            book.getBackground().setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);

            book.setText(getLocaleText("ef_center_book_action"));
            book.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void afterGetCenterTime(CenterTimeBean times) {

    }

    @Override
    public void afterBook() {

    }

    @OnClick({R.id.center_detail_place, R.id.center_detail_tel, R.id.center_detail_time, R.id.center_detail_book})
    public void onClick(View view) {
        Uri uri;
        Intent i;
        switch (view.getId()) {
            case R.id.center_detail_place:
                String formatGeoPosition = String.format("geo:%s,%s", geoPosition.getLatitude(), geoPosition.getLongitude());
                Timber.d(">>> Center location %s", formatGeoPosition);

                uri = Uri.parse(formatGeoPosition);
                i = new Intent(Intent.ACTION_VIEW, uri);
                if (isIntentAvailable(i)) {
                    startActivity(i);
                } else {
//                    showMessage("没有发现地图应用");
                }
                break;
            case R.id.center_detail_tel:
                if (bottomDialog != null) {
                    bottomDialog.show();
                } else {
                    uri = Uri.parse("tel:" + mCenter.getPhones());
                    i = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(i);
                }
                break;
            case R.id.center_detail_book:
                if (isBooked) {
                    presenter.cancelBook(String.valueOf(mCenter.getId()));
                } else {
                    i = new Intent(this, BookActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CenterDetailActivity.SELECTED_CENTER, mCenter);
                    i.putExtras(bundle);
                    i.putExtra(BookActivity.KEY_CENTER_ADDRESS, mCity + "，" + mCenter.getSchoolName());

                    startActivity(i);
                }
                break;
        }
    }

    @OnClick(R.id.center_detail_star)
    public void onClick() {
        if (starred) {
            star.setImageResource(R.drawable.ic_star_empty);
        } else {
            star.setImageResource(R.drawable.ic_star_fill);
        }

        starred = !starred;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bottomDialog != null) {
            bottomDialog.dismiss();
        }
    }
}
