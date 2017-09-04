package app.com.HungryEnglish.Model.StudentList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/14/2017.
 */

public class StudentInfo {

    @SerializedName("studentId")
    @Expose
    private String studentId;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("station")
    @Expose
    private String station;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("skills")
    @Expose
    private String skills;
    @SerializedName("available_time")
    @Expose
    private String availableTime;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

}
