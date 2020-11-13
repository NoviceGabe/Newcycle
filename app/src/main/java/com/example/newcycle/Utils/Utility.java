package com.example.newcycle.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.provider.MediaStore;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static final String URL = "https://new-cycle-bike-shop.000webhostapp.com/";
    //public static final String URL = "http://192.168.254.122/newcycle/"; // local db
    public static final int PRODUCT_BROWSE = 1;
    public static final int PRODUCT_VIEW = 2;
    public static final int ACCOUNT = 3;

    public static Uri getUriFromBitmap(Context context, Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage( context.getContentResolver(), bitmap, timeStamp, null);
        return Uri.parse(path);
    }

    public static String currencyFormatter(int quantity){
        DecimalFormat decimalFormat;
        decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(quantity);
    }

    public static void showSnackBar(View view, String message, int bgColor){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(bgColor);
        snackbar.show();
    }

    public static void showKeyboard(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void closeKeyboard(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static String getDate(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date  = DateFormat.format("MM/dd/yyyy", cal).toString();
        return date;
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCon  = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon  = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiCon != null && wifiCon.isConnected()) || (mobileCon != null && mobileCon.isConnected())){
            return true;
        }
        return false;
    }

}
