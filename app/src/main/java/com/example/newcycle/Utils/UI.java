package com.example.newcycle.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newcycle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class UI {
    private Context context;
    private int toolbarBgColor;
    private int bottomToolbarBgColor;
    private int layoutBgColor;
    private int statusBarBgColor;
    private int navBarBgColor;
    private int navDrawerItemIconColor;
    private int navDrawerBgColor;
    private int navDrawerHeaderBgColor;

    public UI(Context context){
        this.context = context;
        // default
        toolbarBgColor = context.getResources().getColor(R.color.colorPrimary);
        bottomToolbarBgColor =  context.getResources().getColor(R.color.colorPrimary);
        layoutBgColor = Color.WHITE;
        statusBarBgColor = context.getResources().getColor(R.color.colorPrimaryDark);
        navBarBgColor = Color.parseColor("#f2f2f2");
        navDrawerItemIconColor = 0;
        navDrawerBgColor = Color.WHITE;
        navDrawerHeaderBgColor = context.getResources().getColor(R.color.colorPrimary);
    }

    public UI(Context context, int toolbarBgColor, int bottomToolbarBgColor, int layoutBgColor, int statusBarBgColor, int navBarBgColor){
        this.context = context;
        this.toolbarBgColor = toolbarBgColor;
        this.bottomToolbarBgColor = bottomToolbarBgColor;
        this.layoutBgColor = layoutBgColor;
        this.statusBarBgColor = statusBarBgColor;
        this.navBarBgColor = navBarBgColor;
    }

    public void setToolbarBgColor(int toolbarBgColor){
        this.toolbarBgColor = toolbarBgColor;
    }

    public void setBottomToolbarBgColor(int bottomToolbarBgColor){
        this.bottomToolbarBgColor = bottomToolbarBgColor;
    }

    public void setLayoutBgColor(int layoutBgColor){
        this.layoutBgColor = layoutBgColor;
    }

    public void setStatusBarBgColor(int statusBarBgColor){
        this.statusBarBgColor = statusBarBgColor;
    }

    public void setNavBarBgColor(int navBarBgColor){
        this.navBarBgColor = navBarBgColor;
    }

    public void setNavDrawerItemIconColor(int navDrawerItemIconColor){
        this.navDrawerItemIconColor = navDrawerItemIconColor;
    }

    public void setNavDrawerBgColor(int navDrawerBgColor){
        this.navDrawerBgColor = navDrawerBgColor;
    }

    public void setNavDrawerHeaderBgColor(int navDrawerHeaderBgColor){
        this.navDrawerHeaderBgColor = navDrawerHeaderBgColor;
    }

    public int getToolbarBgColor(){
        return toolbarBgColor;
    }

    public int getBottomToolbarBgColor(){
        return bottomToolbarBgColor;
    }

    public int getLayoutBgColor(){
        return layoutBgColor;
    }

    public int getStatusBarBgColor(){
        return statusBarBgColor;
    }

    public int getNavBarBgColor(){
        return navBarBgColor;
    }

    public int getNavDrawerItemIconColor(){
        return navDrawerItemIconColor;
    }

    public int getNavDrawerBgColor(){
        return navDrawerBgColor;
    }

    public int getNavDrawerHeaderBgColor(){
        return navDrawerHeaderBgColor;
    }

    public void loadUITheme(Activity activity, Toolbar toolbar, BottomNavigationView bottomNavigationView, LinearLayout layout){
        Window window = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if(toolbar != null){
            toolbar.setBackgroundColor(toolbarBgColor);
        }

        if(bottomNavigationView != null){
            bottomNavigationView.setBackgroundColor(bottomToolbarBgColor);
        }

        if(layout != null){
            layout.setBackgroundColor(layoutBgColor);
        }

        if(window != null) {
            window.setStatusBarColor(statusBarBgColor);
            window.setNavigationBarColor(navBarBgColor);
        }
    }


    public void loadUITheme(Activity activity, Toolbar toolbar, BottomNavigationView bottomNavigationView, DrawerLayout layout){
        Window window = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if(toolbar != null){
            toolbar.setBackgroundColor(toolbarBgColor);
        }

        if(bottomNavigationView != null){
            bottomNavigationView.setBackgroundColor(bottomToolbarBgColor);
        }

        if(layout != null){
            layout.setBackgroundColor(layoutBgColor);
        }

        if(window != null) {
            window.setStatusBarColor(statusBarBgColor);
            window.setNavigationBarColor(navBarBgColor);
        }
    }


    public void loadUITheme(Activity activity, Toolbar toolbar, BottomNavigationView bottomNavigationView, RelativeLayout layout){
        Window window = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if(toolbar != null){
            toolbar.setBackgroundColor(toolbarBgColor);
        }

        if(bottomNavigationView != null){
            bottomNavigationView.setBackgroundColor(bottomToolbarBgColor);
        }

        if(layout != null){
            layout.setBackgroundColor(layoutBgColor);
        }

        if(window != null) {
            window.setStatusBarColor(statusBarBgColor);
            window.setNavigationBarColor(navBarBgColor);
        }
    }


    public void loadUITheme(Activity activity, Toolbar toolbar, BottomNavigationView bottomNavigationView, DrawerLayout layout,  NavigationView nView){
        View header = nView.getHeaderView(0);
        LinearLayout drawerHeader = (LinearLayout) header.findViewById(R.id.drawer_header);

        Window window = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        toolbar.setBackgroundColor(toolbarBgColor);
        if(bottomNavigationView != null){
            bottomNavigationView.setBackgroundColor(bottomToolbarBgColor);
        }

        if(layout != null){
            layout.setBackgroundColor(layoutBgColor);
        }

        if(nView != null){
            nView.setBackgroundColor(navDrawerBgColor);
            setNavMenuItemThemeColors(nView, navDrawerItemIconColor);

        }

        if(drawerHeader != null){
            drawerHeader.setBackgroundColor(navDrawerHeaderBgColor);
        }


        if(window != null) {
            window.setStatusBarColor(statusBarBgColor);
            window.setNavigationBarColor(navBarBgColor);
        }
    }

    private static void setNavMenuItemThemeColors(NavigationView nView, int color){

        int navDefaultTextColor = Color.parseColor("#202020");
        int navDefaultIconColor = Color.parseColor("#737373");

        ColorStateList navMenuTextList;
        ColorStateList navMenuIconList;

        if(color == 0){
            navMenuTextList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[]{
                            navDefaultTextColor,
                            navDefaultTextColor,
                            navDefaultTextColor,
                            navDefaultTextColor,
                            navDefaultTextColor
                    }
            );
            navMenuIconList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[]{
                            navDefaultIconColor,
                            navDefaultIconColor,
                            navDefaultIconColor,
                            navDefaultIconColor,
                            navDefaultIconColor
                    }
            );
        }else{
            navMenuTextList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[]{
                            color,
                            color,
                            color,
                            color,
                            color
                    }
            );
            navMenuIconList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[]{
                            color,
                            color,
                            color,
                            color,
                            color
                    }
            );
        }

        nView.setItemTextColor(navMenuTextList);
        nView.setItemIconTintList(navMenuIconList);
    }
}
