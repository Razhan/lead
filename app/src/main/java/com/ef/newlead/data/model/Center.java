package com.ef.newlead.data.model;

import android.text.TextUtils;

import com.ef.newlead.domain.location.GeoPosition;
import com.ef.newlead.util.Assert;
import com.google.android.exoplayer.metadata.id3.PrivFrame;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Center implements Comparable<Center>, Serializable {

    private int id;

    @SerializedName("city_code")
    private String cityCode;

    @SerializedName("school_code")
    private String schoolCode;

    private int order;

    @SerializedName("school_name-zh-cn")
    private String schoolName;

    @SerializedName("address-zh-cn")
    private String address;

    private String phones;

    @SerializedName("open_time-zh-cn")
    private String openTime;

    @SerializedName("traffic-zh-cn")
    private String traffic;

    private String coordinates;

    private BookInfo bookInfo;

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

    public GeoPosition getGeoPosition() {
        String errorMsg = "Invalid geo location center value found";
        String sep = ",";
        Assert.checkArgument(!TextUtils.isEmpty(coordinates) && coordinates.contains(sep), errorMsg);

        String[] pos = coordinates.split(sep);
        Assert.checkArgument(pos != null && pos.length == 2, errorMsg);
        return new GeoPosition(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    @Override
    public int compareTo(Center another) {
        return this.order - another.order;
    }

    public static class BookInfo {
        private String date;
        private String time;

        public  BookInfo(String date, String time) {
            this.date = date;
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }
}
