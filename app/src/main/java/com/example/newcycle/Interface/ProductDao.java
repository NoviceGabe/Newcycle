package com.example.newcycle.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newcycle.Model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE type = :type LIMIT :limit OFFSET :offset")
    List<Product> getAll(String type, int limit, int offset);

    @Query("SELECT * FROM product WHERE type = :type AND name LIKE :search LIMIT :limit OFFSET :offset")
    List<Product> getAll(String type, int limit, int offset, String search);

    @Query("SELECT * FROM product WHERE id = :id")
    Product get(int id);

    @Insert
    void insert(Product product);

    @Insert
    void insertAll(List<Product> products);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM product")
    int count();

}
