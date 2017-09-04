package app.com.HungryEnglish.Activity.Teacher;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.ForgotPassword;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Mail;
import app.com.HungryEnglish.Util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static app.com.HungryEnglish.R.id.usernameStudentEdit;
import static app.com.HungryEnglish.Util.Constant.BASEURL;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ROLE;
import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;


/**
 * Created by Rujul on 6/30/2017.
 */

public class TeacherProfileActivity extends BaseActivity implements
        View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private ImageView profileImage, idProofImage, ivCVFileStatus, ivAudioFileStatus, ivViewCv, ivViewAudio;
    final int SELECT_PHOTO = 100;
    final int SELECT_ID_PROOF = 200;
    final int SELECT_FILE = 300;
    final int SELECT_AUDIO = 400;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private EditText btnCvUpload, btnAudioFile, userNameEdit, emailEdit, currnetPlaceEdit, fullNameTeacherEdit, avaibilityDateTeacherEdit, specialSkillTeacherEdit, etMobileOrWechatId;

    private String pathProfilePic = null, pathCvDoc = null, pathIdProofPic = null, pathAudioFile = null;
    private Button btnSubmiTeacherProfile;
    private String id = "", role = "";
    String cvFileName, audioFileName, audioPath, resumePath;
    ProgressDialog mProgressDialog;
    String CallFrom = "";
    Context mContext;
    LinearLayout layoutIdProof;
    LinearLayout layoutCV;
    EditText btn_id_proof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        mContext = getApplicationContext();
        getDataFromIntent();
        idMapping();

        getProfile();
    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            role = extras.getString("role");
            CallFrom = extras.getString("callFrom");
        }
    }

    private void idMapping() {
        profileImage = (ImageView) findViewById(R.id.profile_image);
        idProofImage = (ImageView) findViewById(R.id.idProofImage);
        ivCVFileStatus = (ImageView) findViewById(R.id.ivCVFileStatus);
        ivAudioFileStatus = (ImageView) findViewById(R.id.ivAudioFileStatus);
        ivViewCv = (ImageView) findViewById(R.id.ivViewCv);
        ivViewAudio = (ImageView) findViewById(R.id.ivViewAudio);

        etMobileOrWechatId = (EditText) findViewById(R.id.etMobileOrWechatId);
        currnetPlaceEdit = (EditText) findViewById(R.id.currnetPlaceEdit);
        fullNameTeacherEdit = (EditText) findViewById(R.id.fullNameTeacherEdit);
        avaibilityDateTeacherEdit = (EditText) findViewById(R.id.avaibilityDateTeacherEdit);
        specialSkillTeacherEdit = (EditText) findViewById(R.id.specialSkillTeacherEdit);
        btnAudioFile = (EditText) findViewById(R.id.btn_audio_file);
        btnCvUpload = (EditText) findViewById(R.id.btn_cv_file);
        userNameEdit = (EditText) findViewById(usernameStudentEdit);
        layoutCV = (LinearLayout) findViewById(R.id.layout_cv);
        layoutIdProof = (LinearLayout) findViewById(R.id.layout_idproof);
        btn_id_proof = (EditText)findViewById(R.id.btn_id_proof);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        btnSubmiTeacherProfile = (Button) findViewById(R.id.btnSubmiTeacherProfile);

        String role = Utils.ReadSharePrefrence(getApplicationContext(), Constant.SHARED_PREFS.KEY_USER_ROLE);
        if (role.equalsIgnoreCase("admin")) {
            ivViewCv.setVisibility(View.VISIBLE);
            ivViewAudio.setVisibility(View.VISIBLE);
        }


        if (CallFrom.equals("Student")) {

            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.requst_teacher));

            TextInputLayout wechatLayout = (TextInputLayout) findViewById(R.id.wechat_id_textinput);
            TextInputLayout cvLayout = (TextInputLayout) findViewById(R.id.text_input_layout_uploadCV);
            TextInputLayout idProof = (TextInputLayout) findViewById(R.id.text_input_layout_uploadIdProof);
            TextInputLayout emailLayout  = (TextInputLayout)findViewById(R.id.layout_email);

            btnSubmiTeacherProfile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setVisibility(View.VISIBLE);
            ivViewAudio.setOnClickListener(this);

            btnAudioFile.setVisibility(View.VISIBLE);

            btnCvUpload.setKeyListener(null);
            btn_id_proof.setKeyListener(null);
            btnAudioFile.setKeyListener(null);
            userNameEdit.setKeyListener(null);
            emailEdit.setKeyListener(null);
            currnetPlaceEdit.setKeyListener(null);
            fullNameTeacherEdit.setKeyListener(null);
            avaibilityDateTeacherEdit.setKeyListener(null);
            specialSkillTeacherEdit.setKeyListener(null);
            etMobileOrWechatId.setKeyListener(null);
            wechatLayout.setVisibility(View.GONE);
            cvLayout.setVisibility(View.GONE);
            ivCVFileStatus.setVisibility(View.GONE);
            ivViewCv.setVisibility(View.GONE);
            layoutIdProof.setVisibility(View.GONE);
            layoutCV.setVisibility(View.GONE);
            emailLayout.setVisibility(View.GONE);



        } else if (CallFrom.equals("Admin")) {
            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.submit));

            profileImage.setOnClickListener(this);
