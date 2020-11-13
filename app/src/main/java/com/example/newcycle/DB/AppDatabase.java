package com.example.newcycle.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newcycle.Interface.CartDao;
import com.example.newcycle.Interface.CheckoutDao;
import com.example.newcycle.Interface.FavoriteDao;
import com.example.newcycle.Interface.ProductDao;
import com.example.newcycle.Interface.TotalDao;
import com.example.newcycle.Model.Cart;
import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Favorite;
import com.example.newcycle.Model.Product;
import com.example.newcycle.Model.Total;

@Database(entities = {Product.class, Total.class, Cart.class, Favorite.class, Checkout.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract ProductDao productDao();
    public abstract CartDao cartDao();
    public abstract FavoriteDao favoriteDao();
    public abstract CheckoutDao checkoutDao();
    public abstract TotalDao totalDao();
    public static AppDatabase getAppDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "newcycle")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }
}
