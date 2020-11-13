package com.example.newcycle.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newcycle.Model.Favorite;
import com.example.newcycle.Model.Product;

import java.util.List;
@Dao
public interface FavoriteDao {
    @Query("SELECT product.* FROM product "+
            "INNER JOIN favorite ON favorite.productId = product.id WHERE favorite.uId = :userId")
    List<Product> getAll(int userId);

    @Query("SELECT * FROM favorite WHERE uId = :userId AND productId = :productId")
    Favorite get(int userId, int productId);

    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite product);

    @Query("DELETE FROM favorite WHERE productId = :productId")
    void delete(int productId);

    @Query("SELECT COUNT(*) FROM favorite")
    int count();
}