//            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
            currnetPlaceEdit.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
            btn_id_proof.setOnClickListener(this);
        } else {
            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.submit));

            profileImage.setOnClickListener(this);
//            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
            currnetPlaceEdit.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
            btn_id_proof.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivViewCv:
                createDirectory(resumePath, Constant.FILE_TYPE_RESUME);
                break;

            case R.id.ivViewAudio:
                createDirectory(audioPath, Constant.FILE_TYPE_AUDIO);
                break;

            case R.id.profile_image:
                uploadImage(SELECT_PHOTO);
                break;
            case R.id.btn_id_proof:
                uploadImage(SELECT_ID_PROOF);
                break;
            case R.id.btn_cv_file:
                uploadFile();
                break;
            case R.id.btn_audio_file:
                uploadAudio();
                break;
            case R.id.btnSubmiTeacherProfile:
                if (CallFrom.equals("Student")) {
                    RequestToTeacher();
                } else {
                    if (fullNameTeacherEdit.getText().toString().equals("")) {
                        fullNameTeacherEdit.setError("Enter Name");
                        fullNameTeacherEdit.requestFocus();
                        return;
                    }
                    if (avaibilityDateTeacherEdit.getText().toString().equals("")) {
                        avaibilityDateTeacherEdit.setError("Enter Avaibility");
                        avaibilityDateTeacherEdit.requestFocus();
                        return;
                    }
                    if (specialSkillTeacherEdit.getText().toString().equals("")) {
                        specialSkillTeacherEdit.setError("Enter Special Skills");
                        specialSkillTeacherEdit.requestFocus();
                        return;
                    }

                    if (etMobileOrWechatId.getText().toString().equals("")) {
                        etMobileOrWechatId.setError("Enter Mobile No. or Wechat Id");
                        etMobileOrWechatId.requestFocus();
                    }

                    callTeacherProfileApi();
                    break;
                }
        }
    }


    private void uploadFile() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("*/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }


    private void uploadAudio() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("audio/*");
        startActivityForResult(photoPickerIntent, SELECT_AUDIO);
    }

    private void uploadImage(int PHOTO_CONSTANT) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    toast(R.string.sucess_external_storage_msg);

                } else {
                    toast(R.string.error_permission_msg);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
//        getRealPathFromURI(this, data.getData());
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathProfilePic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathProfilePic = getPath(this, data.getData());
                    }
                    Picasso.with(getApplicationContext()).load(Uri.fromFile(new File(pathProfilePic))).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(profileImage);
                    break;
                case SELECT_ID_PROOF:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathIdProofPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathIdProofPic = getPath(this, data.getData());
                    }
                    String[] spiltFileNameArray = pathIdProofPic.split("/");
                    cvFileName = spiltFileNameArray[spiltFileNameArray.length - 1];
                    btn_id_proof.setText(cvFileName);

                    Picasso.with(getApplicationContext()).load(Uri.fromFile(new File(pathIdProofPic))).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(idProofImage);
                    break;
                case SELECT_FILE:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathCvDoc = getRealPathFromURI(this, data.getData());
                    } else {
                        pathCvDoc = getPath(this, data.getData());
                    }
                    String[] spiltArray = pathCvDoc.split("/");
                    cvFileName = spiltArray[spiltArray.length - 1];
                    btnCvUpload.setText(cvFileName);
                    if (!pathCvDoc.equals("")) {
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_file).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(ivCVFileStatus);
                    }
                    break;
                case SELECT_AUDIO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathAudioFile = getRealPathFromURI(this, data.getData());
                    } else {
                        pathAudioFile = getPath(this, data.getData());
                    }
                    String[] spiltAudioArray = pathAudioFile.split("/");
                    audioFileName = spiltAudioArray[spiltAudioArray.length - 1];
                    btnAudioFile.setText(audioFileName);
                    if (!btnAudioFile.equals("")) {
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_file).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(ivAudioFileStatus);
                    }
                    break;


            }
        } else {
            toast("You cancel current task.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

            }

        } else {

        }

    }

    private void getProfile() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            map.put("uId", read(KEY_USER_ID));
            map.put("role", read(KEY_USER_ROLE));
        } else {
            map.put("uId", id);
            map.put("role", role);
        }
        ApiHandler.getApiService().getTeacherProfile(map, new Callback<TeacherProfileMain>() {
            @Override
            public void success(TeacherProfileMain teacherProfileMain, Response response) {
                fullNameTeacherEdit.setText(teacherProfileMain.getData().getFullName());
                emailEdit.setText(teacherProfileMain.getData().getEmail());
                userNameEdit.setText(teacherProfileMain.getData().getUsername());
                etMobileOrWechatId.setText(String.valueOf(teacherProfileMain.getData().getMobNo()));
                if (teacherProfileMain.getInfo() != null) {
                    avaibilityDateTeacherEdit.setText(teacherProfileMain.getInfo().getAvailableTime());
                    currnetPlaceEdit.setText(teacherProfileMain.getInfo().getAddress());
                    specialSkillTeacherEdit.setText(teacherProfileMain.getInfo().getSkills());
                    Picasso.with(getApplicationContext()).load(BASEURL + teacherProfileMain.getInfo().getProfileImage()).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(profileImage);
                    Picasso.with(getApplicationContext()).load(BASEURL + teacherProfileMain.getInfo().getIdImage()).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(idProofImage);
                    resumePath = teacherProfileMain.getInfo().getResume();
                    String idProof = Constant.BASEURL+teacherProfileMain.getInfo().getIdImage();
                    String[] cvFileArray = resumePath.split("/");
                    if (cvFileArray.length > 1) {
                        btnCvUpload.setText(cvFileArray[cvFileArray.length - 1]);
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_file).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(ivCVFileStatus);
                    }
                    String[] idFileArray = teacherProfileMain.getInfo().getIdImage().split("/");
                    if(idFileArray.length>1){
                        btn_id_proof.setText(idFileArray[idFileArray.length - 1]);
                        Picasso.with(getApplicationContext()).load(idProof).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(idProofImage);
                    }else{
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_file).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(idProofImage);
                    }

                    audioPath = teacherProfileMain.getInfo().getAudioFile();
                    String[] audioFileArray = audioPath.split("/");
                    if (audioFileArray.length > 1) {
                        btnAudioFile.setText(audioFileArray[audioFileArray.length - 1]);
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_file).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(ivAudioFileStatus);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void callTeacherProfileApi() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(getApplicationContext());
        TypedFile proImage = null, idProof = null, resume = null, audiofile = null;

        Map<String, TypedFile> files = new HashMap<String, TypedFile>();
        Log.i("HashMap", "SomeText: " + new Gson().toJson(files) + " second = " + new Gson().toJson(getTeacherProfileDetail()));

        if (pathProfilePic == null && pathIdProofPic == null && pathCvDoc == null && pathAudioFile == null) {

            ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), new Callback<TeacherProfileMainResponse>() {

                @Override
                public void success(TeacherProfileMainResponse teacherProfileMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherProfileMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("false")) {
                        toast(teacherProfileMainResponse.getMsg());
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("true")) {
                        toast(teacherProfileMainResponse.getMsg());
//                        startActivity(MainActivity.class);
                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    toast(getString(R.string.something_wrong));
                }
            });
        } else {
            if (pathProfilePic != null) {
                proImage = new TypedFile("multipart/form-data", new File(pathProfilePic));
                files.put("proImage", proImage);
            }

            if (pathIdProofPic != null) {
                idProof = new TypedFile("multipart/form-data", new File(pathIdProofPic));
                files.put("idProof", idProof);
            }
            if (pathCvDoc != null) {
                resume = new TypedFile("multipart/form-data", new File(pathCvDoc));
                files.put("resume", resume);
            }
            if (pathAudioFile != null) {
                audiofile = new TypedFile("multipart/form-data", new File(pathAudioFile));
                files.put("audioFile", audiofile);
            }
            ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), files, new Callback<TeacherProfileMainResponse>() {

                @Override
                public void success(TeacherProfileMainResponse teacherProfileMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherProfileMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("false")) {
                        toast(teacherProfileMainResponse.getMsg());
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("true")) {
                        toast(teacherProfileMainResponse.getMsg());
//                        startActivity(MainActivity.class);
                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    toast(getString(R.string.something_wrong));
                }
            });
        }


    }

    private Map<String, String> getTeacherProfileDetail() {
        Map<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            String userId = Utils.ReadSharePrefrence(getApplicationContext(), KEY_USER_ID);
            map.put("uId", userId);
        } else {
            map.put("uId", id);
        }
        map.put("fullname", String.valueOf(fullNameTeacherEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityDateTeacherEdit.getText()));
        map.put("address", String.valueOf(currnetPlaceEdit.getText()));
        map.put("skill", String.valueOf(specialSkillTeacherEdit.getText()));
        map.put("mob_no", String.valueOf(etMobileOrWechatId.getText()));
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    public void logout(View view) {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_logout);
        dialog.setCancelable(false);
        TextView tvLogout = (TextView) dialog.findViewById(R.id.btLogoutPopupLogout);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.btCancelPopupLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                write(Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "0");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
    }


    private void RequestToTeacher() {


        Utils.showDialog(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("teacherId", id);
        map.put("studentId", Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ID));

//        http://smartsquad.16mb.com/HungryEnglish/api/add_request.php?teacherId=1&studentId=2
        ApiHandler.getApiService().addRequest(map, new retrofit.Callback<ForgotPasswordModel>() {


            @Override
            public void success(ForgotPasswordModel forgotPasswordModel, Response response) {

                Utils.dismissDialog();


                if (forgotPasswordModel.getStatus().toString().equals("true")) {
                    toast(forgotPasswordModel.getMsg());
                    sendEmail(Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_NAME), String.valueOf(userNameEdit.getText()));

                    onBackPressed();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                toast(getString(R.string.try_again));
            }
        });

    }

    private void sendEmail(String studentName, String TeacherName) {

        String message = "Welcome to Hungry English Club " + studentName + "Enquiry for Teacher  " + TeacherName;

        String[] recipients = {"idigi@live.com"};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
//        email.activity = mContext;
        email.m = new Mail("hungryenglishclub@gmail.com", "rujulgandhi");
        email.m.set_from("hungryenglishclub@gmail.com");
        email.m.setBody(message);
        email.m.set_to(recipients);
        email.m.set_subject("Hungry English CLUB");
        email.execute();


    }


    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        ForgotPassword activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {


                } else {
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e("EmailError", "Bad account details");
                e.printStackTrace();

//            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e("EmailError", "Email failed");
                e.printStackTrace();

//            Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
                e.printStackTrace();

//            Toast.makeText(activity, "Unexpected error occured.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * Background Async Task to download file
     */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private String folderPath;
        private String fileName;

        public DownloadFileFromURL(String folderPath, String fileName) {
            this.folderPath = folderPath;
            this.fileName = fileName;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String fullUrl = BASEURL + f_url[0];
                Log.d("FullURL", fullUrl);
                URL url = new URL(fullUrl);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(folderPath + "/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(mContext, getString(R.string.file_downloaded), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    private void createDirectory(String file_name, String type) {
        Log.d("FilePath", file_name.split("/")[1]);
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_name) + "/" + type + "/");
            if (!folder.exists()) {
                folder.mkdirs();
                new DownloadFileFromURL(folder.getAbsolutePath(), file_name.split("/")[1]).execute(file_name);
            } else {
                new DownloadFileFromURL(folder.getAbsolutePath(), file_name.split("/")[1]).execute(file_name);
            }
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }
}