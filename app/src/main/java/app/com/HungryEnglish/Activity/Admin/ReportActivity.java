package app.com.HungryEnglish.Activity.Admin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.ReportAdapter;
import app.com.HungryEnglish.Model.Report.Datum;
import app.com.HungryEnglish.Model.Report.ReportModel;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Constant.FILE_TYPE_REPORT;

/**
 * Created by Bhadresh Chavada on 27-07-2017.
 */

public class ReportActivity extends BaseActivity {

    RecyclerView reportList;
    Button btnGenerateReport;
    List<Datum> reportArraylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reportList = (RecyclerView) findViewById(R.id.report_list);
        btnGenerateReport = (Button) findViewById(R.id.btn_generate_report);
        getReportDetails();

        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportArraylist.size() > 0) {
                    new GenerateReport().execute();
                } else {
                    toast("No Record Found");
                }
            }
        });
    }


    private class GenerateReport extends AsyncTask<String, Boolean, Boolean> {
        File folderPath;
        File filePath;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ReportActivity.this);
            pd.setMessage(getString(R.string.loading));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_MM");
            String date = simpleDateFormat.format(new Date().getTime());
            String fileName = "report_" + date + ".xls";

            //Saving file in external storage
            folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + FILE_TYPE_REPORT + "/");

            //create directory if not exist
            if (!folderPath.isDirectory()) {
                folderPath.mkdirs();
            }

            //file path
            filePath = new File(folderPath, fileName);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            try {
                //Excel sheet name. 0 represents first sheet
                workbook = Workbook.createWorkbook(filePath, wbSettings);
                for (int i = 0; i < reportArraylist.size(); i++) {
                    WritableSheet sheet = workbook.createSheet(reportArraylist.get(i).getStuFullName() + "  " + i, i);
                    try {
                        sheet.addCell(new Label(0, 0, "Teacher Email")); // column and row
                        sheet.addCell(new Label(0, 1, "Teacher Full Name")); // column and row
                        sheet.addCell(new Label(0, 2, "Teacher WeChat")); // column and row
                        sheet.addCell(new Label(0, 3, "Teacher Address")); // column and row
                        sheet.addCell(new Label(0, 4, "Teacher Time")); // column and row
                        sheet.addCell(new Label(0, 5, "Teacher Skills"));

                        sheet.addCell(new Label(0, 8, "Student Email")); // column and row
                        sheet.addCell(new Label(0, 9, "Student Full Name")); // column and row
                        sheet.addCell(new Label(0, 10, "Student WeChat")); // column and row
                        sheet.addCell(new Label(0, 11, "Student Sex")); // column and row
                        sheet.addCell(new Label(0, 12, "Student Age")); // column and row
                        sheet.addCell(new Label(0, 13, "Student Station")); // column and row
                        sheet.addCell(new Label(0, 14, "Student Skills")); // column and row
                        sheet.addCell(new Label(0, 15, "Student Time")); // column and row


                        sheet.addCell(new Label(1, 0, reportArraylist.get(i).getTeacherEmail())); // column and row
                        sheet.addCell(new Label(1, 1, reportArraylist.get(i).getTeacherFullName())); // column and row
                        sheet.addCell(new Label(1, 2, reportArraylist.get(i).getTeacherWeChat())); // column and row
                        sheet.addCell(new Label(1, 3, reportArraylist.get(i).getTAddress())); // column and row
                        sheet.addCell(new Label(1, 4, reportArraylist.get(i).getTTime())); // column and row
                        sheet.addCell(new Label(1, 5, reportArraylist.get(i).getTSkills()));

                        sheet.addCell(new Label(1, 8, reportArraylist.get(i).getStuEmail())); // column and row
                        sheet.addCell(new Label(1, 9, reportArraylist.get(i).getStuFullName())); // column and row
                        sheet.addCell(new Label(1, 10, reportArraylist.get(i).getStuWeChat())); // column and row
                        sheet.addCell(new Label(1, 11, reportArraylist.get(i).getStuSex())); // column and row
                        sheet.addCell(new Label(1, 12, reportArraylist.get(i).getStuAge())); // column and row
                        sheet.addCell(new Label(1, 13, reportArraylist.get(i).getStuStation())); // column and row
                        sheet.addCell(new Label(1, 14, reportArraylist.get(i).getStuSkills())); // column and row
                        sheet.addCell(new Label(1, 15, reportArraylist.get(i).getStuTime())); // column and row


                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
                workbook.write();
                try {
                    workbook.close();
                    return true;
                } catch (WriteException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                toast(getString(R.string.report_sucess));
            } else {
                toast(getString(R.string.error_something_wrong));
            }
        }
    }

    void getReportDetails() {
        if (!Utils.checkNetwork(ReportActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), ReportActivity.this);
            return;
        } else {
            Utils.showDialog(ReportActivity.this);
            ApiHandler.getApiService().getReportList(new retrofit.Callback<ReportModel>() {

                @Override
                public void success(ReportModel reportModel, Response response) {
                    Utils.dismissDialog();
                    if (reportModel == null) {
                        toast(getString(R.string.error_something_wrong));
                        return;
                    }
                    if (reportModel.getStatus() == null) {
                        toast(getString(R.string.error_something_wrong));
                        return;
                    }
                    if (reportModel.getStatus().equals("false")) {

                        toast(reportModel.getMsg());

                        return;
                    }
                    if (reportModel.getStatus().equals("true")) {
                        reportArraylist = new ArrayList<Datum>();
                        reportArraylist = reportModel.getData();

                        ReportAdapter reportAdapter = new ReportAdapter(ReportActivity.this, reportArraylist);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        reportList.setLayoutManager(mLayoutManager);
                        reportList.setItemAnimator(new DefaultItemAnimator());
                        reportList.setAdapter(reportAdapter);

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    toast(getString(R.string.error_something_wrong));
                }
            });

        }
    }
}
