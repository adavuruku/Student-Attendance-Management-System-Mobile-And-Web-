package com.sherif.atbuattendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sherif146 on 11/01/2018.
 */

public class phoneAdapter extends RecyclerView.Adapter<phoneAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<phoneList> phonelist;
    private RecyclerView recyclerView;
  private  OnItemClickListener mlistener;
  //  private RecyclerItemClickListener listener;
    private Context activity;

    public phoneAdapter(List<phoneList> contacts, Context context,OnItemClickListener listener){
        this.activity = context;
        this.mlistener = listener;
        this.inflater = LayoutInflater.from(context);
        this.phonelist = contacts;
    }
    public interface OnItemClickListener{
        void onCallClick(View v, int position);
    }
    public void setOnitemClickListener(OnItemClickListener listener){mlistener = listener;}
    @Override
    public phoneAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_courses,parent,false);
        phoneAdapter.RecyclerHolder holder= new phoneAdapter.RecyclerHolder(view,mlistener);
        return holder;
    }
    @Override
    public void onBindViewHolder(phoneAdapter.RecyclerHolder holder, int position) {
        phoneList contact = phonelist.get(position);
        holder.unit.setText(("Course Unit : " + contact.getUnit() + " Unit"));
        holder.code.setText(("Course Code : " + contact.getCode()));
        holder.title.setText(("Course Title : " + contact.getTitle()));
    }

    @Override
    public int getItemCount() {
        return phonelist.size();
    }

    //create the holder class
    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView unit,title,code;
        Button proceed;
        public RecyclerHolder(final View itemView,final OnItemClickListener listener) {
            super(itemView);
            unit = (TextView) itemView.findViewById(R.id.unit);
            proceed = (Button) itemView.findViewById(R.id.proceed);
            title = (TextView) itemView.findViewById(R.id.title);
            code = (TextView) itemView.findViewById(R.id.code);

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