package com.example.newcycle.Interface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.newcycle.Model.Total;

import java.util.List;

@Dao
public interface TotalDao {
    @Insert
    void insertAll(List<Total> total);
    @Insert
    void insert(Total total);
    @Query("SELECT * FROM total WHERE id = :id")
    Total get(int id);
    @Query("SELECT SUM(price) FROM total")
    int sum();
    @Query("DELETE FROM total WHERE id = :id")
    void delete(int id);
    @Query("DELETE FROM total")
    void deleteAll();
    @Query("SELECT COUNT(*) FROM total")
    int countTotal();
    @Query("UPDATE total SET price = :price WHERE id = :id")
    void update(int price, int id);
}
