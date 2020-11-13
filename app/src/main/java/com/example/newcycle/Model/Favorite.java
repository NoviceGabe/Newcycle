package com.example.newcycle.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite", foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = ForeignKey.CASCADE
))

public class Favorite {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "productId")
    private int productId;
    @ColumnInfo(name = "uId")
    private int userId;

    public Favorite(){

    }
    @Ignore
    public Favorite(int userId, int productId){
        this.userId = userId;
        this.productId = productId;
    }

    public void setProductId(int id){
        productId = id;
    }

    public void setUserId(int id){
        userId = id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getProductId(){return productId;}

    public int getUserId(){return userId;}

    public int getId(){return id;}
}
