package com.example.newcycle.Interface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newcycle.Model.Cart;
import com.example.newcycle.Model.Checkout;
import com.example.newcycle.Model.Product;

import java.util.List;
@Dao
public interface CartDao {
    @Query("SELECT product.* FROM product INNER JOIN cart ON cart.productId = product.id WHERE cart.uId = :userId")
    List<Product> getAll(int userId);

    @Query("SELECT * FROM cart WHERE cart.uId = :userId")
    List<Cart> getAllCart(int userId);

    @Query("SELECT * FROM cart WHERE uId = :userId AND productId = :productId")
    Cart get(int userId, int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cart cart);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cart> carts);

    @Query("SELECT COUNT(*) FROM cart")
    int count();

    @Query("UPDATE cart SET quantity = :quantity WHERE productId = :productId")
    void updateQuantity(int quantity, int productId);

    @Query("DELETE FROM cart WHERE productId IN (:id)")
    void delete(List<Integer> id);

}
