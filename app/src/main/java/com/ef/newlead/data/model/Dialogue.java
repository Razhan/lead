package com.ef.newlead.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dialogue extends ItemData {

    private String title;

    @SerializedName("title-zh")
    private String translationTitle;

    private VideoBean video;

    private List<UsersBean> users;

    private List<List<DialogsBean>> dialogs;

    public String getTitle() {
        return title;
    }

    public String getTranslationTitle() {
        return translationTitle;
    }

    public VideoBean getVideo() {
        return video;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public List<List<DialogsBean>> getDialogs() {
        return dialogs;
    }

    public static class VideoBean {
        private String media;
        private String thumb;
        private double ratio;

        public String getMedia() {
            return media;
        }

        public String getThumb() {
            return thumb;
        }

        public double getRatio() {
            return ratio;
        }
    }

    public static class UsersBean {
        private String id;
        private String avatar;

        public String getId() {
            return id;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    public static class DialogsBean {
        private String user;
        private double startTime;
        private double endTime;
        private String text;

        @SerializedName("text-zh-cn")
        private String translationText;

        public String getUser() {
            return user;
        }

        public double getStartTime() {
            return startTime;
        }

        public double getEndTime() {
            return endTime;
        }

        public String getText() {
            return text;
        }

        public String getTranslationText() {
            return translationText;
        }
    }
}
