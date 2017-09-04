package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Adapter.TeacherPendingAdapter;
import app.com.HungryEnglish.Model.RemoveTeacher.RemoveTeacherFromListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherPendingRequestMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ROLE;
import static app.com.HungryEnglish.Util.Utils.checkNetwork;
import static app.com.HungryEnglish.Util.Utils.showDialog;

/**
 * Created by Vnnovate on 7/5/2017.
 */

public class TeacherPendingListFragment extends Fragment {

    View mView;
    private static RecyclerView recyclerTearcherList;
    static List<TeacherListResponse> teacherList;
//    private static TeacherPendingAdapter teacherPendingAdapter;
    private static TeacherPendingAdapter teacherPendingListAdapter;
    private static String status, uid;
    public static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_teacher_aproved_list, container, false);

        idMapping();

        return mView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) callTeacherListApi();
    }

    private void idMapping() {
        mContext = getActivity();
        recyclerTearcherList = (RecyclerView) mView.findViewById(R.id.recyclerTearcherList);
    }


    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {
        if (!checkNetwork(getActivity())) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), getActivity());

            return;
        } else {
            showDialog(getActivity());
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {

                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null || teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        teacherList = new ArrayList<TeacherListResponse>();
                        teacherList = teacherListMainResponse.getData();

                        teacherPendingListAdapter = new TeacherPendingAdapter(getActivity(), teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherPendingListAdapter);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "teacher");
        map.put("status", "0");
        return map;
    }


    // CALL TEACHER LIST API HERE
    public static void callTeacherAcceptInvitationApi(final int position, String id, String isActive) {
        uid = id;
        status = isActive;
        if (!checkNetwork(mContext)) {

            Utils.showCustomDialog("Internet Connection !", mContext.getString(R.string.internet_connection_error), (Activity) mContext);

            return;
        } else {
            showDialog(mContext);
            ApiHandler.getApiService().acceptTeacherPendingRequest(teacherAcceptInvitationDetail(), new retrofit.Callback<TeacherPendingRequestMainResponse>() {

                @Override
                public void success(TeacherPendingRequestMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {

                        Toast.makeText(mContext, "" + teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {

                        teacherPendingListAdapter = new TeacherPendingAdapter(mContext, teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherPendingListAdapter);
                        teacherList.remove(position);
                        teacherPendingListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private static Map<String, String> teacherAcceptInvitationDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", uid);
        map.put("status", "1");

        Log.e("map", "TEACHER ACCEPT " + map);
        return map;
    }
    // CALL DELETE TEACHER FROM LIST API HERE
    public static void callRemoveTeacherFromListApi(final int pos) {
        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog("Internet Connection !", mContext.getResources().getString(R.string.internet_connection_error), (Activity) mContext);
            return;
        } else {
            Utils.showDialog(mContext);
            ApiHandler.getApiService().getRemoveTeacherFromList(removeTeacherDetail(), new retrofit.Callback<RemoveTeacherFromListMainResponse>() {
                @Override
                public void success(RemoveTeacherFromListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(mContext, "" + teacherListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        Toast.makeText(mContext, teacherListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                        teacherPendingListAdapter = new TeacherPendingAdapter(mContext, teacherList);
//                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                        recyclerTearcherList.setLayoutManager(mLayoutManager);
//                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
//                        recyclerTearcherList.setAdapter(teacherPendingListAdapter);
                        teacherList.remove(pos);
                        teacherPendingListAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private static Map<String, String> removeTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", Utils.ReadSharePrefrence(mContext, KEY_USER_ROLE));
        map.put("id", Utils.ReadSharePrefrence(mContext, KEY_USER_ID));
        return map;
    }

}