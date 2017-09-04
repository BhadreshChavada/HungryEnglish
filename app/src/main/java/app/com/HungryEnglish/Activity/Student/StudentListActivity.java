package app.com.HungryEnglish.Activity.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.StudentListAdapter;
import app.com.HungryEnglish.Model.RemoveTeacher.RemoveTeacherFromListMainResponse;
import app.com.HungryEnglish.Model.StudentList.StudentData;
import app.com.HungryEnglish.Model.StudentList.StudentListMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by R'jul on 7/14/2017.
 */

public class StudentListActivity extends BaseActivity {

    private RecyclerView recyclerStudentList;
    static List<StudentData> studentList;
    private static StudentListAdapter studentListAdapter;
    public static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mContext = StudentListActivity.this;

        idMapping();

        callStudentListApi();
    }

    private void idMapping() {
        recyclerStudentList = (RecyclerView) findViewById(R.id.recyclerStudentList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminDashboardActivity.class);
        finish();

    }

    // CALL TEACHER LIST API HERE
    private void callStudentListApi() {
        if (!Utils.checkNetwork(StudentListActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentListActivity.this);
            return;
        } else {
            Utils.showDialog(StudentListActivity.this);
            ApiHandler.getApiService().getStudentList(getStudentDetail(), new retrofit.Callback<StudentListMainResponse>() {
                @Override
                public void success(StudentListMainResponse studentListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (studentListMainResponse == null) {
                        Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus() == null) {
                        Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(StudentListActivity.this, "" + studentListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus().equals("true")) {
                        studentList = new ArrayList<StudentData>();
                        studentList = studentListMainResponse.getData();

                        studentListAdapter = new StudentListAdapter(StudentListActivity.this, studentList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(StudentListActivity.this);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerStudentList.setLayoutManager(mLayoutManager);
                        recyclerStudentList.setItemAnimator(new DefaultItemAnimator());
                        recyclerStudentList.setAdapter(studentListAdapter);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getStudentDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "student");
        map.put("status", "1");
        return map;
    }


    //    http://smartsquad.16mb.com/HungryEnglish/api/delete_user.php?id=19&role=student

    // CALL DELETE TEACHER FROM LIST API HERE
    public static void callRemoveStudentFromListApi(int pos, String id, String role) {
        final int position = pos;
        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog("Internet Connection !", mContext.getResources().getString(R.string.internet_connection_error), (Activity) mContext);
            return;
        }
        Utils.showDialog(mContext);
        ApiHandler.getApiService().getRemoveStudentFromList(removeStudentDetail(id, role), new retrofit.Callback<RemoveTeacherFromListMainResponse>() {
            @Override
            public void success(RemoveTeacherFromListMainResponse removeTeacherFromListMainResponse, Response response) {
                Utils.dismissDialog();
                if (removeTeacherFromListMainResponse == null || removeTeacherFromListMainResponse.getStatus() == null) {
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (removeTeacherFromListMainResponse.getStatus().equals("false")) {
                    Toast.makeText(mContext, "" + removeTeacherFromListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (removeTeacherFromListMainResponse.getStatus().equals("true")) {
                    Toast.makeText(mContext, removeTeacherFromListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    studentList.remove(position);
                    studentListAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static Map<String, String> removeStudentDetail(String id, String role) {
        Map<String, String> map = new HashMap<>();
        map.put("role", role);
        map.put("id", id);
        return map;
    }


}
