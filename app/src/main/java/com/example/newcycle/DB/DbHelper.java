package com.example.newcycle.DB;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcycle.Interface.DataHandlerObject;
import com.example.newcycle.Interface.DataHandlerString;
import com.example.newcycle.Interface.DbInterface;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Interface.OnWriteClient;
import com.example.newcycle.Model.Product;
import com.example.newcycle.Model.RatingsReviews;
import com.example.newcycle.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.newcycle.Utils.Utility.URL;

public class DbHelper implements DbInterface {
    private String URL = "";
    private Context context;
    private Map<String, String> params;
    public List<Map<String, String>> listParam;
    private String dialogTitle = "Loading..";
    public static String table;
    public String file;
    private boolean enableProgressDialog = true;

    public DbHelper(Context context){
        this.context = context;
        params = new HashMap<>();
        listParam = new ArrayList<Map<String, String>>();
    }

    public void table(String name){
        param("table", name);
        table = name;
    }

    public void disableProgressDialog(){
        enableProgressDialog = false;
    }

    public void param(String key, String value){
        params.put(key, value);
    }

    public void param(Map<String, String> param){
        listParam.add(param);
    }

    public void paramShow(){
        Iterator iterator = params.keySet().iterator();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            String value = (String) params.get(key);
            Toast.makeText(context, "key: " + key+ " value: " + value, Toast.LENGTH_SHORT).show();
        }
    }

    public void setURL(String hostname, String file){
        URL =  hostname+file;
        this.file = file;
    }

    public void setDialogTitle(String title){
        dialogTitle = title;
    }

    public void request(final DataHandlerObject dh){
        if(URL.isEmpty()){
            Toast.makeText(context, "Empty URL", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(context);
        if(enableProgressDialog){
            progressDialog.setMessage(dialogTitle);
            progressDialog.show();
        }

        JSONArray jsonArray = new JSONArray();
        for(Map<String, String> data : listParam){
            JSONObject obj = new JSONObject(data);
            jsonArray.put(obj);
        }

        JSONObject param = new JSONObject();
        try {
            param.put("order", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dh.getResult(response);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    Toast.makeText(context, "Request Timeout", Toast.LENGTH_SHORT).show();
                }else if(error instanceof NoConnectionError){
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else if(error instanceof AuthFailureError){
                    Toast.makeText(context, "Authentication Failure", Toast.LENGTH_SHORT).show();
                }else if(error instanceof ServerError){
                    Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }else if(error instanceof NetworkError){
                    Toast.makeText(context, "Can't Connect to Server", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void request(final DataHandlerString dh){
        if(URL.isEmpty()){
            Toast.makeText(context, "Empty URL", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        if(enableProgressDialog){
            progressDialog.setMessage(dialogTitle);
            progressDialog.show();
        }

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dh.getResult(response);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    if(error instanceof TimeoutError){
                        dh.getResult("{success:0, message:request_timeout}");
                        //Toast.makeText(context, "Request Timeout", Toast.LENGTH_SHORT).show();
                    }else if(error instanceof NoConnectionError){
                        dh.getResult("{success:0, message:no_internet_connection}");
                        //Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }else if(error instanceof AuthFailureError){
                        dh.getResult("{success:0, message:authentication_failure}");
                        //Toast.makeText(context, "Authentication Failure", Toast.LENGTH_SHORT).show();
                    }else if(error instanceof ServerError){
                        dh.getResult("{success:0, message:internal_server_error}");
                        //Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }else if(error instanceof NetworkError){
                        dh.getResult("{success:0, message:network_error}");
                        //Toast.makeText(context, "Can't Connect to Server", Toast.LENGTH_SHORT).show();
                    }else{
                        dh.getResult(error.getMessage());
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void write(final OnWriteClient client) {
        request(new DataHandlerString() {
            @Override
            public void getResult(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String success = obj.getString("success");
                    String message = obj.getString("message");

                    if(success.equals("1")){
                        client.onSuccess();
                    }else{
                        client.onError(message);
                    }

                } catch (JSONException e) {
                    client.onError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void read(final OnReadClient client) {
        request(new DataHandlerString() {
            @Override
            public void getResult(String response) {
                switch (table){
                    case "product":
                        ArrayList<Product> products = new ArrayList<>();
                        // if json response is an array
                        try {
                            JSONArray jsonData = new JSONArray(response);
                            for(int i =0; i < jsonData.length(); i++){
                                JSONObject obj = jsonData.getJSONObject(i);
                                Product product = new Product();

                                if(hasKey(obj, "id")){
                                    product.setId(Integer.parseInt(obj.getString("id")));
                                }
                                if(hasKey(obj, "stock")){
                                    product.setTotal(obj.getInt("stock"));
                                }
                                if(hasKey(obj, "name")){
                                    product.setName(obj.getString("name"));
                                }
                                if(hasKey(obj, "price")){
                                    product.setPrice(Integer.parseInt(obj.getString("price")));
                                }
                                if(hasKey(obj, "description")){
                                   product.setDescription(obj.getString("description"));
                                }
                                if(hasKey(obj, "rating")){
                                    product.setRating(obj.getInt("rating"));
                                }
                                if(hasKey(obj, "shipping_fee")){
                                    product.setShippingFee(obj.getInt("shipping_fee"));
                                }
                                if(hasKey(obj, "image")){
                                    product.setImage(obj.getString("image"));
                                }
                                if(hasKey(obj, "type")){
                                    product.setType(obj.getString("type"));
                                }
                                if(hasKey(obj, "date")){
                                    product.setDate(obj.getInt("date"));
                                }
                                if(hasKey(obj, "quantity")){
                                    product.setQuantity(obj.getInt("quantity"));
                                }
                                if(hasKey(obj, "code")){
                                    product.setOrderNo(obj.getString("code"));
                                }
                                if(hasKey(obj, "payment_method")){
                                    product.setPaymentMethod(obj.getString("payment_method"));
                                }
                                if(hasKey(obj, "order_status")){
                                    product.setOrderStatus(obj.getString("order_status"));
                                }
                                if(hasKey(obj, "date_ordered")){
                                    product.setDateOrdered(obj.getInt("date_ordered"));
                                }
                                if(hasKey(obj, "date_shipped") && !obj.isNull("date_shipped")){
                                    product.setDateShipped(obj.getInt("date_shipped"));
                                }
                                if(hasKey(obj, "date_received")  && !obj.isNull("date_received")){
                                    product.setDateReceived(obj.getInt("date_received"));
                                }
                                products.add(product);
                            }

                            if(products.size() > 0){
                                client.onSuccess(products);
                            }else{
                                JSONObject obj = new JSONObject(response);
                                String message = obj.getString("message");
                                client.onError(message);
                            }

                        } catch (JSONException ex1) {
                            try{
                                JSONObject obj = new JSONObject(response);
                                String message = obj.getString("message");
                                client.onError(message);
                            }catch (JSONException ex2){
                                client.onError(ex2.getMessage());
                            }

                        }
                        break;
                    case "ratings_reviews":
                        ArrayList<RatingsReviews> ratingsReviews = new ArrayList<>();
                        String rrName, review, rrDate;
                        int rrRating;
                        try {
                            JSONArray jsonData = new JSONArray(response);
                            for(int i =0; i < jsonData.length(); i++){
                                JSONObject obj = jsonData.getJSONObject(i);
                                rrRating = Integer.parseInt(obj.getString("rating"));
                                rrName = obj.getString("customer_name");
                                review = obj.getString("review");
                                rrDate = obj.getString("date");

                                ratingsReviews.add(new RatingsReviews(rrName, rrRating, review, rrDate));
                            }
                            if(ratingsReviews.size() > 0){
                                   /* Arrays.sort(ratingsReviews.toArray());
                                    while(ratingsReviews.size() > 2){
                                        ratingsReviews.remove(ratingsReviews.size()-1);
                                    }*/

                                client.onSuccess(ratingsReviews);
                            }else{
                                JSONObject obj = new JSONObject(response);
                                String message = obj.getString("message");
                                client.onError(message);
                            }
                        }catch (JSONException ex1) {
                            try{
                                JSONObject obj = new JSONObject(response);
                                String message = obj.getString("message");
                                client.onError(message);
                            }catch (JSONException ex) {
                                client.onError(ex.getMessage());
                            }

                        }
                }

            }
        });

    }

    public void paramClear(){
        params.clear();
    }

    public boolean hasKey(JSONObject jsonObject, String key){
        return jsonObject != null && jsonObject.has(key);
    }

}