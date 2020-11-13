package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.Fragments.BrowseFragment;
import com.example.newcycle.Model.Data;
import com.example.newcycle.R;
import com.example.newcycle.Utils.SessionManager;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;
import com.example.newcycle.Adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import static com.example.newcycle.Utils.Utility.PRODUCT_BROWSE;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private AppDatabase appDb;
    private User user;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Cycle Bike Shop");

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int pos = 0;
                switch (item.getItemId()) {
                    case R.id.order:
                        Intent order = new Intent(MainActivity.this, OrderActivity.class);
                        startActivity(order);
                        break;
                    case R.id.bikes_category:
                        pos = 0;
                        break;
                    case R.id.bike_parts:
                        pos = 1;
                        break;
                    case R.id.bike_accessories:
                        pos = 2;
                        break;
                    case R.id.login:
                        if(!user.isSessionInit()){
                            Intent login = new Intent(MainActivity.this, AccountActivity.class);
                            startActivity(login);
                        }
                        break;
                    case R.id.contact:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final SpannableString spannableString = new SpannableString("+639123456789");
                        Linkify.addLinks(spannableString, Linkify.ALL);
                        builder.setTitle("Contact Us");
                        builder.setMessage("Contact Us at "+spannableString)
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                }

                drawerLayout.closeDrawers();
                viewPager.setCurrentItem(pos);
                return true;
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new String[]{"Category", "Parts", "Accessories"}, PRODUCT_BROWSE);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        user = User.getInstance();
        user.init(MainActivity.this);
        session = new SessionManager(MainActivity.this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.store:
                        BrowseFragment.search = "";
                        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new String[]{"Category", "Parts", "Accessories"}, PRODUCT_BROWSE);
                        viewPager.setAdapter(viewPagerAdapter);
                        return true;
                    case R.id.cart:
                        if(!user.isSessionInit()){
                            session.setSession("lastActivity","Cart");
                            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, CartActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.favorites:
                        if(!user.isSessionInit()){
                            session.setSession("lastActivity","Favorite");
                            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.account:
                        if(!user.isSessionInit()){
                            session.setSession("lastActivity","UserProfile");
                            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                        }

                }
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.store);
        Data data = user.loadDataFromSession();
        appDb = AppDatabase.getAppDatabase(MainActivity.this);
        
        if(data != null){
            int count = appDb.cartDao().count();
            if(count > 0){
                bottomNavigationView.getOrCreateBadge(R.id.cart).setNumber(count);
            }else{
                bottomNavigationView.getOrCreateBadge(R.id.cart).setVisible(false);
            }
        }
        UI ui = new UI(MainActivity.this);
        ui.loadUITheme(MainActivity.this, toolbar, bottomNavigationView, drawerLayout, navigationView);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        MenuItem menuItem = menu.findItem(R.id.search_bar);
        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new String[]{"Category", "Parts", "Accessories"}, PRODUCT_BROWSE);
                viewPager.setAdapter(viewPagerAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // when input text change
                BrowseFragment.search = newText;
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // when edit text collapse
                // clear the input to reload the fragment
                searchView.setQuery("", false);
                return true;
            }
        });

        return true;
    }


}
