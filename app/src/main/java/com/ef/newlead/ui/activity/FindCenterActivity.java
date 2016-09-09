package com.ef.newlead.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import com.ef.newlead.R;
import com.ef.newlead.data.model.Center;
import com.ef.newlead.data.model.City;
import com.ef.newlead.ui.adapter.CenterAdapter;
import com.ef.newlead.ui.adapter.NewCityAdapter;
import com.ef.newlead.ui.adapter.SummaryDialogueAdapter;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.MiscUtils;
import com.ef.newlead.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindCenterActivity extends BaseActivity {

    @BindView(R.id.find_center_input)
    DeletableEditText input;
    @BindView(R.id.find_center_locate)
    TextView locate;
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

    private List<View> resLayout;
    private CenterAdapter centerAdapter;
    private NewCityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colorfulStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_find_center;
    }

    @Override
    protected int getStatusColor() {
        return Color.BLACK;
    }

    @Override
    protected String setToolBarText() {
        return "Find EF center";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        input.setHint("城市名、拼音");
        locate.setText("定位城市");

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
        Type type = new TypeToken<List<Center>>() {
        }.getType();

        List<Center> centerList = FileUtils.readObjectFromAssertFile(this, "centers.json", type);

        centerAdapter = new CenterAdapter(this, centerList);
        centerAdapter.setClickListener((view, pos, item) ->{
            Intent i = new Intent(this, CenterDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CenterDetailActivity.SELECTED_CENTER, item);
            i.putExtras(bundle);

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
        Type type = new TypeToken<List<City>>() {
        }.getType();

        List<City> cityList = FileUtils.readObjectFromAssertFile(this, "cities.json", type);

        cityAdapter = new NewCityAdapter(this, cityList);
        cityAdapter.setClickListener((view, pos, item) -> {
            ViewUtils.hideKeyboard(this);

            List<Center> list = centerAdapter.getCenterInCity(item.getCode());

            if (list == null || list.size() < 1) {
                showResultView(empty);
            } else {
                centerAdapter.set(list);
                showResultView(centers);
            }
        });

        cities.setLayoutManager(new LinearLayoutManager(this));
        cities.setAdapter(cityAdapter);
        cities.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(this);
            return false;
        });
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

}
