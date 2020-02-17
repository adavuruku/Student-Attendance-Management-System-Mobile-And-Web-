package com.sherif.atbuattendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sherif146 on 11/01/2018.
 */

public class attendanceAdapter extends RecyclerView.Adapter<attendanceAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<attendanceList> attendanceList;
    private RecyclerView recyclerView;
  private  OnItemClickListener mlistener;
  //  private RecyclerItemClickListener listener;
    private Context activity;

    public attendanceAdapter(List<attendanceList> contacts, Context context, OnItemClickListener listener){
        this.activity = context;
        this.mlistener = listener;
        this.inflater = LayoutInflater.from(context);
        this.attendanceList = contacts;
    }
    public interface OnItemClickListener{
        void onCallClick(View v, int position);
    }
    public void setOnitemClickListener(OnItemClickListener listener){mlistener = listener;}
    @Override
    public attendanceAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row,parent,false);
        attendanceAdapter.RecyclerHolder holder= new attendanceAdapter.RecyclerHolder(view,mlistener);
        return holder;
    }
    @Override
    public void onBindViewHolder(attendanceAdapter.RecyclerHolder holder, int position) {
        attendanceList contact = attendanceList.get(position);
        holder.stname.setText((contact.getName()));
        holder.faculty.setText((contact.getFaculty()+ " - " + contact.getDept()));
        holder.dept.setText((contact.getRegno() + " - " + contact.getLevel() +" Level"));
        String sta = contact.getStatus();
        if(sta.equals("1")){
            holder.proceed.setChecked(true);
        }else{
            holder.proceed.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    //create the holder class
    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView dept,faculty,stname;
        CheckBox proceed;
        public RecyclerHolder(final View itemView,final OnItemClickListener listener) {
            super(itemView);
            dept = (TextView) itemView.findViewById(R.id.dept);
            proceed = (CheckBox) itemView.findViewById(R.id.mark);
            faculty = (TextView) itemView.findViewById(R.id.faculty);
            stname = (TextView) itemView.findViewById(R.id.stname);

            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCallClick(view, position);
                        }
                    }
                }
            });
        }
    }
}