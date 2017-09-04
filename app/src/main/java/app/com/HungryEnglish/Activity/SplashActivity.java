package app.com.HungryEnglish.Activity;

import android.os.Bundle;
import android.os.Handler;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;

/**
 * Created by Vnnovate on 6/29/2017.
 */

public class SplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String isLoggedIn = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN);
                String isActiveStatue = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_IS_ACTIVE);
                String role = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
                if (isLoggedIn.equalsIgnoreCase("1")) {


                    if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {
                        startActivity(StudentProfileActivity.class);
                        finish();
                    } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity( TeacherListActivity.class);
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {

                        startActivity( LoginActivity.class);
                        finish();

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity(MainActivity.class);
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                        startActivity( MainActivity.class);
                        finish();
                    } else if (role.equalsIgnoreCase("admin")) {
                        startActivity( AdminDashboardActivity.class);
                        finish();
                    }



                } else {
                    startActivity( LoginActivity.class);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }
}
