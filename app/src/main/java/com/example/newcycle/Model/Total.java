package com.example.newcycle.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "total")
public class Total {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "price")
    private int price = 0;

    public Total(){}
    @Ignore
    public Total(int id, int price){
        this.id = id;
        this.price = price;
    }
    public void setId(int id){this.id = id;}
    public void setPrice(int price){this.price = price;}
    public int getId(){return id;}
    public int getPrice(){return price;}
}
