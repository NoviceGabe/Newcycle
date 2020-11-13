package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Adapters.RecyclerViewAdapter;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Model.Data;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.newcycle.Utils.Utility.URL;

public class OrderActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager manager;
    private Data userData;
    private User user;
    private DbHelper db;
    private TextView orderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        toolbar = findViewById(R.id.toolbar);
        orderCount = findViewById(R.id.order_item_count);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(OrderActivity.this);
        recyclerView.setLayoutManager(manager);

        recyclerView.setLayoutManager(manager);

        user = User.getInstance();
        user.init(OrderActivity.this);
        db = new DbHelper(OrderActivity.this);

        if(!user.isSessionInit()){
            Intent intent = new Intent(OrderActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            userData = user.loadDataFromSession();
            LoadItems(URL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI ui = new UI(OrderActivity.this);
        View view = findViewById(R.id.order);
        ui.loadUITheme(OrderActivity.this, toolbar, null, (RelativeLayout) view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return true;
    }

    public void LoadItems(String URL){
        if(userData.getId() > 0){
            db.setURL(URL,"order.php");
            db.table("product");
            db.param("user_id", String.valueOf(userData.getId()));

            db.read(new OnReadClient<Product>() {
                @Override
                public void onSuccess(List<Product> products) {
                    if(products != null){
                        recyclerViewAdapter = new RecyclerViewAdapter(OrderActivity.this, products, 4);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                        orderCount.setText("Item ("+products.size()+")");
                    }
                }

                @Override
                public void onError(String message) {
                    switch (message){
                        case "connect_error":
                            Toast.makeText(OrderActivity.this, "Database connection error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_table":
                            Toast.makeText(OrderActivity.this, "No table", Toast.LENGTH_SHORT).show();
                            break;
                        case "query_error":
                            Toast.makeText(OrderActivity.this, "Query error", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_data":

                            break;
                        case "request_timeout":
                            Toast.makeText(OrderActivity.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                            break;
                        case "no_internet_connection":
                            Toast.makeText(OrderActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            break;
                        case "internal_server_error":
                            Toast.makeText(OrderActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(OrderActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            user.logOut(OrderActivity.this);
        }
    }



}