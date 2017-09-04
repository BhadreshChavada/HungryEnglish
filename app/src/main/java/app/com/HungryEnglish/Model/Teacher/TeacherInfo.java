package app.com.HungryEnglish.Model.Teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/6/2017.
 */

public class TeacherInfo {

    @SerializedName("uId")
    @Expose
    private String uId;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("idImage")
    @Expose
    private String idImage;
    @SerializedName("resume")
    @Expose
    private String resume;
    @SerializedName("audioFile")
    @Expose
    private String audioFile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("available_time")
    @Expose
    private String availableTime;
    @SerializedName("skills")
    @Expose
    private String skills;

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
