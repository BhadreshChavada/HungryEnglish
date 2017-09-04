package app.com.HungryEnglish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Model.register.RegisterMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Constant.ROLE_STUDENT;
import static app.com.HungryEnglish.Util.Constant.ROLE_TEACHER;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button loginBtn, signupBtn;
    EditText fullNameEdt, usernameEdt, passwordEdt, emailEdt, mobileEdt;
    AlphaAnimation click;
    CheckBox isTeacherChBox;
    boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        click = new AlphaAnimation(1F, 0.5F);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Sign up");
        idMapping();
    }

    private void idMapping() {

        loginBtn = (Button) findViewById(R.id.register_login);

        signupBtn = (Button) findViewById(R.id.activity_register_btn);

        fullNameEdt = (EditText) findViewById(R.id.activity_register_fullname);

        usernameEdt = (EditText) findViewById(R.id.activity_register_username);

        passwordEdt = (EditText) findViewById(R.id.activity_register_password);

        emailEdt = (EditText) findViewById(R.id.activity_register_email);

        mobileEdt = (EditText) findViewById(R.id.activity_register_mobile_number);

        isTeacherChBox = (CheckBox) findViewById(R.id.activity_register_is_teacher);

        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_login:
                v.startAnimation(click);

                startActivity(LoginActivity.class);

                this.finish();

                break;


            case R.id.activity_register_btn:

                v.startAnimation(click);

                if (fullNameEdt.getText().toString().equals("")) {
                    fullNameEdt.setError(getApplicationContext().getString(R.string.full_name_validation));
                    fullNameEdt.requestFocus();
                    return;
                }

                if (usernameEdt.getText().toString().equals("")) {
                    usernameEdt.setError(getApplicationContext().getString(R.string.user_name_validation));
                    usernameEdt.requestFocus();
                    return;
                }

                if (emailEdt.getText().toString().equals("")) {
                    emailEdt.setError(getApplicationContext().getString(R.string.email_validation));
                    emailEdt.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(
                        emailEdt.getText().toString()).matches()) {
                    emailEdt.setError("Enter Valid Email Address");
                    emailEdt.requestFocus();
                    return;
                }

                if (passwordEdt.getText().toString().equals("")) {
                    passwordEdt.setError("Enter Password");
                    passwordEdt.requestFocus();
                    return;
                }

                if (passwordEdt.getText().toString().trim().length() < 6) {
                    passwordEdt.setError("Password must be minimun 6 character");
                    passwordEdt.requestFocus();
                    return;
                }

                if (passwordEdt.getText().toString().length() > 15) {
                    passwordEdt.setError("Password must be maximum 15 character");
                    passwordEdt.requestFocus();
                    return;
                }

                if (mobileEdt.getText().toString().equals("")) {
                    mobileEdt.setError("Enter Mobile Number");
                    mobileEdt.requestFocus();
                    return;
                }

                if (mobileEdt.getText().toString().trim().length() == 9) {
                    mobileEdt.setError("Mobile number must be 10 digit");
                    mobileEdt.requestFocus();
                    return;
                }

                isTeacher = isTeacherChBox.isChecked();
                // CALL REGISTER API
                callRegisterApi();

                break;


        }
    }


    boolean checkValidation() {
        if (getText(fullNameEdt).length() < 4) {
            fullNameEdt.setError(getApplicationContext().getString(R.string.full_name_validation));
            fullNameEdt.requestFocus();
            return false;
        } else if (getText(usernameEdt).length() < 4) {
            usernameEdt.setError(getApplicationContext().getString(R.string.user_name_validation));
            usernameEdt.requestFocus();
            return false;
        } else if (Utils.emailValidator(String.valueOf(emailEdt.getText()))) {
            emailEdt.setError(getApplicationContext().getString(R.string.email_validation));
            emailEdt.requestFocus();
            return false;
        } else if (getText(passwordEdt).length() < 6) {
            passwordEdt.setError(getApplicationContext().getString(R.string.password_validation));
            passwordEdt.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    // CALL REGISTER API HERE
    private void callRegisterApi() {

        if (!Utils.checkNetwork(RegisterActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), RegisterActivity.this);
            return;
        }


        ApiHandler.getApiService().getRegister(getRegisterDetail(), new retrofit.Callback<RegisterMainResponse>() {
            @Override
            public void success(RegisterMainResponse registerMainResponse, Response response) {
                if (registerMainResponse == null) {
                    toast("Something Wrong");

                    return;

                }
                if (registerMainResponse.getStatus() == null) {
                    toast("Something Wrong");

                    return;
                }
                if (registerMainResponse.getStatus().equals("false")) {
                    toast(registerMainResponse.getMsg());

                    return;
                }
                if (registerMainResponse.getStatus().equals("true")) {
                    toast("" + registerMainResponse.getMsg());
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                error.getMessage();
                toast("Something Wrong");
            }
        });

    }

    private Map<String, String> getRegisterDetail() {
        Map<String, String> map = new HashMap<>();
//        map.put("fullname", "" + fullNameEdt.getText().toString());
        map.put("username", "" + usernameEdt.getText().toString());
        map.put("email", "" + emailEdt.getText().toString());
        map.put("password", "" + passwordEdt.getText().toString());
        map.put("mob_no", "" + mobileEdt.getText().toString());
        map.put("fullName", String.valueOf(fullNameEdt.getText()));
        if (isTeacher) {
            map.put("role", ROLE_TEACHER);
        } else {
            map.put("role", ROLE_STUDENT);
        }
        return map;
    }
}
