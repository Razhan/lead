package com.ef.newlead.ui.fragment.storyTell;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.presenter.CityInfoPresenter;
import com.ef.newlead.ui.activity.PermissionListener;
import com.ef.newlead.ui.activity.StoryTellActivity;
import com.ef.newlead.ui.activity.UserRecordActivity;
import com.ef.newlead.ui.adapter.NewCityAdapter;
import com.ef.newlead.ui.fragment.BaseMVPFragment;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.util.ViewUtils;

import java.util.List;

import butterknife.BindView;

public class TellLocationFragment extends BaseTellFragment<CityInfoPresenter>
        implements CityLocationView, NestedScrollView.OnScrollChangeListener {

    @BindView(R.id.tell_location_input)
    DeletableEditText input;
    @BindView(R.id.tell_location_search)
    TextView location;
    @BindView(R.id.tell_location_list)
    RecyclerView cities;
    @BindView(R.id.tell_location_input_wrapper)
    LinearLayout inputWrapper;

    private List<City> cityList;
    private NewCityAdapter cityAdapter;

    private View headerView;
    private NestedScrollView scrollView;

    public static Fragment newInstance() {
        return new TellLocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String permissions[] = new String[] {Manifest.permission.ACCESS_COARSE_LOCATION};
        askForPermissions(new PermissionListener() {
            @Override
            public void permissionGranted() {

            }

            @Override
            public void permissionDenied() {
                mActivity.showPermissionDeniedView(permissions);
            }
        }, permissions);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_tell_location;
    }

    @Override
    public void initView() {
        super.initView();

        headerView = mActivity.getHeaderView();
        scrollView = mActivity.getNestedScrollView();
        scrollView.setOnScrollChangeListener(this);

        initCityView();

        location.setText(getLocaleText("city_select_locate"));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<City> selected = cityAdapter.setFilter(s.toString());

                if (selected != null) {
                    cityAdapter.set(selected);
                } else {
                    cityAdapter.clear();
                }
            }
        });
    }

    @Override
    protected CityInfoPresenter createPresent() {
        return new CityInfoPresenter(getContext(), this);
    }

    private void initCityView() {
        cityList = presenter.fetchAllCities();

        cityAdapter = new NewCityAdapter(getContext(), cityList);
        cityAdapter.setClickListener((view, pos, item) -> {
            input.setText(item.getFullName());
            startRecordActivity("I am from %s.",
                    item.getPinyin(),
                    "Now try saying it.",
                    "Cool!",
                    R.drawable.bg_story_location);
        });

        this.cities.setLayoutManager(new LinearLayoutManager(getContext()));
        this.cities.setAdapter(cityAdapter);

        this.cities.setOnTouchListener((v, event) -> {
            ViewUtils.hideKeyboard(getActivity());
            return false;
        });

        this.cities.setNestedScrollingEnabled(false);
    }

    @Override
    public void onStartLocation(String msg) {

    }

    @Override
    public void onStopLocation() {

    }

    @Override
    public void onLocationError(String msg) {

    }

    @Override
    public void onLocationComplete(String location) {

    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > headerView.getHeight()) {
            inputWrapper.setTranslationY(scrollY - headerView.getHeight());
        }

        if (scrollY < headerView.getHeight()) {
            inputWrapper.setTranslationY(0);
        }
    }

    @Override
    protected String[] getHeaderText() {
        return new String[] {
                "I am from…",
                "我来自于…",
                "Tell people where you come from."};
    }
}
