package com.ef.newlead.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.location.AMapService;
import com.ef.newlead.location.LocationAppService;
import com.ef.newlead.location.LocationInfo;
import com.ef.newlead.ui.view.CityLocationView;
import com.ef.newlead.util.FileUtils;
import com.ef.newlead.util.SystemText;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by seanzhou on 8/15/16.
 * <p>
 * Presenter takes care of fetching all city info and city location.
 * </p>
 */
public class CityInfoPresenter extends Presenter<CityLocationView> implements LocationAppService.ResultCallback {
    private final AMapService locationAppService;
    private final Context context;

    public CityInfoPresenter(Context context, CityLocationView view) {
        super(view, null);

        this.context = context;

        locationAppService = new AMapService(context.getApplicationContext());
        locationAppService.setListener(this);
    }

    public List<City> fetchAllCities() {
        Type type = new TypeToken<List<City>>() {
        }.getType();

        List<City> cities = FileUtils.readObjectFromAssertFile(context, "cities.json", type);
        return cities;
    }

    public void locate() {
        locationAppService.startLocation();
    }

    public void stopLocation() {
        locationAppService.stopLocation();
    }

    public void dispose() {
        super.dispose();

        if (locationAppService != null) {
            locationAppService.dispose();
        }

    }


    String getString(int res) {
        return context.getString(res);
    }

    @Override
    public void onStartLocation() {
        String startMsg = SystemText.getSystemText(context, "city_select_locate") + "  " +
                SystemText.getSystemText(context, "city_select_locating");
        getView().onStartLocation(startMsg);
    }

    @Override
    public void onStopLocation() {
        getView().onStopLocation();
    }

    @Override
    public void onLocationError(int errorCode, String msg) {
        String errorMsg = getLocationErrorMsg();

        getView().onLocationError(errorMsg);
        locationAppService.stopLocation();
    }

    @NonNull
    public String getLocationErrorMsg() {
        String errorMsg = SystemText.getSystemText(context, "city_select_locate") + "  " +
        SystemText.getSystemText(context, "city_select_error");
        return errorMsg;
    }

    @Override
    public void onLocationComplete(LocationInfo location) {
        getView().onLocationComplete(location.getCity());
        locationAppService.stopLocation();
    }
}
