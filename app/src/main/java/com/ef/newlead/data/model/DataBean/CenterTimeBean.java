package com.ef.newlead.data.model.DataBean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CenterTimeBean {

    @SerializedName("center_id")
    private String centerId;

    @SerializedName("available_time")
    private List<AvailableTimeBean> availableTime;

    public String getCenterId() {
        return centerId;
    }

    public List<AvailableTimeBean> getAvailableTime() {
        return availableTime;
    }

    public static class AvailableTimeBean {

        private String date;

        @SerializedName("timeslots")
        private List<TimeSlotBean> timeSlots;

        public String getDate() {
            return date;
        }

        public List<TimeSlotBean> getTimeSlots() {
            return timeSlots;
        }

        public static class TimeSlotBean {
            private String time;

            public String getTime() {
                return time;
            }
        }
    }
}
