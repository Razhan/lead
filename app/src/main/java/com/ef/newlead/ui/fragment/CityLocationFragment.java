package com.ef.newlead.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.Constant;
import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.presenter.CityInfoPresenter;
import com.ef.newlead.ui.adapter.CityAdapter;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.util.MiscUtils;
import com.ef.newlead.util.SharedPreUtils;
import com.ef.newlead.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by seanzhou on 8/12/16.
 * <p>
 * Fragment provides user the ability to select city or just locate the position automatically.
 */
public class CityLocationFragment extends BaseCollectInfoFragment<CityInfoPresenter> implements TextWatcher,
        AdapterView.OnItemClickListener, CityLocationView {

    public static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0xFF;

    @BindView(R.id.cancel_input)
    ImageView cancel;

    @BindView(R.id.editText)
    EditText input;

    @BindView(R.id.city_location)
    TextView location;

    @BindView(R.id.listView)
    ListView cityListView;

    @BindView(R.id.button)
    Button submit;

    @BindView(R.id.textViewCityTitle)
    TextView title;

    private CityAdapter adapter;
    private List<City> cities;

    private int previousInputLength = 0;
    private boolean backSpaceDetected = false;

    @Override
    public int bindLayout() {
        return R.layout.fragment_location;
    }

    @Override
    public void initView() {
        super.initView();
        input.addTextChangedListener(this);
        cancel.setVisibility(View.GONE);

        submit.setOnClickListener((View v) ->
                onSubmit()
        );
    }

    @Override
    protected CityInfoPresenter createPresent() {
        return new CityInfoPresenter(getActivity(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cities = presenter.fetchAllCities();
        adapter = new CityAdapter(getContext(), cities);
        cityListView.setAdapter(adapter);

        cityListView.setOnItemClickListener(this);

        // Hide the keyboard automatically when the user is trying to select the desired city
        cityListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
                    ViewUtils.hideKeyboard(CityLocationFragment.this.getActivity());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        submit.setText(getContinueText());
        title.setText(getLocaleText("city_select_top_label"));
        input.setHint(getLocaleText("city_select_placeholder"));
        location.setText(getLocaleText("city_select_locate"));

        // activate ripple effect on SDK 21+; otherwise apply alpha animation

        if (!MiscUtils.hasLollipop()) {
            final Animation animFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            animFadeIn.setDuration(200);
            animFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onCancelInput();

                    cancel.clearAnimation();
                    cancel.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            cancel.setOnClickListener((v) -> cancel.startAnimation(animFadeIn));
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = (String) adapter.getItem(position);
        if (!text.equals(getLocaleText("city_not_found"))) {
            input.setText(text);

            clearCityList();
            submit.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.city_location)
    void onLocate() {
        tryToLocate();
    }

    private void tryToLocate() {
        FragmentActivity activity = this.getActivity();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, WRITE_COARSE_LOCATION_REQUEST_CODE);
        } else {
            locateNow();
        }
    }

    private void locateNow() {
        presenter.locate();
    }

    @OnClick(R.id.cancel_input)
    void onCancelInput() {
        input.setText("");
    }

    private void onSubmit() {
        if (input.getText().toString().isEmpty()) {
            return;
        }

        SharedPreUtils.putString(Constant.USER_CITY, input.getText().toString());
        startNextFragment();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        previousInputLength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // Check whether user is re-inputting.
        backSpaceDetected = previousInputLength - s.length() > 0;

        String filter = s.toString();

        boolean empty = filter.isEmpty();
        cancel.setVisibility(empty ? View.GONE : View.VISIBLE);

        if (empty || backSpaceDetected) {
            submit.setVisibility(View.GONE);
        }
        adapter.setFilter(filter);
    }

    @Override
    public void onStopLocation() {
        location.setEnabled(true);
    }

    @Override
    public void onStartLocation(String msg) {
        location.setEnabled(false);
        location.setText(msg);
    }

    @Override
    public void onLocationError(String msg) {
        location.setText(getLocaleText("city_select_locate"));
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationComplete(String location) {
        this.location.setText(getLocaleText("city_select_locate"));

        input.setText(location);

        String cityName = location;
        String postfix = getString(R.string.city);
        if (location.endsWith(postfix)) {
            int pos = location.lastIndexOf(postfix);
            cityName = location.substring(0, pos);
        }

        City destCity = null;
        for (City city : cities) {
            if (city.getName().startsWith(cityName)) {
                destCity = city;
                break;
            }
        }

        if (destCity != null) { // matched
            input.setText(destCity.getFullName());
            clearCityList();

        } else {
            // City on the support list will be regarded as location failure.
            onLocationError(presenter.getLocationErrorMsg());
        }
        submit.setVisibility(destCity != null ? View.VISIBLE : View.GONE);
    }

    private void clearCityList() {
        // to clear the city list data
        cityListView.setVisibility(View.INVISIBLE);

        cityListView.postDelayed(() -> {
            adapter.setFilter("");
            cityListView.setVisibility(View.VISIBLE);
        }, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locateNow();
            } else {
                Toast.makeText(this.getActivity(), "Permission for using location has been rejected.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
