package com.ef.newlead.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.presenter.CityInfoPresenter;
import com.ef.newlead.ui.adapter.CityAdapter;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.util.MiscUtils;
import com.ef.newlead.util.SystemText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by seanzhou on 8/12/16.
 * <p>
 * Fragment provides user the ability to select city or just locate the position automatically.
 */
public class CityLocationFragment extends BaseFragment implements TextWatcher,
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

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    private CityAdapter adapter;
    private CityInfoPresenter cityInfoPresenter;
    private List<City> cities;

    @Override
    public int bindLayout() {
        return R.layout.activity_location;
    }

    @Override
    public void initView() {
        rootLayout.setBackground(getGradientDrawable("city_select_gradient_color"));

        input.addTextChangedListener(this);
        cancel.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = this.getActivity();
        cityInfoPresenter = new CityInfoPresenter(activity, this);

        cities = cityInfoPresenter.fetchAllCities();
        adapter = new CityAdapter(activity, cities);
        cityListView.setAdapter(adapter);

        cityListView.setOnItemClickListener(this);

        submit.setText(SystemText.getSystemText(activity, "purpose_select_next"));
        title.setText(SystemText.getSystemText(activity, "city_select_top_label"));
        input.setHint(SystemText.getSystemText(activity, "city_select_placeholder"));
        location.setText(SystemText.getSystemText(activity, "city_select_locate"));
        submit.setText(SystemText.getSystemText(activity, "city_select_submit"));

        // activate ripple effect on SDK 21+; otherwise apply alpha animation
        if (MiscUtils.hasLollipop()) {
            int rippleColor = 0xFF8755A1;
            ColorStateList colorStateList = ColorStateList.valueOf(rippleColor);
            RippleDrawable rippledImage = new
                    RippleDrawable(colorStateList, getResources().getDrawable(R.drawable.x, getActivity().getTheme()), null);
            cancel.setImageDrawable(rippledImage);
        } else {
            final Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            animFadein.setDuration(200);
            animFadein.setAnimationListener(new Animation.AnimationListener() {
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

            cancel.setOnClickListener((View v) -> cancel.startAnimation(animFadein));
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = (String) adapter.getItem(position);
        if (!text.equals(getString(R.string.city_not_found))) {
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
        cityInfoPresenter.locate();
    }

    @OnClick(R.id.cancel_input)
    void onCancelInput() {
        input.setText("");
    }

    @OnClick(R.id.button)
    void onSubmit() {
        Toast.makeText(getActivity(), "submit button clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // update list
        String filter = s.toString();
        boolean empty = filter.isEmpty();
        cancel.setVisibility(empty ? View.GONE : View.VISIBLE);

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
        location.setText(msg);
    }

    @Override
    public void onLocationComplete(String location) {
        this.location.setText(getString(R.string.city_location));
        input.setText(location);
        submit.setVisibility(View.VISIBLE);

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
            this.location.setText(cityInfoPresenter.getLocationErrorMsg());
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
    public void onDestroy() {
        if (cityInfoPresenter != null) {
            cityInfoPresenter.dispose();
        }
        super.onDestroy();
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
