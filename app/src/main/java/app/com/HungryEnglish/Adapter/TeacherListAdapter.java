package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Fragment.TeacherApprovedListFragment;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<TeacherListResponse> teacherList;
    private Context mContext;
    TeacherApprovedListFragment activity;
    //    private OnRemoveTeacherClickListener mListener;

    public TeacherListAdapter(Context mContext, List<TeacherListResponse> teacherList) {
        this.mContext = mContext;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvClosestStation, tvTeacherAvaibility, tvSpecialSkills, tvReportTeacher;
        public ImageView ivProfilePic;

        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvClosestStation = (TextView) view.findViewById(R.id.tvClosestStation);
            tvSpecialSkills = (TextView) view.findViewById(R.id.tvSpecialSkills);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            if (Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ROLE).equals("student")) {
                tvReportTeacher = (TextView) view.findViewById(R.id.tvReportTeacher);
            }


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvTeacherName.setText(teacherList.get(position).getUsername());
        holder.tvReportTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, TeacherProfileActivity.class);
                intent.putExtra("id", teacherList.get(position).getId());
                intent.putExtra("role", teacherList.get(position).getRole());
                intent.putExtra("callFrom", "Student");
                mContext.startActivity(intent);

            }
        });
        if (teacherList.get(position).getTeacherInfo() != null) {

            holder.tvSpecialSkills.setText(mContext.getString(R.string.special_skils_label) + " " + teacherList.get(position).getTeacherInfo().getSkills());

            holder.tvTeacherAvaibility.setText(mContext.getString(R.string.teacher_avaibility_label) + " " + teacherList.get(position).getTeacherInfo().getAvailableTime());

            holder.tvClosestStation.setText(teacherList.get(position).getTeacherInfo().getAddress());

            String profilePicUrl = Constant.BASEURL + "" + teacherList.get(position).getTeacherInfo().getProfileImage();
            Log.e("URL", "" + profilePicUrl);
            Picasso.with(mContext).load(profilePicUrl).error(R.drawable.ic_user_default).into(holder.ivProfilePic);

            if (Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ROLE).equals("student")) {
                holder.tvReportTeacher.setVisibility(View.VISIBLE);
            } else {
                holder.tvReportTeacher.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return teacherList.size();
    }


}