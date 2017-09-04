package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Adapter.TeacherApprovedAdapter;
import app.com.HungryEnglish.Model.RemoveTeacher.RemoveTeacherFromListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Vnnovate on 7/5/2017.
 */

public class TeacherApprovedListFragment extends Fragment {
    View mView;
    private static RecyclerView recyclerTearcherList;
    static List<TeacherListResponse> teacherList;
    private static TeacherApprovedAdapter teacherApprovedAdapter;
    public static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_teacher_aproved_list, container, false);
        mContext = getActivity();
        idMapping();
        callTeacherListApi();

        return mView;
    }

    private void idMapping() {
        recyclerTearcherList = (RecyclerView) mView.findViewById(R.id.recyclerTearcherList);
    }

    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {
        if (!Utils.checkNetwork(getActivity())) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), getActivity());
            return;
        } else {
            Utils.showDialog(getActivity());
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {
                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(getActivity(), "" + teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        teacherList = new ArrayList<TeacherListResponse>();
                        teacherList = teacherListMainResponse.getData();

                        teacherApprovedAdapter = new TeacherApprovedAdapter(getActivity(), teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherApprovedAdapter);
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
        map.put("status", "1");
        map.put("optional_status", "2");
        return map;
    }

//    http://smartsquad.16mb.com/HungryEnglish/api/delete_user.php?id=19&role=student

    // CALL DELETE TEACHER FROM LIST API HERE
    public static void callRemoveTeacherFromListApi(int pos, String id, String role) {
        final int position = pos;
        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog("Internet Connection !", mContext.getResources().getString(R.string.internet_connection_error), (Activity) mContext);
            return;
        }
        Utils.showDialog(mContext);
        ApiHandler.getApiService().getRemoveTeacherFromList(removeTeacherDetail(id, role), new retrofit.Callback<RemoveTeacherFromListMainResponse>() {
            @Override
            public void success(RemoveTeacherFromListMainResponse removeTeacherFromListMainResponse, Response response) {
                Utils.dismissDialog();
                if (removeTeacherFromListMainResponse == null) {
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (removeTeacherFromListMainResponse.getStatus() == null) {
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (removeTeacherFromListMainResponse.getStatus().equals("false")) {
                    Toast.makeText(mContext, "" + removeTeacherFromListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (removeTeacherFromListMainResponse.getStatus().equals("true")) {
                    Toast.makeText(mContext, removeTeacherFromListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                    teacherApprovedAdapter = new TeacherApprovedAdapter(mContext, teacherList);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerTearcherList.setLayoutManager(mLayoutManager);
//                    recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
//                    recyclerTearcherList.setAdapter(teacherApprovedAdapter);
                    teacherList.remove(position);
                    teacherApprovedAdapter.notifyDataSetChanged();
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

    private static Map<String, String> removeTeacherDetail(String id, String role) {
        Map<String, String> map = new HashMap<>();
        map.put("role", role);
        map.put("id", id);
        return map;
    }


}
