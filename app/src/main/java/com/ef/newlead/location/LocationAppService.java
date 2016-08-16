package com.ef.newlead.location;

/**
 * Created by seanzhou on 8/12/16.
 */
public interface LocationAppService {
    void startLocation();

    void stopLocation();

    void dispose();

    /***
     * Set the listener for observing the location result. Currently only one listener will be accepted.
     *
     * @param callback
     */
    void setListener(ResultCallback callback);

    interface ResultCallback {
        void onStartLocation();

        void onStopLocation();

        void onLocationError(int errorCode, String msg);

        void onLocationComplete(LocationInfo location);
    }

}
