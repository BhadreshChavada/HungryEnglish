package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Model.StudentList.StudentData;
import app.com.HungryEnglish.R;

import static app.com.HungryEnglish.Activity.Student.StudentListActivity.callRemoveStudentFromListApi;

/**
 * Created by R'jul on 7/14/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private List<StudentData> studentList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility, etEditStudentProfile;
        public RelativeLayout ivEdit, ivRemove;
        public ImageView ivProfilePic;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            ivEdit = (RelativeLayout) view.findViewById(R.id.studentEditRelative);
            ivRemove = (RelativeLayout) view.findViewById(R.id.studentRemoveRelative);
        }
    }


    public StudentListAdapter(Context mainActivity, List<StudentData> studentList) {

        this.studentList = studentList;
        this.mContext = mainActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        holder.tvTeacherName.setText(studentList.get(position).getUsername());
        holder.tvEmail.setText("Email : " + studentList.get(position).getEmail());
        holder.tvMobileNo.setText("Mobile No : " + studentList.get(position).getMobNo());
        holder.tvTeacherAvaibility.setText("Avaibility : " + String.valueOf(studentList.get(position).getStudentInfo().getAvailableTime()));
        holder.ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentProfileActivity.class);
                intent.putExtra("id", studentList.get(pos).getId());
                intent.putExtra("role", studentList.get(pos).getRole());
                mContext.startActivity(intent);
            }
        });
        holder.ivRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                callRemoveStudentFromListApi(pos, studentList.get(pos).getId(), studentList.get(pos).getRole());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
