package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Fragment.TeacherPendingListFragment;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;

import static app.com.HungryEnglish.Fragment.TeacherApprovedListFragment.callRemoveTeacherFromListApi;

/**
 * Created by R'jul on 7/11/2017.
 */

public class TeacherPendingAdapter extends RecyclerView.Adapter<TeacherPendingAdapter.MyViewHolder> {
    private List<TeacherListResponse> teacherList;
    private Context mContext;
    OnItemClickListener mOnItemClickLister;
    private OnRemoveTeacherClickListener mListener;


    public interface OnRemoveTeacherClickListener {
        public void onItemClick(View view, int position);
    }

    public TeacherPendingAdapter(Context activity, List<TeacherListResponse> teacherList) {
        this.mContext = activity;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility, tvAcceptInvitation;
        public ImageView ivProfilePic, ivRemove, ivEdit;
        public LinearLayout llEditDel;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            tvAcceptInvitation = (TextView) view.findViewById(R.id.tvAcceptInvitation);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            llEditDel = (LinearLayout) view.findViewById(R.id.llEditDel);
            ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_pending_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        //        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(teacherList.get(pos).getUsername());

        holder.tvEmail.setText("Email : " + teacherList.get(pos).getEmail());

        holder.tvMobileNo.setText("Mobile No : " + teacherList.get(pos).getMobNo());

        holder.tvAcceptInvitation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherPendingListFragment.callTeacherAcceptInvitationApi(pos, teacherList.get(pos).getId(), teacherList.get(pos).getIsActive());
            }
        });


        holder.ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeacherProfileActivity.class);
                intent.putExtra("id", teacherList.get(pos).getId());
                intent.putExtra("callFrom", "Admin");
                intent.putExtra("role", teacherList.get(pos).getRole());
                mContext.startActivity(intent);
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callRemoveTeacherFromListApi(pos, teacherList.get(pos).getId(), teacherList.get(pos).getRole());

            }
        });
//        Picasso.with(mContext).load(R.drawable.ic_user_default).into(holder.ivProfilePic);
//        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
//        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
//        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }


    @Override
    public int getItemCount() {
        return teacherList.size();
    }


}
