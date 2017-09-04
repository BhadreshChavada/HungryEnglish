package app.com.HungryEnglish.Model.Teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherProfileMain {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private TeacherBasicInfo teacherBasicInfo;
    @SerializedName("info")
    @Expose
    private TeacherInfo teacherInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TeacherBasicInfo getData() {
        return teacherBasicInfo;
    }

    public void setData(TeacherBasicInfo data) {
        this.teacherBasicInfo = data;
    }

    public TeacherInfo getInfo() {
        return teacherInfo;
    }

    public void setInfo(TeacherInfo info) {
        this.teacherInfo = info;
    }

}