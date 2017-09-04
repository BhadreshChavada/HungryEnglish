package app.com.HungryEnglish.Model.Report;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Bhadresh Chavada on 01-08-2017.
 */

public class Datum implements Parcelable{


    @SerializedName("teacherEmail")
    @Expose
    private String teacherEmail;

    @SerializedName("teacherFullName")
    @Expose
    private String teacherFullName;

    @SerializedName("teacherWeChat")
    @Expose
    private String teacherWeChat;

    @SerializedName("stuEmail")
    @Expose
    private String stuEmail;

    @SerializedName("stuFullName")
    @Expose
    private String stuFullName;

    @SerializedName("stuWeChat")
    @Expose
    private String stuWeChat;

    @SerializedName("stuSex")
    @Expose
    private String stuSex;

    @SerializedName("stuAge")
    @Expose
    private String stuAge;

    @SerializedName("stuStation")
    @Expose
    private String stuStation;

    @SerializedName("stuSkills")
    @Expose
    private String stuSkills;

    @SerializedName("stuTime")
    @Expose
    private String stuTime;

    @SerializedName("tProfileImage")
    @Expose
    private String tProfileImage;

    @SerializedName("tIdImage")
    @Expose
    private String tIdImage;

    @SerializedName("tResume")
    @Expose
    private String tResume;

    @SerializedName("tAudioFile")
    @Expose
    private String tAudioFile;

    @SerializedName("tAddress")
    @Expose
    private String tAddress;

    @SerializedName("tTime")
    @Expose
    private String tTime;

    @SerializedName("tSkills")
    @Expose
    private String tSkills;


    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }


    public String getTeacherFullName() {
        return teacherFullName;
    }

    public void setTeacherFullName(String teacherFullName) {
        this.teacherFullName = teacherFullName;
    }


    public String getTeacherWeChat() {
        return teacherWeChat;
    }

    public void setTeacherWeChat(String teacherWeChat) {
        this.teacherWeChat = teacherWeChat;
    }


    public String getStuEmail() {
        return stuEmail;
    }

    public void setStuEmail(String stuEmail) {
        this.stuEmail = stuEmail;
    }


    public String getStuFullName() {
        return stuFullName;
    }

    public void setStuFullName(String stuFullName) {
        this.stuFullName = stuFullName;
    }


    public String getStuWeChat() {
        return stuWeChat;
    }

    public void setStuWeChat(String stuWeChat) {
        this.stuWeChat = stuWeChat;
    }


    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }


    public String getStuAge() {
        return stuAge;
    }

    public void setStuAge(String stuAge) {
        this.stuAge = stuAge;
    }


    public String getStuStation() {
        return stuStation;
    }

    public void setStuStation(String stuStation) {
        this.stuStation = stuStation;
    }


    public String getStuSkills() {
        return stuSkills;
    }

    public void setStuSkills(String stuSkills) {
        this.stuSkills = stuSkills;
    }


    public String getStuTime() {
        return stuTime;
    }

    public void setStuTime(String stuTime) {
        this.stuTime = stuTime;
    }


    public String getTProfileImage() {
        return tProfileImage;
    }

    public void setTProfileImage(String tProfileImage) {
        this.tProfileImage = tProfileImage;
    }


    public String getTIdImage() {
        return tIdImage;
    }

    public void setTIdImage(String tIdImage) {
        this.tIdImage = tIdImage;
    }


    public String getTResume() {
        return tResume;
    }

    public void setTResume(String tResume) {
        this.tResume = tResume;
    }


    public String getTAudioFile() {
        return tAudioFile;
    }

    public void setTAudioFile(String tAudioFile) {
        this.tAudioFile = tAudioFile;
    }


    public String getTAddress() {
        return tAddress;
    }

    public void setTAddress(String tAddress) {
        this.tAddress = tAddress;
    }


    public String getTTime() {
        return tTime;
    }

    public void setTTime(String tTime) {
        this.tTime = tTime;
    }


    public String getTSkills() {
        return tSkills;
    }

    public void setTSkills(String tSkills) {
        this.tSkills = tSkills;
    }

    protected Datum(Parcel in) {
        teacherEmail = in.readString();
        teacherFullName = in.readString();
        teacherWeChat = in.readString();
        stuEmail = in.readString();
        stuFullName = in.readString();
        stuWeChat = in.readString();
        stuSex = in.readString();
        stuAge = in.readString();
        stuStation = in.readString();
        stuSkills = in.readString();
        stuTime = in.readString();
        tProfileImage = in.readString();
        tIdImage = in.readString();
        tResume = in.readString();
        tAudioFile = in.readString();
        tAddress = in.readString();
        tTime = in.readString();
        tSkills = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teacherEmail);
        dest.writeString(teacherFullName);
        dest.writeString(teacherWeChat);
        dest.writeString(stuEmail);
        dest.writeString(stuFullName);
        dest.writeString(stuWeChat);
        dest.writeString(stuSex);
        dest.writeString(stuAge);
        dest.writeString(stuStation);
        dest.writeString(stuSkills);
        dest.writeString(stuTime);
        dest.writeString(tProfileImage);
        dest.writeString(tIdImage);
        dest.writeString(tResume);
        dest.writeString(tAudioFile);
        dest.writeString(tAddress);
        dest.writeString(tTime);
        dest.writeString(tSkills);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };

}
