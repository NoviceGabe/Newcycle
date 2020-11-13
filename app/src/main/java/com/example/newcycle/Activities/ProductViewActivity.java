package com.example.newcycle.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.Model.Cart;
import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Data;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Favorite;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Utils.SessionManager;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;
import com.example.newcycle.Adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import static com.example.newcycle.Utils.Utility.PRODUCT_VIEW;
import static com.example.newcycle.Utils.Utility.URL;

public class ProductViewActivity extends AppCompatActivity {
    private static ProductViewActivity productViewActivity;
    private static Product product;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private Data userData;
    private SessionManager session;
    private boolean flag = false;
    private User user;
    private DbHelper db;
    private AppDatabase appDb;
    private Product cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new String[]{"Overview", "Reviews", "Details"}, PRODUCT_VIEW);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());

        Menu menu = bottomNavigationView.getMenu();
        menu.setGroupCheckable(0, false, true);

        productViewActivity = this;
        session = new SessionManager(ProductViewActivity.this);
        user = User.getInstance();
        user.init(ProductViewActivity.this);
        userData = user.loadDataFromSession();
        db = new DbHelper(ProductViewActivity.this);

        appDb = AppDatabase.getAppDatabase(ProductViewActivity.this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = bottomNavigationView.getMenu();
                Intent intent;

                switch(item.getItemId()){
                    case R.id.store:
                        menu.setGroupCheckable(0, true, true);
                         intent = new Intent(ProductViewActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.buy:
                        menu.setGroupCheckable(0, true, true);
                        if(!user.isSessionInit()){
                            session.setSession("lastActivity","ProductView");
                            intent = new Intent(ProductViewActivity.this, AccountActivity.class);
                        }else{
                            Checkout checkout = new Checkout(userData.getId(), product.getId());
                            checkout.setQuantity(1);
                            appDb.checkoutDao().insert(checkout);
                            intent = new Intent(ProductViewActivity.this, CheckoutActivity.class);
                        }
                        startActivity(intent);
                        return true;
                    case R.id.add_to_cart:

                        if(!user.isSessionInit()){
                            session.setSession("lastActivity","ProductView");
                            intent = new Intent(ProductViewActivity.this, AccountActivity.class);
                            startActivity(intent);

                        }else{
                            // check if product already existing in the cart
                            Cart cart = appDb.cartDao().get(userData.getId(), product.getId());
                            if(cart != null){
                                menu.setGroupCheckable(0, false, true);
                                if(!flag){
                                    Toast.makeText(productViewActivity, "Product already added to cart", Toast.LENGTH_SHORT).show();
                                    flag = true;
                                }
                            }else{
                                menu.setGroupCheckable(0, true, true);
                                addToCart();
                            }

                        }

                        return true;
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = findViewById(R.id.product_view);
        UI ui = new UI(ProductViewActivity.this);
        ui.loadUITheme(ProductViewActivity.this, toolbar, bottomNavigationView, (RelativeLayout) view);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_view_menu, menu);
        
        MenuItem menuItemFavorite = menu.findItem(R.id.favorites);
        if(user.isSessionInit()){
            Favorite favorite = appDb.favoriteDao().get(userData.getId(), product.getId());
            if(favorite != null){
                Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_favorite_24);
                menuItemFavorite.setIcon(drawable);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.favorites:
                if(!user.isSessionInit()) {
                    session.setSession("lastActivity","ProductView");
                    Intent intent = new Intent(ProductViewActivity.this, AccountActivity.class);
                    startActivity(intent);
                }else{
                    Favorite favorite = appDb.favoriteDao().get(userData.getId(), product.getId());
                    // toggle off
                    if (favorite != null) {
                        appDb.favoriteDao().delete(favorite);
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp);
                        item.setIcon(drawable);

                    } else {
                        //toggle on
                        favorite = new Favorite(userData.getId(), product.getId());
                        appDb.favoriteDao().insert(favorite);
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_favorite_24);
                        item.setIcon(drawable);
                        Toast.makeText(productViewActivity, "Added to wish list", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
        }
        return true;
    }

    public void setCurrentItem(int item, boolean smoothScroll){
        viewPager.setCurrentItem(item, smoothScroll);
    }

    public static ProductViewActivity get(){
        return productViewActivity;
    }

    public static Product getProduct(){
        return product;
    }

    public static void setProduct(Product product){
        ProductViewActivity.product = product;
    }

    public void addToCart(){

        if(userData.getId() > 0){

            db.setURL(URL,"addtocart.php");
            db.param("user_id", String.valueOf(userData.getId()));
            db.param("product_id", String.valueOf(product.getId()));

            db.write(new OnWriteClient() {
                @Override
                public void onSuccess() {
                    Toast.makeText(productViewActivity, "Product added to cart", Toast.LENGTH_SHORT).show();
                    //add to local cart
                    Cart cart = new Cart(userData.getId(), product.getId(), 1);
                    appDb.cartDao().insert(cart);

                }

                @Override
                public void onError(String message) {
                    switch (message){
                        case "connect_error":
                            Toast.makeText(productViewActivity, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                            break;
                        case "query error":
                            Toast.makeText(productViewActivity, "Query error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_table":
                            Toast.makeText(productViewActivity, "No specified table", Toast.LENGTH_SHORT).show();
                            break;
                        case "non_unique_data_error":
                            Toast.makeText(productViewActivity, "Product already added to cart", Toast.LENGTH_SHORT).show();
                            Cart cart = appDb.cartDao().get(userData.getId(), product.getId());
                            if(cart == null ){
                                cart = new Cart(userData.getId(), product.getId());
                                cart.setQuantity(1);
                                appDb.cartDao().insert(cart);
                            }
                            break;
                        case "insert_error":
                            Toast.makeText(productViewActivity, "Unable to add to cart", Toast.LENGTH_SHORT).show();
                            break;
                        case "request_timeout":
                            Toast.makeText(productViewActivity, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_internet_connection":
                            Toast.makeText(productViewActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            break;
                        case "internal_server_error":
                            Toast.makeText(productViewActivity, "Internal Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(productViewActivity, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(productViewActivity, "Unable to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
