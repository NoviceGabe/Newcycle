package com.example.newcycle.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.newcycle.Model.RatingsReviews;
import com.example.newcycle.R;

import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapter extends BaseAdapter {
    private List<RatingsReviews> mData;
    private Context mContext;

    public CustomBaseAdapter(List<RatingsReviews> data, Context context){
        mData  = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        RatingsReviews currentItem = (RatingsReviews) getItem(position);
        TextView customerName = view.findViewById(R.id.customer_name);
        customerName.setText(currentItem.getName());
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        ratingBar.setRating(currentItem.getRating());
        TextView date = view.findViewById(R.id.date);
        date.setText(currentItem.getDate());
        TextView description = view.findViewById(R.id.description);
        description.setText(currentItem.getReview());

        return view;
    }
}
