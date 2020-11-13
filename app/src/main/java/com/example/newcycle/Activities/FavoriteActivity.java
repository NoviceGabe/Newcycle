package com.example.newcycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newcycle.Adapters.RecyclerViewAdapter;
import com.example.newcycle.DB.AppDatabase;
import com.example.newcycle.Model.Data;
import com.example.newcycle.Model.Product;
import com.example.newcycle.R;
import com.example.newcycle.Utils.UI;
import com.example.newcycle.Utils.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager manager;
    private Data userData;
    private User user;
    private AppDatabase appDb;
    private List<Product> favorites;
    private  TextView itemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wish List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDb = AppDatabase.getAppDatabase(FavoriteActivity.this);
        user = User.getInstance();
        user.init(FavoriteActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(FavoriteActivity.this);
        recyclerView.setLayoutManager(manager);
        itemCount = findViewById(R.id.favorite_item_count);
        favorites = new ArrayList<>();

        if(!user.isSessionInit()){
            Intent intent = new Intent(FavoriteActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            userData = user.loadDataFromSession();
            if(userData.getId() > 0){

                favorites = appDb.favoriteDao().getAll(userData.getId());
                if(favorites != null){
                    itemCount.setText("Item ("+favorites.size()+")");
                    recyclerViewAdapter = new RecyclerViewAdapter(FavoriteActivity.this, favorites, 5);
                    recyclerViewAdapter.setTotal(itemCount);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI ui = new UI(FavoriteActivity.this);
        View view = findViewById(R.id.checkout);
        ui.loadUITheme(FavoriteActivity.this, toolbar, null, (RelativeLayout) view);
        if(favorites != null){
            itemCount.setText("Item ("+favorites.size()+")");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return true;
    }
}