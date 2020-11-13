package com.example.newcycle.Utils;


import android.content.Context;
import android.content.Intent;

import com.example.newcycle.Activities.AccountActivity;
import com.example.newcycle.Model.Data;

public class User {
    private  static User user = null;
    private SessionManager session;

    private User(){

    }

    public static User getInstance(){
        if(user == null){
            return new User();
        }
        return user;
    }
    public void init(Context context){
        session = new SessionManager(context);
    }

    public  boolean isSessionInit(){
        if(!session.has("init")){
            return false;
        }
        return session.getSessionBoolean("init");
    }

    public void initUserSession(Data data){
        if(data == null || data.getId() < 1){
            return;
        }

       if(session != null){
           session.setSession("init", true);
           session.setSession("id", data.getId());
           session.setSession("email", data.getEmail());
           session.setSession("fname", data.getFirstName());
           session.setSession("lname", data.getLastName());
           session.setSession("balance", data.getBalance());
           session.setSession("address", data.getAddress());
           session.setSession("phone", data.getPhoneNo());
       }
    }

    public Data loadDataFromSession(){
        if(!isSessionInit()){
            return null;
        }
        Data userData = new Data();
        userData.setId(session.getSessionInt("id"));
        userData.setEmail(session.getSessionString("email"));
        userData.setFirstName(session.getSessionString("fname"));
        userData.setLastName(session.getSessionString("lname"));
        userData.setBalance(session.getSessionInt("balance"));
        userData.setAddress(session.getSessionString("address"));
        userData.setPhoneNo(session.getSessionString("phone"));

        if(userData == null){
            return null;
        }

        return userData;
    }
    public boolean destroyUserSession(){
        return session.clearAllSession();
    }

    public void logOut(Context context){
        destroyUserSession();
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

}
