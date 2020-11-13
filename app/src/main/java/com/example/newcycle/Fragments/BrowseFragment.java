package com.example.newcycle.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcycle.Activities.CartActivity;
import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.DB.DbHelper;
import com.example.newcycle.Utils.InfiniteScrollListener;
import com.example.newcycle.Interface.OnLoadMoreListener;
import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Adapters.RecyclerViewAdapter;
import com.example.newcycle.Utils.SessionManager;
import com.example.newcycle.Utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.newcycle.Utils.Utility.URL;


public class BrowseFragment extends Fragment implements OnLoadMoreListener {
    private RecyclerView recyclerView;
    private InfiniteScrollListener infiniteScrollListener;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DbHelper db;
    private AppDatabase appDb;
    private int productType = 1;
    private int page = 1;
    private final int LIMIT = 10;
    public static String search = "";
    private SessionManager sessionManager;
    private static int viewPage = 3;

    public static BrowseFragment newInstance(int pos){
        BrowseFragment browse = new BrowseFragment();
        browse.productType = pos+1;
        return browse;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHelper(getActivity());
        appDb = AppDatabase.getAppDatabase(getActivity());
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
        if(viewPage == 3){
            enableSync();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.main_content, container, false);
        mView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        recyclerView = mView.findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        infiniteScrollListener = new InfiniteScrollListener(getActivity(), manager,this);
        infiniteScrollListener.setLoaded();

        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(infiniteScrollListener);

        loadItemsFromDB(URL);

        return mView;
    }

    public List<Product> loadItemFromLocal(){
        List<Product> products;
        int offset = (page - 1) * LIMIT;
        if(!search.equals("")){
            products = appDb.productDao().getAll(String.valueOf(productType), LIMIT, offset, "%"+search+"%");
        }else{
            products = appDb.productDao().getAll(String.valueOf(productType), LIMIT, offset);
        }

       return products;
    }

    public void loadItemsFromDB(String URL){
        int offset = (page - 1) * LIMIT;
        db.setURL(URL, "get.php");
        db.table("product");
        db.param("type", String.valueOf(productType));
        if(!search.equals("")){
            db.param("like-name", "%"+search+"%");
        }
        db.param("limit", LIMIT+" OFFSET "+ offset);
        db.read(new OnReadClient<Product>() {
            @Override
            public void onSuccess(List<Product> products) {
                List<Product> localProducts = appDb.productDao().getAll();
                if(viewPage == 0){
                    disableSync();
                }

                if(isSyncEnabled()){
                    List<Product> newProducts =  filter(products, localProducts);
                    appDb.productDao().insertAll(newProducts);
                    viewPage--;
                }

                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), products, 1);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onError(String message) {
                List<Product> products;
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
                    case "no_data":
                        break;
                    case "request_timeout":
                        Toast.makeText(getActivity(), "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                        products = loadItemFromLocal();

                        if(products.size() > 0){
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), products, 1);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }
                        break;
                    case "no_internet_connection":
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                        products = loadItemFromLocal();

                        if(products.size() > 0){
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), products, 1);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }
                        break;
                    case "internal_server_error":
                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();

                        products = loadItemFromLocal();

                        if(products.size() > 0){
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), products, 1);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }
                        break;
                    default:
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        page++;
        recyclerViewAdapter.addNuLLData();
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int offset = (page - 1) * LIMIT;
                    db.setURL(URL, "get.php");
                    db.table("product");
                    db.param("type", String.valueOf(productType));
                    if(!search.equals("")){
                        db.param("like-name", "%"+search+"%");
                    }
                    db.param("limit", LIMIT+" OFFSET "+ offset);
                    db.disableProgressDialog();

                    db.read(new OnReadClient<Product>() {
                        @Override
                        public void onSuccess(List<Product> products) {
                            List<Product> localProducts = appDb.productDao().getAll();
                            List<Product> newProducts =  filter(products, localProducts);
                            appDb.productDao().insertAll(newProducts);

                            recyclerViewAdapter.removeNull();
                            recyclerViewAdapter.addData(products);
                            infiniteScrollListener.setLoaded();
                        }

                        @Override
                        public void onError(String message) {
                            List<Product> products;

                            switch (message){
                                case "connect_error":
                                    Toast.makeText(getActivity(), "Database connection error", Toast.LENGTH_SHORT).show();
                                    recyclerViewAdapter.removeNull();
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "no_table":
                                    Toast.makeText(getActivity(), "No table", Toast.LENGTH_SHORT).show();
                                    recyclerViewAdapter.removeNull();
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "query_error":
                                    Toast.makeText(getActivity(), "Query error", Toast.LENGTH_SHORT).show();
                                    recyclerViewAdapter.removeNull();
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "no_data":
                                    recyclerViewAdapter.removeNull();
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "request_timeout":
                                    Toast.makeText(getActivity(), "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                                    products = loadItemFromLocal();
                                    recyclerViewAdapter.removeNull();
                                    if(products.size() > 0){
                                        recyclerViewAdapter.addData(products);
                                    }
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "no_internet_connection":
                                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    products = loadItemFromLocal();
                                    recyclerViewAdapter.removeNull();
                                    if(products.size() > 0){
                                        recyclerViewAdapter.addData(products);
                                    }
                                    infiniteScrollListener.setLoaded();
                                    break;
                                case "internal_server_error":
                                    Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                                    products = loadItemFromLocal();
                                    recyclerViewAdapter.removeNull();
                                    if(products.size() > 0){
                                        recyclerViewAdapter.addData(products);
                                    }
                                    infiniteScrollListener.setLoaded();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    recyclerViewAdapter.removeNull();
                                    infiniteScrollListener.setLoaded();
                            }
                        }
                    });
                }
            }, 1000);

    }

    public boolean isSyncEnabled(){
        if(!sessionManager.has("sync")){
            return false;
        }
        return sessionManager.getSessionBoolean("sync");
    }

    public void enableSync(){
        sessionManager.setSession("sync", true);
    }

    public void disableSync(){
        sessionManager.setSession("sync", false);
    }

    public List<Product>  filter(List<Product> products1, List<Product> products2){
        List<Product> toBeRemove = new ArrayList<>();
        List<Product> copy = new ArrayList<>(products1);
        for(Product productA : copy){
            for(Product productB : products2){
                if(productA.getId() == productB.getId()){
                    toBeRemove.add(productA);
                }
            }
        }
        copy.removeAll(toBeRemove);
        return copy;
    }


}
