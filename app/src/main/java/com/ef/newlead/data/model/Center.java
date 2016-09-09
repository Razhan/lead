package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

public class Center implements Comparable<Center> {

    private int id;

    @SerializedName("city_code")
    private String cityCode;

    @SerializedName("school_code")
    private String schoolCode;

    private int order;

    @SerializedName("school_name_zh_cn")
    private String schoolName;

    @SerializedName("address_zh_cn")
    private String address;

    private String phones;

    @SerializedName("open_time_zh_cn")
    private String openTime;

    @SerializedName("traffic_zh_cn")
    private String traffic;

    private String coordinates;

    public int getId() {
        return id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public int getOrder() {
        return order;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhones() {
        return phones;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getTraffic() {
        return traffic;
    }

    public String getCoordinates() {
        return coordinates;
    }

    @Override
    public int compareTo(Center another) {
        return this.order - another.order;
    }
}
