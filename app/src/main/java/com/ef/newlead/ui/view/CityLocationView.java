package com.ef.newlead.ui.view;

/**
 * Created by seanzhou on 8/15/16.
 */
public interface CityLocationView extends View{
    void onStartLocation(String msg);
    void onStopLocation();
    void onLocationError(String msg);
    void onLocationComplete(String location);
}
