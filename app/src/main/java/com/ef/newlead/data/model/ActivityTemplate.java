package com.ef.newlead.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityTemplate {

    private TemplateType typeNum;

    private String title;

    @SerializedName("title-zh-cn")
    private String translationTitle;

    private VideoBean video;

    private List<UserBean> users;

    private List<List<DialogBean>> dialogs;

    @SerializedName("roleplay")
    private List<List<RolePlayElement>> rolePlayItems;

    public String getTitle() {
        return title;
    }

    public String getTranslationTitle() {
        return translationTitle;
    }

    public VideoBean getVideo() {
        return video;
    }

    public List<UserBean> getUsers() {
        return users;
    }

    public List<List<DialogBean>> getDialogs() {
        return dialogs;
    }

    public List<List<RolePlayElement>> getRolePlayItems() {
        return rolePlayItems;
    }

    public boolean hasRolePlay() {
        return rolePlayItems != null && rolePlayItems.size() > 0;
    }

    public TemplateType getTypeNum() {
        return typeNum;
    }

    public enum TemplateType {
        @SerializedName("1")
        Dialog(1),

        @SerializedName("2")
        RolePlay(2),

        @SerializedName("3")
        Story(3);

        private final int type;

        TemplateType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
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

    public static class UserBean {
        private String id;
        private String avatar;

        public String getId() {
            return id;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    public static class DialogBean implements Parcelable {

        public static final Parcelable.Creator<DialogBean> CREATOR =
                new Parcelable.Creator<DialogBean>() {
                    public DialogBean createFromParcel(Parcel in) {
                        return new DialogBean(in);
                    }

                    public DialogBean[] newArray(int size) {
                        return new DialogBean[size];
                    }
                };
        private String user;
        private double startTime;
        private double endTime;
        private String text;
        @SerializedName("text-zh-cn")
        private String translationText;

        private DialogBean(Parcel in) {
            user = in.readString();
            startTime = in.readDouble();
            endTime = in.readDouble();
            text = in.readString();
            translationText = in.readString();
        }

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

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user);
            dest.writeDouble(startTime);
            dest.writeDouble(endTime);
            dest.writeString(text);
            dest.writeString(translationText);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class RolePlayElement {
        private String id;
        private double startTime;
        private long duration;
        private String sentence;
        private Asr asr;

        public String getId() {
            return id;
        }

        public double getStartTime() {
            return startTime;
        }

        public long getDuration() {
            return duration;
        }

        public String getSentence() {
            return sentence;
        }

        public Asr getAsr() {
            return asr;
        }
    }

    public static class Asr {
        // phonems is for iOS only.
        private List<String> dictionary;

        @SerializedName("keys")
        private String correctPhones;

        public List<String> getDictionary() {
            return dictionary;
        }

        public String getCorrectPhones() {
            return correctPhones;
        }
    }
}
