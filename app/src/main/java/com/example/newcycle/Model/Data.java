package com.example.newcycle.Model;

public class Data {
    private int id = 0;
    private String email = "";
    private String fname = "";
    private String lname = "";
    private int balance = 0;
    private String address = "";
    private String phoneNo = "";

    public Data(int id, String email, String fname, String lname){
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
    }

    public Data(){}

    public void setFirstName(String fname){
        this.fname = fname;
    }

    public void setLastName(String lname){this.lname = lname;}

    public void setId(int id){
        this.id = id;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setPhoneNo(String no){
        this.phoneNo = no;
    }

    public String getFirstName(){
        return fname;
    }

    public String getLastName(){return lname;}

    public int getId(){
        return id;
    }

    public int getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    public String getAddress(){
        return address;
    }

    public String getPhoneNo(){return phoneNo;}
}
