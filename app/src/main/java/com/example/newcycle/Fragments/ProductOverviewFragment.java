package com.example.newcycle.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.R;
import com.example.newcycle.Adapters.CustomBaseAdapter;
import com.example.newcycle.Model.RatingsReviews;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import static com.example.newcycle.Utils.Utility.URL;
import static com.example.newcycle.Utils.Utility.currencyFormatter;


public class ProductOverviewFragment extends Fragment {
    private DbHelper db;
    private View mView;

    public ProductOverviewFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_overview, container, false);
        ImageView imageView = mView.findViewById(R.id.image);
        TextView tprice = mView.findViewById(R.id.price);
        TextView tstock = mView.findViewById(R.id.stock);
        TextView tname = mView.findViewById(R.id.name);
        TextView trating = mView.findViewById(R.id.rating);
        TextView tdelivery = mView.findViewById(R.id.delivery);
        TextView tdescription = mView.findViewById(R.id.description);
        TextView tratingLink = mView.findViewById(R.id.product_ratings_link);
        TextView tdescriptionLink = mView.findViewById(R.id.description_link);
        RatingBar ratingBar = mView.findViewById(R.id.rating_bar);

        String price = "Free";
        String shippingFee = "Free";

        if(ProductViewActivity.getProduct() != null){
            if(ProductViewActivity.getProduct().getImage() == null || ProductViewActivity.getProduct().getImage().isEmpty()){
                Picasso.get().load(R.drawable.dummy_image).
                        placeholder(R.drawable.progress_animation)
                        .into(imageView);
            }else{
                Picasso.get().load(ProductViewActivity.getProduct().getImage())
                        .error(R.drawable.dummy_image)
                        .placeholder(R.drawable.progress_animation)
                        .into(imageView);
            }

            String rating = Double.valueOf(ProductViewActivity.getProduct().getRating())+"/5";

            if(ProductViewActivity.getProduct().getPrice() > 0){
                price = "\u20B1"+currencyFormatter(ProductViewActivity.getProduct().getPrice());
            }
            if(ProductViewActivity.getProduct().getShippingFee() > 0){
                shippingFee = "\u20B1"+currencyFormatter(ProductViewActivity.getProduct().getShippingFee());
            }
            if(ProductViewActivity.getProduct().getTotal() > 1){
                tstock.setText(ProductViewActivity.getProduct().getTotal()+" items left");
            }else if(ProductViewActivity.getProduct().getTotal() == 1){
                tstock.setText("Only 1 item left");
            }

            if(ProductViewActivity.getProduct().getDescription() != null && !ProductViewActivity.getProduct().getDescription().isEmpty()){
                tdescription.setText(ProductViewActivity.getProduct().getDescription());
            }else{
                tdescription.setText("This product has no description.");
                tdescription.setGravity(Gravity.CENTER);
            }

            tprice.setText(price);
            tname.setText(ProductViewActivity.getProduct().getName());
            trating.setText(rating);
            ratingBar.setRating(ProductViewActivity.getProduct().getRating());
            tdelivery.setText(shippingFee);
        }

        tratingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductViewActivity)getActivity()).setCurrentItem(1, true);
            }
        });

        tdescriptionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductViewActivity)getActivity()).setCurrentItem(2, true);
            }
        });

        mView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        LinearLayout [] linearLayoutList = new LinearLayout[5];
        linearLayoutList[0]= mView.findViewById(R.id.div1);
        linearLayoutList[1]= mView.findViewById(R.id.div2);
        linearLayoutList[2]= mView.findViewById(R.id.div3);
        linearLayoutList[3]= mView.findViewById(R.id.div4);
        linearLayoutList[4]= mView.findViewById(R.id.div5);
        for (LinearLayout view : linearLayoutList) {
            view.setBackgroundColor(Color.parseColor("white"));
        }

        if(ProductViewActivity.getProduct() != null){
            LoadRatingsReviews(String.valueOf(ProductViewActivity.getProduct().getId()), URL);
        }


        return mView;
    }

    public static ProductOverviewFragment newInstance(){
        ProductOverviewFragment productOverviewFragment = new ProductOverviewFragment();
        return productOverviewFragment;
    }

    private void LoadRatingsReviews(String id, String URL){
        db.setURL(URL,"get.php");
        db.table("ratings_reviews");
        db.param("product_id", id);
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
                    for(int i = 0; i < 2; i++){
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
