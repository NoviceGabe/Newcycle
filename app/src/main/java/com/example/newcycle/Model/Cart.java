package com.example.newcycle.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId")
)
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "productId", index = true)
    private int productId;
    @ColumnInfo(name = "uId")
    private int userId;
    @ColumnInfo(name = "quantity")
    private int quantity = 0;

    public Cart(){}
    @Ignore
    public Cart(int userId, int productId){
        this.userId = userId;
        this.productId = productId;
    }
    @Ignore
    public Cart(int userId, int productId, int quantity){
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

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){return quantity;}

    public int getProductId(){return productId;}

    public int getUserId(){return userId;}

    public int getId(){return id;}
}
