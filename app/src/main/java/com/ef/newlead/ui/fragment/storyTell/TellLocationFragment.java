package com.ef.newlead.ui.fragment.storyTell;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.presenter.CityInfoPresenter;
import com.ef.newlead.ui.adapter.NewCityAdapter;
import com.ef.newlead.ui.fragment.BaseMVPFragment;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.ui.widget.DeletableEditText;
import com.ef.newlead.util.ViewUtils;

import java.util.List;

import butterknife.BindView;

public class TellLocationFragment extends BaseMVPFragment<CityInfoPresenter>
        implements CityLocationView {

    @BindView(R.id.tell_location_input)
    DeletableEditText input;
    @BindView(R.id.tell_location_search)
    TextView location;
    @BindView(R.id.tell_location_list)
    RecyclerView cities;

    private List<City> cityList;
    private NewCityAdapter cityAdapter;

    public static Fragment newInstance() {
        return new TellLocationFragment();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_tell_location;
    }

    @Override
    public void initView() {
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

}
