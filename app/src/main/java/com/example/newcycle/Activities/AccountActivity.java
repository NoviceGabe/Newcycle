package com.example.newcycle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.newcycle.R;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import static com.example.newcycle.Utils.Utility.ACCOUNT;

public class AccountActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new String[]{"Login", "Sign Up"}, ACCOUNT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = findViewById(R.id.account);
        UI ui = new UI(AccountActivity.this);
        ui.loadUITheme(AccountActivity.this, null, null, (RelativeLayout) view);
        invalidateOptionsMenu();
    }

    public void setCurrentItem(int item, boolean smoothScroll){
        viewPager.setCurrentItem(item, smoothScroll);
    }
}
