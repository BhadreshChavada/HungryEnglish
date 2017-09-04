package app.com.HungryEnglish.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button registerBtn, loginBtn;
    private Context context;
    private EditText emailEdt, passwordEdt;
    private Utils utils;
    private AlphaAnimation click;
    private TextView forgotPasswordTxt;
    private String Token = "ABC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        click = new AlphaAnimation(1F, 0.5F);
        utils = new Utils(context);
        idMapping();
    }

    private void idMapping() {
        registerBtn = (Button) findViewById(R.id.login_register);
        loginBtn = (Button) findViewById(R.id.activity_login_btn);
        emailEdt = (EditText) findViewById(R.id.activity_login_email);
        passwordEdt = (EditText) findViewById(R.id.activity_login_password);
        forgotPasswordTxt = (TextView) findViewById(R.id.txt_forgot_password);

        setTitle(getString(R.string.sign_in));

        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        forgotPasswordTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.activity_login_btn:

                if (emailEdt.getText().toString().equalsIgnoreCase(getString(R.string.null_value))) {
                    emailEdt.setError(getString(R.string.email_validation));
                    emailEdt.requestFocus();
                    return;
                }
                if (passwordEdt.getText().toString().equalsIgnoreCase(getString(R.string.null_value))) {
                    passwordEdt.setError(getString(R.string.password_validation));
                    passwordEdt.requestFocus();
                    return;
                }
                if (passwordEdt.getText().toString().trim().length() < 6) {
                    passwordEdt.setError(getString(R.string.password_validation));
                    passwordEdt.requestFocus();
                    return;
                }
                if (Token == null) {
                    toast(getString(R.string.try_again));
                    return;
                }
                callLoginApi();
                break;

            case R.id.txt_forgot_password:
                startActivity(ForgotPassword.class);
        }

    }


    // CALL LOGIN API HERE
    private void callLoginApi() {

        if (!Utils.checkNetwork(context)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(context);
        ApiHandler.getApiService().getLogin(getLoginDetail(), new retrofit.Callback<LoginMainResponse>() {
            @Override
            public void success(LoginMainResponse loginUser, Response response) {
                Utils.dismissDialog();
                if (loginUser == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (loginUser.getStatus() == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (loginUser.getStatus().equals("false")) {
                    toast(loginUser.getMsg());
                    return;
                }
                if (loginUser.getStatus().equals("true")) {

                    String role = loginUser.getData().getRole();

                    String isActiveStatue = loginUser.getData().getIsActive();
               
                    Utils.WriteSharePrefrence(context, Constant.SHARED_PREFS.KEY_USER_ID, loginUser.getData().getId());
                    Utils.WriteSharePrefrence(context, Constant.SHARED_PREFS.KEY_USER_ROLE, role);
                    Utils.WriteSharePrefrence(context, Constant.SHARED_PREFS.KEY_USER_NAME, loginUser.getData().getUsername());
                    Utils.WriteSharePrefrence(context, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "1");
                    Utils.WriteSharePrefrence(context, Constant.SHARED_PREFS.KEY_IS_ACTIVE, isActiveStatue);

                    if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {
                        startActivity(StudentProfileActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity(TeacherListActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {

                        toast(getString(R.string.admin_approve_request));

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity(MainActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                        startActivity(MainActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("admin")) {
                        startActivity(AdminDashboardActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                toast(getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("u_pass", "" + passwordEdt.getText().toString());
        map.put("u_name", "" + emailEdt.getText());
        map.put("device_id", Token);

        return map;
    }
}
