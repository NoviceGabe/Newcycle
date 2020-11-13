package com.example.newcycle.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.R;
import com.example.newcycle.Adapters.CustomBaseAdapter;
import com.example.newcycle.Model.RatingsReviews;

import java.util.List;

import static com.example.newcycle.Utils.Utility.URL;


public class ProductReviewFragment extends Fragment {
    private DbHelper db;
    private ListView listView;
    private View mView;

    public ProductReviewFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_review, container, false);
        LoadRatingsReviews();
        return mView;
    }

    public static ProductReviewFragment newInstance(){
        ProductReviewFragment productReviewFragment = new ProductReviewFragment();
        return productReviewFragment;
    }

    private void LoadRatingsReviews(){
        if(ProductViewActivity.getProduct() != null && ProductViewActivity.getProduct().getId() > 0){
            db.setURL(URL,"get.php");
            db.table("ratings_reviews");
            db.param("product_id", String.valueOf(ProductViewActivity.getProduct().getId()));
            db.param("order", "rating DESC");
            db.disableProgressDialog();

            db.read(new OnReadClient<RatingsReviews>() {
                @Override
                public void onSuccess(List<RatingsReviews> ratingsReviews) {
                    if(ratingsReviews.size() > 0){
                        TextView rrLabel = mView.findViewById(R.id.product_ratings_label);
                        String trrLabel = "Ratings & Reviews ("+ratingsReviews.size()+")";
                        rrLabel.setText(trrLabel);
                        CustomBaseAdapter ratingReviewsBaseAdapter = new CustomBaseAdapter(ratingsReviews, getActivity());
                        //listView.setAdapter(ratingReviewsBaseAdapter);

                        LinearLayout list = mView.findViewById(R.id.list_container);
                        for(int i = 0; i < ratingsReviews.size(); i++){
                            View view = ratingReviewsBaseAdapter.getView(i, null, list);
                            list.addView(view);
                        }
                        TextView rrDefaultText =  mView.findViewById(R.id.product_ratings_empty);
                        rrDefaultText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String message) {
                    switch (message){
                        case "connect_error":
                            Toast.makeText(getActivity(), "Database connection error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_table":
                            Toast.makeText(getActivity(), "No table", Toast.LENGTH_SHORT).show();
                            break;
                        case "query_error":
                            Toast.makeText(getActivity(), "Query error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }

}
