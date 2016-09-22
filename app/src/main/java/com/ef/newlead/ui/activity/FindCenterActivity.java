package com.ef.newlead.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.R;
import com.ef.newlead.data.model.Center;
import com.ef.newlead.data.model.City;
import com.ef.newlead.presenter.CityInfoPresenter;
import com.ef.newlead.ui.adapter.CenterAdapter;
import com.ef.newlead.ui.adapter.NewCityAdapter;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.ui.widget.OnClickListenerWrapper;
import com.ef.newlead.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindCenterActivity extends BaseMVPActivity<CityInfoPresenter>
        implements CityLocationView {

    @BindView(R.id.find_center_input)
    DeletableEditText input;

    @BindView(R.id.city_location)
    TextView location;

    @BindView(R.id.find_center_res_start)
    LinearLayout start;

    @BindView(R.id.find_center_more)
    Button more;

    @BindView(R.id.find_center_res_empty)
    RelativeLayout empty;

    @BindView(R.id.find_center_res_centers)
    RecyclerView centers;

    @BindView(R.id.find_center_res_cities)
    RecyclerView cities;

    @BindView(R.id.find_center_res)
    FrameLayout result;

    @BindView(R.id.find_center_res_empty_title)
    TextView emptyTitle;

    @BindView(R.id.find_center_res_empty_info)
    TextView emptyInfo;

    @BindView(R.id.find_center_res_start_text)
    TextView startText;

    private List<View> resLayout;
    private CenterAdapter centerAdapter;
    private NewCityAdapter cityAdapter;
    private List<City> cityList;
    private City selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_find_center;
    }

    @NonNull
    @Override
    protected CityInfoPresenter createPresenter() {
        return new CityInfoPresenter(this, this);
    }

    @Override
    protected int getStatusColor() {
        return Color.BLACK;
    }

    @Override
    protected String setToolBarText() {
        return getLocaleText("ef_center_title");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        input.setHint(getLocaleText("city_select_placeholder"));
        location.setText(getLocaleText("city_select_locate"));
        location.setOnClickListener(new OnClickListenerWrapper((View v) -> tryToLocate()));

        startText.setText(getLocaleText("ef_center_help_hint"));
        emptyTitle.setText(getLocaleText("ef_center_no_center_title"));
        emptyInfo.setText(getLocaleText("ef_center_no_center_detail"));
        more.setText(getLocaleText("ef_center_learn_more_button"));

        initCityView();
        initCenterView();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    showResultView(start);
                    return;
                }

                List<City> selected = cityAdapter.setFilter(s.toString());

                if (selected == null || selected.size() < 1) {
                    showResultView(empty);
                } else {
                    cityAdapter.set(selected);
                    showResultView(cities);
                }
            }
        });
        input.setListener(() -> showResultView(start));

        start.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });

        empty.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });

        showResultView(start);
    }

    private void initCenterView() {
        List<Center> centerList = presenter.fetchAllCenters();

        centerAdapter = new CenterAdapter(this, centerList);
        centerAdapter.setClickListener((view, pos, item) -> {
            Intent i = new Intent(this, CenterDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CenterDetailActivity.SELECTED_CENTER, item);
            i.putExtras(bundle);
            i.putExtra(BookActivity.KEY_CENTER_ADDRESS, selectedCity.getName());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View sharedView = view.findViewById(R.id.center_pic);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, "shared_pic");
                startActivity(i, options.toBundle());
            } else {
                startActivity(i);
            }
        });

        centers.setLayoutManager(new LinearLayoutManager(this));
        centers.setAdapter(centerAdapter);
    }

    private void initCityView() {
        cityList = presenter.fetchAllCities();

        cityAdapter = new NewCityAdapter(this, cityList);
        cityAdapter.setClickListener((view, pos, item) -> {
            input.setText(item.getFullName());
            selectedCity = item;
            inflateCentersByCity(item);
        });

        this.cities.setLayoutManager(new LinearLayoutManager(this));
        this.cities.setAdapter(cityAdapter);
        this.cities.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });
    }

    private void inflateCentersByCity(City item) {
        ViewUtils.hideKeyboard(this);

        List<Center> list = centerAdapter.getCenterInCity(item.getCode());

        if (list == null || list.size() < 1) {
            showResultView(empty);
        } else {
            centerAdapter.set(list);
            showResultView(centers);
        }
    }

    private void showResultView(View view) {
        if (resLayout == null || resLayout.size() < 4) {
            resLayout = new ArrayList<>();
            resLayout.add(start);
            resLayout.add(empty);
            resLayout.add(cities);
            resLayout.add(centers);
        }

        for (View child : resLayout) {
            if (child.equals(view)) {
                child.setVisibility(View.VISIBLE);
            } else {
                if (child.getVisibility() == View.VISIBLE) {
                    child.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @OnClick(R.id.find_center_more)
    public void onClick() {

    }

    private void tryToLocate() {
        askForPermissions(new PermissionListener() {
            @Override
            public void permissionGranted() {
                locateNow();
            }

            @Override
            public void permissionDenied() {
                Toast.makeText(FindCenterActivity.this,
                        "Permission for using location has been rejected.", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void locateNow() {
        presenter.locate();
    }

    @Override
    public void onStartLocation(String msg) {
        location.setEnabled(false);
        location.setText(msg);
    }

    @Override
    public void onStopLocation() {
        location.setEnabled(true);
    }

    @Override
    public void onLocationError(String msg) {
        location.setText(getLocaleText("city_select_locate"));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationComplete(String location) {
        this.location.setEnabled(true);
        this.location.setText(getLocaleText("city_select_locate"));

        input.setText(location);

        String cityName = location;
        String postfix = getString(R.string.city);
        if (location.endsWith(postfix)) {
            int pos = location.lastIndexOf(postfix);
            cityName = location.substring(0, pos);
        }

        City destCity = null;
        for (City city : cityList) {
            if (city.getName().startsWith(cityName)) {
                destCity = city;
                break;
            }
        }

        if (destCity != null) { // matched
            input.setText(destCity.getFullName());
            inflateCentersByCity(destCity);
        } else {
            showResultView(empty);
        }
    }

}
