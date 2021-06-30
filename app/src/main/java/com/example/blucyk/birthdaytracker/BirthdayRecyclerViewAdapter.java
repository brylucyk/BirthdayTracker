package com.example.blucyk.birthdaytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BirthdayRecyclerViewAdapter extends RecyclerView.Adapter<BirthdayRecyclerViewAdapter.ViewHolder> {

    ArrayList<Birthday> birthdayArrayList;
    Context context;
    View recyclerViewLayout;
    ViewHolder recyclerViewHolder;

    public BirthdayRecyclerViewAdapter(Context appContext, ArrayList<Birthday> birthdays){
        context = appContext;
        birthdayArrayList = birthdays;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Button deleteButton;
        public TextView bdayName;
        public TextView bdayDate;

        public ViewHolder(View v){
            super(v);
            bdayName = (TextView)v.findViewById(R.id.bdayName);
            bdayDate = (TextView)v.findViewById(R.id.bdayDate);
            deleteButton = (Button)v.findViewById(R.id.removeBdayBtn);
        }
    }

    @Override
    public BirthdayRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        recyclerViewLayout = LayoutInflater.from(context).inflate(R.layout.birthdaysrecyclerview_layout,
                parent,false);
        recyclerViewHolder = new ViewHolder(recyclerViewLayout);
        recyclerViewHolder.itemView.getLayoutParams().height=400;
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.bdayName.setText(birthdayArrayList.get(position).getBirthdayName());
        holder.bdayDate.setText(birthdayArrayList.get(position).toString());

//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int id = trainingSessionArrayList.get(position).getId();
//
//                HistoryActivity.deleteSession(id);
//            }
//        });
    }

    @Override
    public int getItemCount(){
        if(!birthdayArrayList.isEmpty()) {
            return birthdayArrayList.size();
        }
        else {
            return 0;
        }
    }
}
