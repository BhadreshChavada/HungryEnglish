package app.com.HungryEnglish.Services;


import java.util.Map;

import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Profile.StudentGetProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.StudentProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.RemoveTeacher.RemoveTeacherFromListMainResponse;
import app.com.HungryEnglish.Model.Report.ReportModel;
import app.com.HungryEnglish.Model.StudentList.StudentListMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherPendingRequestMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.Model.admin.AddInfoResponse;
import app.com.HungryEnglish.Model.admin.CountListMainResponse;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.Model.register.RegisterMainResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;


public interface WebServices {

    @POST("/login.php")
    public void getLogin(@QueryMap Map<String, String> map, Callback<LoginMainResponse> callback);

    @POST("/register.php")
    public void getRegister(@QueryMap Map<String, String> map, Callback<RegisterMainResponse> callback);


    @POST("/change_password.php")
    public void resetPassword(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @POST("/student_profile.php")
    public void getParentProfile(@QueryMap Map<String, String> map, Callback<StudentProfileMainResponse> callback);

    @POST("/getuserbystatus.php")
    public void getTeacherList(@QueryMap Map<String, String> map, Callback<TeacherListMainResponse> callback);

    @POST("/get_count.php")
    public void getCountList(@QueryMap Map<String, String> map, Callback<CountListMainResponse> callback);

    @POST("/delete_user.php")
    public void getRemoveTeacherFromList(@QueryMap Map<String, String> map, Callback<RemoveTeacherFromListMainResponse> callback);

    @POST("/delete_user.php")
    public void getRemoveStudentFromList(@QueryMap Map<String, String> map, Callback<RemoveTeacherFromListMainResponse> callback);

    @POST("/getuserbystatus.php")
    public void getStudentList(@QueryMap Map<String, String> map, Callback<StudentListMainResponse> callback);

    @POST("/updateStatus.php")
    public void acceptTeacherPendingRequest(@QueryMap Map<String, String> map, Callback<TeacherPendingRequestMainResponse> callback);

    @POST("/get_info.php")
    public void getInfo(@QueryMap Map<String, String> map, Callback<InfoMainResponse> callback);

    @Multipart
    @POST("/add_info.php")
    public void addInfo(@QueryMap Map<String, String> map, @Part("image1") TypedFile imageTypeFile, @Part("image2") TypedFile imageTypeFile2, @Part("image3") TypedFile image, Callback<AddInfoResponse> callback);

    @POST("/profile.php")
    public void getTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMain> callback);

    @POST("/profile.php")
    public void getStudentProfile(@QueryMap Map<String, String> map, Callback<StudentGetProfileMainResponse> callback);


    @Multipart
    @POST("/teacher_profile.php")
    public void createTeacherProfile(@QueryMap Map<String, String> map, @PartMap Map<String, TypedFile> Files, Callback<TeacherProfileMainResponse> callback);

    @POST("/teacher_profile.php")
    public void createTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMainResponse> callback);

    @GET("/add_request.php")
    void addRequest(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @GET("/check_user.php")
    void checkUser(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @GET("/report_list.php")
    void getReportList(Callback<ReportModel> callback);
}








