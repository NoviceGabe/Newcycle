package com.example.newcycle.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "checkout", foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId")

)

public class Checkout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "productId", index = true)
    private int productId;
    @ColumnInfo(name = "uId")
    private int userId;
    @ColumnInfo(name = "quantity")
    private int quantity;

    public Checkout(){}
    @Ignore
    public Checkout(int userId, int productId){
        this.userId = userId;
        this.productId = productId;
    }
    @Ignore
    public Checkout(int userId, int productId, int quantity){
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
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

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){return quantity;}
}
