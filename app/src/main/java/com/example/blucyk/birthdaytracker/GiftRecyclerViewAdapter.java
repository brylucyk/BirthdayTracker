package com.example.blucyk.birthdaytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder> {

    ArrayList<BirthdayGift> giftArrayList;
    Context context;
    View recyclerViewLayout;
    ViewHolder recyclerViewHolder;

    public GiftRecyclerViewAdapter(Context appContext, ArrayList<BirthdayGift> sessions){
        context = appContext;
        giftArrayList = sessions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Button deleteButton;
        public TextView giftItem;

        public ViewHolder(View v){
            super(v);
            deleteButton = (Button)v.findViewById(R.id.btnDeleteGiftItem);
            giftItem = (TextView)v.findViewById(R.id.textviewGiftItem);
        }
    }

    @Override
    public GiftRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        recyclerViewLayout = LayoutInflater.from(context).inflate(R.layout.giftrecyclerview_layout,parent,false);
        recyclerViewHolder = new ViewHolder(recyclerViewLayout);
        recyclerViewHolder.itemView.getLayoutParams().height=800;
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
//        holder.rVSessionDate.setText(trainingSessionArrayList.get(position).getDate());
//        holder.rVSessionTime.setText(trainingSessionArrayList.get(position).getTime());
//        holder.activity1.setText(
//                trainingSessionArrayList.get(position).getActivities().get(0).getActivityName());
//        holder.activity2.setText(
//                trainingSessionArrayList.get(position).getActivities().get(1).getActivityName());
//        holder.activity3.setText(
//                trainingSessionArrayList.get(position).getActivities().get(2).getActivityName());
//        holder.ratingBarActivity1.setRating(
//                trainingSessionArrayList.get(position).getActivities().get(0).getActivityRating());
//        holder.ratingBarActivity1.setEnabled(false);
//        holder.ratingBarActivity2.setRating(
//                trainingSessionArrayList.get(position).getActivities().get(1).getActivityRating());
//        holder.ratingBarActivity2.setEnabled(false);
//        holder.ratingBarActivity3.setRating(
//                trainingSessionArrayList.get(position).getActivities().get(2).getActivityRating());
//        holder.ratingBarActivity3.setEnabled(false);
//
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
        if(!giftArrayList.isEmpty()) {
            return giftArrayList.size();
        }
        else {
            return 0;
        }
    }
}
