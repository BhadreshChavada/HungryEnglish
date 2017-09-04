package app.com.HungryEnglish.Activity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.SetDatePicker;
import app.com.HungryEnglish.Util.Utils;



/**
 * Created by AMD21 on 13/5/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void toast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int resId) {
        toast(this.getResources().getText(resId));
    }

    protected void startActivity(Class klass, boolean clearFlag) {
        Intent in = new Intent(this, klass);
        if (clearFlag) {
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(in);
    }

    protected void startActivity(Class klass) {
        Intent in = new Intent(this, klass);
        startActivity(in);
    }


    protected void startActivityData(Class klass, HashMap<String, String> hash) {
        Intent in = new Intent(this, klass);
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            in.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(new Intent(this, klass));
    }

    protected String getText(EditText eTxt) {
        return eTxt == null ? "" : eTxt.getText().toString().trim();
    }

    protected boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    protected void write(String key, String val) {
        Utils.WriteSharePrefrence(this, key, val);
    }

    protected String read(String key) {
        return Utils.ReadSharePrefrence(this, key);
    }

    protected void clear() {
        Utils.ClearSharePrefrence(this);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isLoggedIn() {
        if (read(Constant.SHARED_PREFS.KEY_USER_ID).equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }
    }

    public void setToolBar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // TODO: 09/6/17 Show the calender and dispaly Date
    protected void chooseDate(final TextView textView, final SetDatePicker dateResponse) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BaseActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(newDate.getTime());
                dateResponse.onDateSelect(date, textView);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }


    // TODO: 09/6/17 Show the TimePicker and dispaly Date
    protected void chooseTime(final TextView textView, final SetDatePicker dateResponse) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(BaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                dateResponse.onDateSelect(selectedHour + ":" + selectedMinute, textView);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
