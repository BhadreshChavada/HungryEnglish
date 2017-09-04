package app.com.HungryEnglish.Activity.Admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.Model.admin.AddInfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AddImageOrLinkActivity extends BaseActivity {
    final int SELECT_PHOTO = 100;
    final int SELECT_PHOTO2 = 200;
    final int SELECT_PHOTO3 = 300;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private LinearLayout llAddMoreLayout;
    private TextView tvAddMoreBtn;
    private List<EditText> allEds;
    private List<EditText> lintTitleArray;
    public int cnt = 0;
    EditText etAddMoreLink;
    private ImageView ivSelectImage, ivSelectImage2, ivSelectImage3;
    EditText imageLink1, imageLink2, imageLink3;

    private TextView tvSubmitLink;
    private String pathPic = "", pathPic3 = "", pathPic2 = "";
    public static int maxLinks = 5;
    public ArrayList<String> links;

    InfoResponse infoList;
    private LinearLayout llLinkList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_or_link);
        idMapping();
        setOnClick();

        allEds = new ArrayList<EditText>();
        lintTitleArray = new ArrayList<>();
        links = new ArrayList<>();

        callGetInfoApi();
    }

    private void hideAddMoreButton() {
        if (allEds.size() == 6) {
            tvAddMoreBtn.setVisibility(View.GONE);
        }
    }


    private void idMapping() {
        llAddMoreLayout = (LinearLayout) findViewById(R.id.llAddMoreLayout);
        tvAddMoreBtn = (TextView) findViewById(R.id.tvAddMoreBtn);
        ivSelectImage = (ImageView) findViewById(R.id.ivSelectImage1);
        ivSelectImage2 = (ImageView) findViewById(R.id.ivSelectImage2);
        ivSelectImage3 = (ImageView) findViewById(R.id.ivSelectImage3);
        imageLink1 = (EditText) findViewById(R.id.edt_image1_url);
        imageLink2 = (EditText) findViewById(R.id.edt_image2_url);
        imageLink3 = (EditText) findViewById(R.id.edt_image3_url);


        tvSubmitLink = (TextView) findViewById(R.id.tvSubmitLink);
    }

    private void setOnClick() {
        tvAddMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allEds.size() > 0 && allEds.size() <= maxLinks) {
                    addDynamicContactText("", "");
                }
            }
        });

        ivSelectImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(SELECT_PHOTO);
            }
        });

        ivSelectImage2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(SELECT_PHOTO2);
            }
        });

        ivSelectImage3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(SELECT_PHOTO3);
            }
        });
        tvSubmitLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pathPic.equalsIgnoreCase("")) {
                    Toast.makeText(AddImageOrLinkActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pathPic2.equalsIgnoreCase("")) {
                    Toast.makeText(AddImageOrLinkActivity.this, "Please Select Image 2", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pathPic3.equalsIgnoreCase("")) {
                    Toast.makeText(AddImageOrLinkActivity.this, "Please Select Image 3", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int i = 0; i < allEds.size(); i++) {
                    String edtText = getText(allEds.get(i));
                    String lintTitle = getText(lintTitleArray.get(i));
                    if (edtText.equalsIgnoreCase("")) {
                        toast("Please Enter Link" + (i + 1));
                        return;
                    } else {
                        if (i == 0)
                            links.add(lintTitle + "--" + edtText + "--" + imageLink1.getText().toString());
                        else if (i == 1)
                            links.add(lintTitle + "--" + edtText + "--" + imageLink2.getText().toString());
                        else if (i == 2)
                            links.add(lintTitle + "--" + edtText + "--" + imageLink3.getText().toString());
                        else
                            links.add(lintTitle + "--" + edtText);
                    }
                }
                callSubmitImageAndLInkApi();
            }
        });
    }

    private void callSubmitImageAndLInkApi() {
        if (!Utils.checkNetwork(AddImageOrLinkActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AddImageOrLinkActivity.this);
            return;
        } else {
            Utils.showDialog(AddImageOrLinkActivity.this);
            TypedFile imageTypeFile = new TypedFile("multipart/form-data", new File(pathPic));
            TypedFile imageTypeFile2 = new TypedFile("multipart/form-data", new File(pathPic2));
            TypedFile imageTypeFile3 = new TypedFile("multipart/form-data", new File(pathPic3));
            ApiHandler.getApiService().addInfo(addInfoDetail(), imageTypeFile, imageTypeFile2, imageTypeFile3, new retrofit.Callback<AddInfoResponse>() {
                @Override
                public void success(AddInfoResponse addInfoResponse, Response response) {
                    Utils.dismissDialog();
                    if (addInfoResponse == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addInfoResponse.getStatus() == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addInfoResponse.getStatus().equals("false")) {
                        Toast.makeText(getApplicationContext(), "" + addInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addInfoResponse.getStatus().equals("true")) {
                        Toast.makeText(getApplicationContext(), "" + addInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        startActivity(AdminDashboardActivity.class);
                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> addInfoDetail() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < links.size(); i++) {
            map.put("link" + (i + 1), links.get(i));
        }
//        map.put("link1", imageLink1.getText().toString());
//        map.put("link2", imageLink2.getText().toString());
//        map.put("link3", imageLink3.getText().toString());


        return map;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminDashboardActivity.class);
        finish();
    }

    private void addDynamicContactText(String link1, String linkTitle) {
        cnt = cnt + 1;
        EditText tvLabel = new EditText(AddImageOrLinkActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvLabel.setLayoutParams(llp);
        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        if (linkTitle.equals("")) {
            tvLabel.setHint("Link " + cnt);
        } else {
            tvLabel.setText(linkTitle);
        }
        tvLabel.setTextColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tvLabel.setTextSize(12);
        tvLabel.setBackground(ContextCompat.getDrawable(AddImageOrLinkActivity.this, R.drawable.bg_edt));
        llAddMoreLayout.addView(tvLabel);
        lintTitleArray.add(tvLabel);
        etAddMoreLink = new EditText(AddImageOrLinkActivity.this);
        allEds.add(etAddMoreLink);
//            etAddMoreLink.setId(id);
        LinearLayout.LayoutParams llpET = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llpET.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        etAddMoreLink.setLayoutParams(llpET);
        etAddMoreLink.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        etAddMoreLink.setTextSize(12);
        etAddMoreLink.setBackground(ContextCompat.getDrawable(AddImageOrLinkActivity.this, R.drawable.bg_edt));
        etAddMoreLink.setBackground(null);
        etAddMoreLink.setInputType(InputType.TYPE_CLASS_TEXT);
        etAddMoreLink.setHint("Link " + cnt);
        etAddMoreLink.setText(link1);
        llAddMoreLayout.addView(etAddMoreLink);

        View view = new View(AddImageOrLinkActivity.this);
        LinearLayout.LayoutParams llparamView = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
        view.setLayoutParams(llparamView);
        view.setBackgroundColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
        llAddMoreLayout.addView(view);

        hideAddMoreButton();

    }


    private void uploadImage(int PHOTO_CONSTANT) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        Log.d("reqCode", String.valueOf(reqCode));
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathPic = getPath(this, data.getData());
                    }
                    ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                    Picasso.with(AddImageOrLinkActivity.this).load(Uri.fromFile(new File(pathPic))).error(R.drawable.ic_user_default).into(ivSelectImage);
                    break;

                case SELECT_PHOTO2:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathPic2 = getRealPathFromURI(this, data.getData());
                    } else {
                        pathPic2 = getPath(this, data.getData());
                    }
                    ivSelectImage2.setScaleType(ScaleType.CENTER_CROP);
                    Picasso.with(AddImageOrLinkActivity.this).load(Uri.fromFile(new File(pathPic2))).error(R.drawable.ic_user_default).into(ivSelectImage2);
                    break;

                case SELECT_PHOTO3:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathPic3 = getRealPathFromURI(this, data.getData());
                    } else {
                        pathPic3 = getPath(this, data.getData());
                    }
                    ivSelectImage3.setScaleType(ScaleType.CENTER_CROP);
                    Picasso.with(AddImageOrLinkActivity.this).load(Uri.fromFile(new File(pathPic3))).error(R.drawable.ic_user_default).into(ivSelectImage3);
                    break;

            }
        }
    }


    private void callGetInfoApi() {
        if (!Utils.checkNetwork(AddImageOrLinkActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AddImageOrLinkActivity.this);

            return;
        } else {

            ApiHandler.getApiService().getInfo(getInfo(), new retrofit.Callback<InfoMainResponse>() {

                @Override
                public void success(InfoMainResponse infoMainResponse, Response response) {

                    if (infoMainResponse == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (infoMainResponse.getStatus() == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("false")) {
                        toast(infoMainResponse.getMsg());
                        allEds.clear();
                        tvAddMoreBtn.setVisibility(View.VISIBLE);
                        addDynamicContactText("", "");
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("true")) {

                        infoList = new InfoResponse();
                        infoList = infoMainResponse.getInfo();
                        String imgUrl = Constant.BASEURL + infoList.getImage1();
                        ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                        Picasso.with(AddImageOrLinkActivity.this).load(imgUrl).placeholder(R.drawable.ic_add_img).error(R.drawable.ic_add_img).into(ivSelectImage);

                        String imgUrl2 = Constant.BASEURL + infoList.getImage2();
                        ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                        Picasso.with(AddImageOrLinkActivity.this).load(imgUrl2).placeholder(R.drawable.ic_add_img).error(R.drawable.ic_add_img).into(ivSelectImage2);


                        String imgUrl3 = Constant.BASEURL + infoList.getImage3();
                        ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                        Picasso.with(AddImageOrLinkActivity.this).load(imgUrl3).placeholder(R.drawable.ic_add_img).error(R.drawable.ic_add_img).into(ivSelectImage3);


                        if (!infoList.getLink1().equalsIgnoreCase("")) {
                            if (infoList.getLink1().contains("--")) {
                                String[] link1 = infoList.getLink1().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                                addDynamicContactText(link1[1], link1[0]);
                                imageLink1.setText(link1[2]);
                            }
                        }
                        if (!infoList.getLink2().equalsIgnoreCase("")) {
                            if (infoList.getLink2().contains("--")) {
                                String[] link1 = infoList.getLink2().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                                addDynamicContactText(link1[1], link1[0]);
                                imageLink2.setText(link1[2]);
                            }
                        }

                        if (!infoList.getLink3().equalsIgnoreCase("")) {
                            if (infoList.getLink3().contains("--")) {
                                String[] link1 = infoList.getLink3().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                                addDynamicContactText(link1[1], link1[0]);
                                imageLink3.setText(link1[2]);
                            }
                        }
                        if (!infoList.getLink4().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink4().split("--");
                            addDynamicContactText(link1[1],link1[0]);
                        }

                        if (!infoList.getLink5().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink5().split("--");
                            addDynamicContactText(link1[1],link1[0]);
                        }

                        if (!infoList.getLink6().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink6().split("--");
                            addDynamicContactText(link1[1],link1[0]);
                        }
                        hideAddMoreButton();
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

    }


    private Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //    private void showDynamicContactText(final String link1) {
//        cnt = cnt + 1;
//        TextView tvLabel = new TextView(AddImageOrLinkActivity.this);
//        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
//        tvLabel.setLayoutParams(llp);
//        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
//        tvLabel.setText(link1);
//        tvLabel.setTextColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
//        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//        tvLabel.setTextSize(12);
//        llAddMoreLayout.addView(tvLabel);
//        tvLabel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "";
//                if (!link1.startsWith("http://") && !link1.startsWith("https://")) {
//                    url = "http://" + link1;
//                } else {
//                    url = link1;
//                }
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
//            }
//
//        });
//    }
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
}
