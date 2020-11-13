package com.example.newcycle.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Favorite;
import com.example.newcycle.Model.Product;

import java.util.List;
@Dao
public interface CheckoutDao {
    @Query("SELECT product.* FROM product INNER JOIN checkout ON checkout.productId = product.id WHERE checkout.uId = :userId")
    List<Product> getAll(int userId);

    @Query("SELECT * FROM checkout WHERE checkout.uId = :userId")
    List<Checkout> getAllCheckout(int userId);

    @Query("SELECT * FROM checkout WHERE uId = :userId AND productId = :productId")
    Checkout get(int userId, int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Checkout checkout);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Checkout> checkouts);

    @Delete


    void delete(Checkout checkout);

    @Query("DELETE FROM checkout")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM checkout")
    int count();

}
