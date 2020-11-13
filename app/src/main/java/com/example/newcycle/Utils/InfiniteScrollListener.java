package com.example.newcycle.Utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcycle.Interface.OnLoadMoreListener;

public class InfiniteScrollListener extends RecyclerView.OnScrollListener {
    private final static int VISIBLE_THRESHOLD = 2;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading;
    private OnLoadMoreListener listener;
    private boolean pauseListening = false;

    private boolean END_OF_FEED_ADDED = false;
    private int NUM_LOAD_ITEMS = 10;
    private Context context;

    public InfiniteScrollListener(Context context, LinearLayoutManager linearLayoutManager, OnLoadMoreListener listener){
        this.linearLayoutManager = linearLayoutManager;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dx == 0 && dy == 0){
            return;
        }

        int totalItemCount = linearLayoutManager.getItemCount();
        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        if(!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && totalItemCount != 0 && !END_OF_FEED_ADDED && !pauseListening){
            if(listener != null){
                listener.onLoadMore();
            }
            loading = true;
        }
    }

    public void setLoaded(){
        loading = false;
    }

    public void addEndOfRequests(){
        this.END_OF_FEED_ADDED = true;
    }

    public void pauseScrollingListener(boolean pauseListening){
        this.pauseListening = pauseListening;
    }
}
