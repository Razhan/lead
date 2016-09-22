package com.ef.newlead.data.model.DataBean;


import com.google.gson.annotations.SerializedName;

public class BookInfoBean {

    @SerializedName("center_id")
    private String centerId;
    private String name;
    private String age;
    private String phone;

    @SerializedName("booking_date")
    private String bookingDate;

    @SerializedName("booking_time")
    private String bookingTime;

    public String getCenterId() {
        return centerId;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
    public String getPhone() {
        return phone;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public String getBookingTime() {
        return bookingTime;
    }
}
