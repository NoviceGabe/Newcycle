package com.example.newcycle.Model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class Product{
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "quantity")
    private int quantity = 0;
    @ColumnInfo(name = "total")
    private int total;
    @ColumnInfo(name = "price")
    private int price;
    @ColumnInfo(name = "shippingFee")
    private int shippingFee;
    @ColumnInfo(name = "rating")
    private int rating;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "date")
    private int date;
    @ColumnInfo(name = "userId")
    private int userId;
    @ColumnInfo(name = "isSelected")
    private boolean isSelected = false;
    @Ignore
    private int position;
    @Ignore
    private String paymentMethod;
    @Ignore
    private String orderNo;
    @Ignore
    private String orderStatus;
    @Ignore
    private int dateOrdered;
    @Ignore
    private int dateShipped;
    @Ignore
    private int dateReceived;

    public Product(){
        userId = 0;
        id = 0;
        total = 0;
        price = 0;
        shippingFee = 0;
        rating = 0;
        name = "";
        description = "";
        image = "";
        type = "1";
        date = 0;
    }
    @Ignore
    public Product(Product product){
        userId = product.getUserId();
        id = product.getId();
        total = product.getTotal();
        price = product.getPrice();
        shippingFee = product.getShippingFee();
        rating = product.getRating();
        name = product.getName();
        description = product.getDescription();
        image = product.getImage();
        type = product.getType();
        date = product.getDate();
    }
    @Ignore
    public Product(int userId, int id, int total, String name, int price, int shippingFee, int rating, String description, String image, String type,  int date){
        this.userId = userId;
        this.id = id;
        this.total = total;
        this.name = name;
        this.price = price;
        this.shippingFee = shippingFee;
        this.rating = rating;
        this.description = description;
        this.image = image;
        this.type = type;
        this.date = date;
    }

    public  void setUserId(int userId){this.userId = userId;}

    public void setId(int id){
        this.id = id;
    }

    public void setTotal(int total){this.total = total;}

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public void setShippingFee(int fee){
        shippingFee = fee;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setDate(int date){
        this.date = date;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setOrderNo(String code){orderNo = code;}

    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod = paymentMethod;
    }

    public void setOrderStatus(String status){
        orderStatus = status;
    }

    public void setDateOrdered(int date){dateOrdered = date;}

    public void setDateShipped(int date){
        dateShipped = date;
    }

    public void setDateReceived(int date){
        dateReceived = date;
    }

    public int getUserId(){return userId;}
    public int getId() {
        return id;
    }
    public int getTotal(){return total;}
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public int getShippingFee(){return shippingFee;}
    public int getRating(){return rating;}
    public String getDescription(){
        return description;
    }
    public String getImage(){return image;}
    public String getType() { return type; }
    public int getDate(){return date;}
    public int getQuantity(){return quantity;}
    public boolean isSelected(){return isSelected;}
    public int getPosition(){return  position;}
    public String getPaymentMethod(){return paymentMethod;}
    public String getOrderNo(){return orderNo;}
    public String getOrderStatus(){return orderStatus;}
    public int getDateOrdered(){return dateOrdered;}
    public int getDateShipped(){return dateShipped;}
    public int getDateReceived(){return dateReceived;}
}
