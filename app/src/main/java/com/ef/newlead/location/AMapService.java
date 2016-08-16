package com.ef.newlead.location;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import timber.log.Timber;

/**
 * Created by seanzhou on 8/12/16.
 */
public class AMapService implements LocationAppService, AMapLocationListener {
    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private ResultCallback listener;

    public AMapService(Context context) {
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = new AMapLocationClientOption();

        /***
         * 定位模式，目前支持三种定位模式
         * 高精度定位模式： 在这种定位模式下，将同时使用高德网络定位和GPS定位,优先返回精度高的定位
         * 低功耗定位模式： 在这种模式下，将只使用高德网络定位
         * 仅设备定位模式： 在这种模式下，将只使用GPS定位。
         */
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);// Hight_Accuracy

        // 设置定位监听
        locationClient.setLocationListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOCATION_START: //"正在定位..."
                    if (listener != null) {
                        listener.onStartLocation();
                    }
                    break;

                case MSG_LOCATION_STOP: //"定位停止"
                    if (listener != null) {
                        listener.onStopLocation();
                    }
                    break;

                case MSG_LOCATION_FINISH:
                    AMapLocation location = (AMapLocation) msg.obj;
                    if (location != null) {
                        Timber.d(">>> location info: " + location);

                        int errorCode = location.getErrorCode();
                        if (errorCode == 0) {
                            String provider = location.getProvider();
                            double lon = location.getLongitude();
                            double lat = location.getLatitude();
                            String city = location.getCity();

                            if (listener != null) {
                                listener.onLocationComplete(new LocationInfo(city, lat, lon, provider));
                            }
                        } else {
                            if (listener != null) {
                                listener.onLocationError(errorCode, location.getErrorInfo());
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void startLocation() {
        locationClient.setLocationOption(locationOption);

        locationClient.startLocation();
        handler.sendEmptyMessage(MSG_LOCATION_START);
    }

    @Override
    public void stopLocation() {
        locationClient.stopLocation();
        handler.sendEmptyMessage(MSG_LOCATION_STOP);
    }

    @Override
    public void dispose() {
        listener = null;

        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = handler.obtainMessage();
            msg.obj = loc;
            msg.what = MSG_LOCATION_FINISH;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void setListener(ResultCallback callback) {
        listener = callback;
    }
}
