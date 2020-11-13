package com.example.newcycle.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.newcycle.Fragments.BrowseFragment;
import com.example.newcycle.Fragments.LoginFragment;
import com.example.newcycle.Fragments.ProductDetailsFragment;
import com.example.newcycle.Fragments.ProductOverviewFragment;
import com.example.newcycle.Fragments.ProductReviewFragment;
import com.example.newcycle.Fragments.RegisterFragment;
import com.example.newcycle.Model.Product;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String [] title;
    private int param;
    private Product product;
    private FragmentManager fm;

    public ViewPagerAdapter(FragmentManager fm, String [] title, final int PARAM){
        super(fm);
        this.fm = fm;
        this.title = title;
        this.param = PARAM;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(param == 1){
            return BrowseFragment.newInstance(position);
        }else if(param == 2){
            switch (position){
                case 1:
                    return ProductReviewFragment.newInstance();
                case 2:
                    return ProductDetailsFragment.newInstance();
                default:
                    return ProductOverviewFragment.newInstance();
            }

        }else{
           switch (position){
               case 1:
                   return RegisterFragment.newInstance();
                   default:
                       return LoginFragment.newInstance();
           }
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

}
