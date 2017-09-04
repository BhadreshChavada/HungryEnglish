package app.com.HungryEnglish.Model.Profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/18/2017.
 */

public class StudentGetProfileMainResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private GetDataStudentProfile data;
    @SerializedName("info")
    @Expose
    private GetInfoStudentProfile info;

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

    public GetDataStudentProfile getData() {
        return data;
    }

    public void setData(GetDataStudentProfile data) {
        this.data = data;
    }

    public GetInfoStudentProfile getInfo() {
        return info;
    }

    public void setInfo(GetInfoStudentProfile info) {
        this.info = info;
    }
}
