package app.com.HungryEnglish.Activity.Admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentListActivity;
import app.com.HungryEnglish.Model.admin.CountListMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AdminDashboardActivity extends BaseActivity {

    RelativeLayout llStudentList, llTeacherList, llAddImageOrLink, llreport;

    AlphaAnimation click;
    int teacherCount = 0, studentCount = 0;
    private TextView tvTeacherCountAdmin, tvStudentCountAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click = new AlphaAnimation(1F, 0.5F);
        setContentView(R.layout.admin_dashboard_activity);
        idMapping();

        setOnClick();

        callGetCountApi();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(AdminDashboardActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                final Dialog dialog = new Dialog(AdminDashboardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_logout);
                dialog.setCancelable(false);
                TextView tvLogout = (TextView) dialog.findViewById(R.id.btLogoutPopupLogout);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.btCancelPopupLogout);
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        write(Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "0");
                        startActivity(LoginActivity.class, true);
                        finish();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
        return true;
    }

    private void idMapping() {
        llStudentList = (RelativeLayout) findViewById(R.id.llStudentList);
        llTeacherList = (RelativeLayout) findViewById(R.id.llTeacherList);
        llAddImageOrLink = (RelativeLayout) findViewById(R.id.llAddImageOrLink);
        llreport = (RelativeLayout) findViewById(R.id.llreport);
        tvTeacherCountAdmin = (TextView) findViewById(R.id.tvTeacherCountAdmin);
        tvStudentCountAdmin = (TextView) findViewById(R.id.tvStudentCountAdmin);
    }

    private void setOnClick() {
        llStudentList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StudentListActivity.class);
            }
        });
        llTeacherList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdminTeacherList.class);
            }
        });
        llAddImageOrLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddImageOrLinkActivity.class);
            }
        });
        llreport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(ReportActivity.class);
            }
        });

    }


    // CALL GET COUNT API HERE
    private void callGetCountApi() {
        if (!Utils.checkNetwork(AdminDashboardActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AdminDashboardActivity.this);
            return;
        } else {
            Utils.showDialog(AdminDashboardActivity.this);
            ApiHandler.getApiService().getCountList(getTeacherDetail(), new retrofit.Callback<CountListMainResponse>() {
                @Override
                public void success(CountListMainResponse countListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (countListMainResponse == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus() == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("false")) {
                        toast(countListMainResponse.getMsg());
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("true")) {
                        teacherCount = Integer.parseInt(countListMainResponse.getTeacherCount());
                        studentCount = Integer.parseInt(countListMainResponse.getStudentCount());
                        tvTeacherCountAdmin.setText(String.valueOf(teacherCount));
                        tvStudentCountAdmin.setText(String.valueOf(studentCount));
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Utils.dismissDialog();
                    Toast.makeText(AdminDashboardActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


}
