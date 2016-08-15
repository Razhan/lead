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
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.location.AMapService;
import com.ef.newlead.location.LocationAppService;
import com.ef.newlead.location.LocationInfo;
import com.ef.newlead.ui.adapter.CityAdapter;
import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.MiscUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by seanzhou on 8/12/16.
 * <p>
 * Fragment provides user the ability to select city or just locate the position automatically.
 */
public class CityLocationFragment extends BaseFragment implements TextWatcher,
        AdapterView.OnItemClickListener, LocationAppService.ResultCallback {

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

    private CityAdapter adapter;
    private LocationAppService locationAppService;

    @Override
    public int bindLayout() {
        return R.layout.activity_location;
    }

    @Override
    public void initView() {
        input.addTextChangedListener(this);
        cancel.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Type type = new TypeToken<List<City>>() {
        }.getType();

        List<City> cities = FileUtils.readObjectFromAssertFile(this.getContext(), "cities.json", type);

        adapter = new CityAdapter(this.getActivity(), cities);
        cityListView.setAdapter(adapter);

        cityListView.setOnItemClickListener(this);

        locationAppService = new AMapService(this.getContext());
        locationAppService.setListener(this);

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

            cancel.setOnClickListener( (View v) -> cancel.startAnimation(animFadein));
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = (String) adapter.getItem(position);
        if (!text.equals(getString(R.string.city_not_found))) {
            input.setText(text);

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
        locationAppService.startLocation();
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
        submit.setVisibility(empty ? View.VISIBLE : View.GONE);

        adapter.setFilter(filter);
    }

    @Override
    public void onStartLocation() {
        location.setEnabled(false);
        location.setText(getString(R.string.city_location) + "  " + getString(R.string.location_ongoing));
    }

    @Override
    public void onStopLocation() {
        location.setEnabled(true);
    }

    @Override
    public void onLocationError(int errorCode, String msg) {
        location.setText(getString(R.string.city_location) + "  " + getString(R.string.location_failure));
        locationAppService.stopLocation();
    }

    @Override
    public void onLocationComplete(LocationInfo location) {
        this.location.setText(getString(R.string.city_location));

        input.setText(location.getCity());
        locationAppService.stopLocation();

        submit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        if (locationAppService != null) {
            locationAppService.dispose();
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
