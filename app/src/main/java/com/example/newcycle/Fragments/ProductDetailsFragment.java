package com.example.newcycle.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newcycle.Activities.ProductViewActivity;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;


public class ProductDetailsFragment extends Fragment {
    private Product product;

    public ProductDetailsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_product_details, container, false);
        TextView tdescription = mView.findViewById(R.id.description);
        if(ProductViewActivity.getProduct() != null){
            tdescription.setText(ProductViewActivity.getProduct().getDescription());
        }
        return mView;
    }


    public static ProductDetailsFragment newInstance(){
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        return productDetailsFragment;
    }


}
