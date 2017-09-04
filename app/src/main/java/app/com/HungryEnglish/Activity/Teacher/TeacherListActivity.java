package app.com.HungryEnglish.Activity.Teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Interface.OnRemoveTeacherClickListener;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.R.id.image_teacher_list_header;

/**
 * Created by Rujul on 7/1/2017.
 * TEACHER LIST
 * http://smartsquad.16mb.com/HungryEnglish/api/getuserbystatus.php?role=teacher&status=0
 */

public class TeacherListActivity extends BaseActivity {

    RecyclerView recyclerTearcherList;
    private TeacherListAdapter teacherListAdapter;
    ImageView imgListHeader;
    List<TeacherListResponse> teacherList;
    InfoResponse infoList;
    private int cnt = 0;
    private LinearLayout llLinkList;
    OnRemoveTeacherClickListener onRemoveTeacherClickListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        idMapping();
        if (Utils.ReadSharePrefrence(getApplicationContext(), Constant.SHARED_PREFS.KEY_USER_ROLE).equalsIgnoreCase("student")) {
            callTeacherListApi();
        }
    }


    private void idMapping() {
        recyclerTearcherList = (RecyclerView) findViewById(R.id.recyclerTearcherList);
        imgListHeader = (ImageView) findViewById(image_teacher_list_header);
        llLinkList = (LinearLayout) findViewById(R.id.llLinkList);
    }


    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        } else {
            Utils.showDialog(getApplicationContext());
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {
                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                       toast(teacherListMainResponse.getMessage());
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {

                        teacherList = new ArrayList<TeacherListResponse>();
                        teacherList = teacherListMainResponse.getData();

                        teacherListAdapter = new TeacherListAdapter(getApplicationContext(), teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherListAdapter);

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                   toast(getString(R.string.something_wrong));
                }
            });
        }
    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "teacher");
        map.put("status", "1");
        map.put("optional_status", "2");
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.contact);
        item.setVisible(true);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(getApplicationContext(), Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.ClearSharePrefrence(getApplicationContext());
                startActivity(LoginActivity.class, true);
                finish();
                break;
            case R.id.profile:
                switch (role) {
                    case "student":
                        startActivity(StudentProfileActivity.class);
                        break;
                    case "teacher":
                        startActivity(TeacherProfileActivity.class);
                        break;
                }
                break;
            case R.id.contact:
                startActivity(Contactus.class);
                break;
        }
        return true;
    }
}
