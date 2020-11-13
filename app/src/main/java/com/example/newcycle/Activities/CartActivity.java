package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.Model.Cart;
import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Data;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Adapters.RecyclerViewAdapter;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.newcycle.Utils.Utility.URL;

public class CartActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager manager;
    private Data userData;
    private User user;
    private DbHelper db;
    private List<Product> products;
    private List<Product> selectedItems;
    private AppDatabase appDb;
    private boolean checkoutFlag = false;
    private boolean deleteFlag = false;
    private TextView tcart;
    private TextView ttotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = findViewById(R.id.toolbar);
        Button checkout = findViewById(R.id.checkout);
        tcart = findViewById(R.id.cart_item_count);
        ttotal = findViewById(R.id.total);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(manager);

        recyclerView.setLayoutManager(manager);

        user = User.getInstance();
        user.init(CartActivity.this);
        db = new DbHelper(CartActivity.this);
        appDb = AppDatabase.getAppDatabase(CartActivity.this);
        products = new ArrayList<>();
        selectedItems = new ArrayList<>();

        if(!user.isSessionInit()){
            Intent intent = new Intent(CartActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            userData = user.loadDataFromSession();
            loadItemsFromDB(URL);
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(products.size() > 0){
                        List<Product> checkout = getAllSelectedItems();
                        if(checkout.size() < 1){
                            if(!checkoutFlag){
                                Toast.makeText(CartActivity.this, "Select a product", Toast.LENGTH_SHORT).show();
                                checkoutFlag = true;
                            }
                        }else{
                            addToCheckout(checkout);
                            int countCheckout = appDb.checkoutDao().count();
                            if(countCheckout > 0){
                                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(CartActivity.this, "Unable to proceed to checkout", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(CartActivity.this, "Empty cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI ui = new UI(CartActivity.this);
        View view = findViewById(R.id.cart);
        ui.loadUITheme(CartActivity.this, toolbar, null, (RelativeLayout) view);
        if(CheckoutActivity.isPlaceOrder){
            List<Product> toBeRemoved = getAllSelectedItems();
            int size = products.size();
            products.removeAll(toBeRemoved);
            recyclerViewAdapter.notifyItemRangeRemoved(0, size);
            tcart.setText("Item ("+products.size()+")");
            ttotal.setText("\u20B10.00");
            appDb.cartDao().delete(getId(toBeRemoved));
            CheckoutActivity.isPlaceOrder = false;
        }
    }

    @Override
    public void onBackPressed() {
        appDb.totalDao().deleteAll();
        RecyclerViewAdapter.isPressed = false;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        appDb.totalDao().deleteAll();
        RecyclerViewAdapter.isPressed = false;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                appDb.totalDao().deleteAll();
                RecyclerViewAdapter.isPressed = false;
                finish();
                break;
            case R.id.delete:

                final List<Product> toBeRemoved = getAllSelectedItems();
                if(toBeRemoved.size() < 1){
                    if(!deleteFlag){
                        Toast.makeText(CartActivity.this, "Select a product", Toast.LENGTH_SHORT).show();
                        deleteFlag = true;
                    }
                }else{
                    // delete from cart
                    db.setURL(URL,"delete_cart.php");
                    db.table("cart");
                    db.param("user_id", String.valueOf(userData.getId()));
                    int i = 0;
                    for (Product product : toBeRemoved) {
                        db.param("product_id"+i, String.valueOf(product.getId()));
                        i++;
                    }
                    db.write(new OnWriteClient() {
                        @Override
                        public void onSuccess() {
                            int size = products.size();
                            products.removeAll(toBeRemoved);
                            recyclerViewAdapter.notifyItemRangeRemoved(0, size);
                            tcart.setText("Item ("+products.size()+")");
                            ttotal.setText("\u20B10.00");

                            appDb.totalDao().deleteAll();
                            appDb.cartDao().delete(getId(toBeRemoved));
                            RecyclerViewAdapter.isPressed = false;
                        }

                        @Override
                        public void onError(String message) {
                            switch (message){
                                case "connect_error":
                                    Toast.makeText(CartActivity.this, "Database connection error", Toast.LENGTH_SHORT).show();
                                    break;
                                case "no_table":
                                    Toast.makeText(CartActivity.this, "No table", Toast.LENGTH_SHORT).show();
                                    break;
                                case "post_error":
                                    Toast.makeText(CartActivity.this, "Post error", Toast.LENGTH_SHORT).show();
                                    break;
                                case "delete_error":
                                    Toast.makeText(CartActivity.this, "Error on deleting cart", Toast.LENGTH_SHORT).show();
                                    break;
                                case "request_timeout":
                                    Toast.makeText(CartActivity.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                    break;
                                case "no_internet_connection":
                                    Toast.makeText(CartActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    break;
                                case "internal_server_error":
                                    Toast.makeText(CartActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
        return true;
    }

    public void loadItemsFromDB(String URL){
        if(userData.getId() > 0){
            db.setURL(URL,"cart.php");
            db.table("product");
            db.param("user_id", String.valueOf(userData.getId()));

            db.read(new OnReadClient<Product>() {
                @Override
                public void onSuccess(final List<Product> results) {
                    products = results;

                    int count = appDb.cartDao().count();
                    if(count == 0){
                        addToLocalCart(products);
                    }

                    if(products.size() > 0){
                        tcart.setText("Item ("+products.size()+")");
                        recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, products, 2);
                        recyclerViewAdapter.setTotal(ttotal);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(String message) {
                    switch (message){
                        case "connect_error":
                            Toast.makeText(CartActivity.this, "Database connection error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_table":
                            Toast.makeText(CartActivity.this, "No table", Toast.LENGTH_SHORT).show();
                            break;
                        case "query_error":
                            Toast.makeText(CartActivity.this, "Query error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_data":
                            break;
                        case "request_timeout":
                            Toast.makeText(CartActivity.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                            products =  appDb.cartDao().getAll(userData.getId());

                            if(products.size() > 0){
                                tcart.setText("Item ("+products.size()+")");
                                recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, products, 2);
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }
                            break;
                        case "no_internet_connection":
                            Toast.makeText(CartActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                            products =  appDb.cartDao().getAll(userData.getId());

                            if(products.size() > 0){
                                tcart.setText("Item ("+products.size()+")");
                                recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, products, 2);
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }
                            break;
                        case "internal_server_error":
                            Toast.makeText(CartActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();

                            products =  appDb.cartDao().getAll(userData.getId());

                            if(products.size() > 0){
                                tcart.setText("Item ("+products.size()+")");
                                recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, products, 2);
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }
                            break;
                        default:
                            Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            user.logOut(CartActivity.this);
        }
    }

    public List<Product> getAllSelectedItems(){
        List<Product> selected = new ArrayList<>();
        if(products != null){
            for (Product product : products) {
                if(product.isSelected()){
                    selected.add(product);
                }
            }
        }
        return selected;
    }

    public List<Integer> getId(List<Product> products){
        List<Integer> id = new ArrayList<>();
        for(Product product : products){
            id.add(product.getId());
        }
        return id;
    }

    public void addToLocalCart(List<Product> products){
        List<Cart> cart = new ArrayList<>();
        for (Product product: products) {
            cart.add(new Cart(userData.getId(), product.getId()));
        }

        appDb.cartDao().insertAll(cart);
    }

    public void addToCheckout(List<Product> products){
        List<Checkout> checkouts = new ArrayList<>();
        List<Cart> cart = appDb.cartDao().getAllCart(userData.getId());

        for (Product product : products) {
            for(Cart reference : cart){
                if(product.getId() == reference.getProductId()){
                    product.setQuantity(reference.getQuantity());
                }
            }
        }
        for (Product product : products) {
            checkouts.add(new Checkout(userData.getId(), product.getId(), product.getQuantity()));
        }
        appDb.checkoutDao().insertAll(checkouts);
    }



}
