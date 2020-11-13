package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Adapters.RecyclerViewAdapter;
import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Interface.DataHandlerObject;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Data;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.newcycle.Utils.Utility.URL;
import static com.example.newcycle.Utils.Utility.currencyFormatter;

public class CheckoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager manager;
    private Data userData;
    private User user;
    private DbHelper db;
    private AppDatabase appDb;
    private List<Product> checkout;
    public static boolean isPlaceOrder = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDb = AppDatabase.getAppDatabase(CheckoutActivity.this);
        db = new DbHelper(CheckoutActivity.this);

        user = User.getInstance();
        user.init(CheckoutActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(CheckoutActivity.this);
        recyclerView.setLayoutManager(manager);
        TextView tname = findViewById(R.id.name);
        TextView taddress = findViewById(R.id.address);
        TextView tcontact = findViewById(R.id.contact);
        TextView tsubTotal = findViewById(R.id.sub_total);
        TextView tshippingFee = findViewById(R.id.shipping_fee);
        TextView ttotal = findViewById(R.id.total);
        Button placeOrder = findViewById(R.id.place_order);

        if(!user.isSessionInit()){
            Intent intent = new Intent(CheckoutActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            userData = user.loadDataFromSession();
            if(userData.getId() > 0){
                String fullName = userData.getFirstName()+" "+userData.getLastName();
                tname.setText(fullName);

                if(userData.getAddress() != null && !userData.getAddress().equals("null")){
                    taddress.setText(userData.getAddress());
                }else{
                    taddress.setText("");
                }

                if(userData.getPhoneNo() != null && !userData.getPhoneNo().equals("null")){
                    tcontact.setText(userData.getPhoneNo());
                }else{
                    tcontact.setText("");
                }

                checkout = appDb.checkoutDao().getAll(userData.getId());
                List<Checkout> references = appDb.checkoutDao().getAllCheckout(userData.getId());

                for (Product product : checkout) {
                    for(Checkout reference : references){
                        if(product.getId() == reference.getProductId()){
                            product.setQuantity(reference.getQuantity());
                        }
                    }
                }

                recyclerViewAdapter = new RecyclerViewAdapter(CheckoutActivity.this, checkout, 3);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

                int subTotal = 0;
                int shippingFee = 0;
                int maxFee = 0;
                int total = 0;

                List<Integer> fee = new ArrayList<>();
                for(Product product : checkout){
                    subTotal += product.getPrice() * product.getQuantity();
                    if(!product.getType().equals("1")){
                        fee.add(product.getShippingFee());
                    }else{
                        shippingFee += product.getShippingFee();
                    }
                }

                if(subTotal > 0){
                    tsubTotal.setText("\u20B1"+currencyFormatter(subTotal));
                }else{
                    tsubTotal.setText("\u20B10.00");
                }
                if(fee.size() > 0){
                   maxFee = Collections.max(fee);
                    shippingFee += maxFee;
                }

                tshippingFee.setText("\u20B1"+currencyFormatter(shippingFee));
                if(subTotal > 0){
                    total = shippingFee + subTotal;
                }else{
                    total = 0;
                }
                ttotal.setText("\u20B1"+currencyFormatter(total));

                placeOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(userData.getAddress().equals("") || userData.getPhoneNo().equals("null")){
                            Toast.makeText(CheckoutActivity.this, "Address and Phone number is required", Toast.LENGTH_SHORT).show();
                        }else{
                            for(Product product : checkout){
                                Map<String, String> param  = new HashMap<>();
                                param.put("product_id", String.valueOf(product.getId()));
                                param.put("user_id", String.valueOf(userData.getId()));
                                param.put("quantity", String.valueOf(product.getQuantity()));
                                param.put("payment_method", "2");
                                db.param(param);
                            }

                            db.setURL(URL,"placeorder.php");
                            db.request(new DataHandlerObject<JSONObject>() {
                                @Override
                                public void getResult(JSONObject response) {
                                    if(db.hasKey(response, "message")){
                                        try {
                                            switch (response.getString("message")){
                                                case "connect_error":
                                                    Toast.makeText(CheckoutActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "query error":
                                                    Toast.makeText(CheckoutActivity.this, "Query error", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "no_table":
                                                    Toast.makeText(CheckoutActivity.this, "No specified table", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "insert_error":
                                                    Toast.makeText(CheckoutActivity.this, "Unable to place order", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "insert_success":

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // delete from cart
                                                            db.disableProgressDialog();
                                                            db.setURL(URL,"delete_cart.php");
                                                            db.table("cart");
                                                            db.param("user_id", String.valueOf(userData.getId()));
                                                            int i = 0;
                                                            for (Product product : checkout) {
                                                                db.param("product_id"+i, String.valueOf(product.getId()));
                                                                i++;
                                                            }

                                                            db.write(new OnWriteClient() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    isPlaceOrder = true;
                                                                    appDb.checkoutDao().deleteAll();
                                                                }

                                                                @Override
                                                                public void onError(String message) {
                                                                    switch (message){
                                                                        case "connect_error":
                                                                            Toast.makeText(CheckoutActivity.this, "Database connection error", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        case "no_table":
                                                                            Toast.makeText(CheckoutActivity.this, "No table", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        case "post_error":
                                                                            Toast.makeText(CheckoutActivity.this, "Post error", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        case "delete_error":
                                                                            break;
                                                                        case "request_timeout":
                                                                            Toast.makeText(CheckoutActivity.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        case "no_internet_connection":
                                                                            Toast.makeText(CheckoutActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        case "internal_server_error":
                                                                            Toast.makeText(CheckoutActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                                                            break;
                                                                        default:
                                                                            Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    },1000);
                                                    Intent intent = new Intent(CheckoutActivity.this, OrderActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                case "request_timeout":
                                                    Toast.makeText(CheckoutActivity.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "no_internet_connection":
                                                    Toast.makeText(CheckoutActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "internal_server_error":
                                                    Toast.makeText(CheckoutActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(CheckoutActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            Toast.makeText(CheckoutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI ui = new UI(CheckoutActivity.this);
        View view = findViewById(R.id.checkout);
        ui.loadUITheme(CheckoutActivity.this, toolbar, null, (RelativeLayout) view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                appDb.checkoutDao().deleteAll();
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        appDb.checkoutDao().deleteAll();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        appDb.checkoutDao().deleteAll();
        super.onDestroy();
    }
}