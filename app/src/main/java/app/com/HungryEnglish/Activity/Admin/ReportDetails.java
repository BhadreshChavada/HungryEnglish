package app.com.HungryEnglish.Activity.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Model.Report.Datum;
import app.com.HungryEnglish.R;

/**
 * Created by Bhadresh Chavada on 02-08-2017.
 */

public class ReportDetails extends BaseActivity {
    private ArrayList<? extends Datum> reportList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getIntent()!=null){
            reportList = getIntent().getParcelableArrayListExtra("ReportDetails");

            Toast.makeText(this, reportList.get(0).getTeacherFullName(), Toast.LENGTH_SHORT).show();
        }
    }
}
