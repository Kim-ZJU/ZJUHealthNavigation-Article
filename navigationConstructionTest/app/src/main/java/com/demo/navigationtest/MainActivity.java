package com.demo.navigationtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.navigationtest.ui.user.collection.UserCollectionActivity;
import com.demo.navigationtest.ui.user.health_info.UserHealthInfoActivity;
import com.demo.navigationtest.ui.user.help.UserHelpActivity;
import com.demo.navigationtest.ui.user.order.UserOrderActivity;
import com.demo.navigationtest.ui.user.user_info.UserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);

        this.getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_infoboard, R.id.navigation_user,R.id.navigation_appoint)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void userPageClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.collections:
                intent = new Intent(MainActivity.this, UserCollectionActivity.class);
                break;
            case R.id.orders:
                intent = new Intent(MainActivity.this, UserOrderActivity.class);
                break;
            case R.id.health_info:
                intent = new Intent(MainActivity.this, UserHealthInfoActivity.class);
                break;
            case R.id.helps:
                intent = new Intent(MainActivity.this,UserHelpActivity.class);
                break;
            case R.id.user_info:
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://10.0.2.2:3000/users/fetchUserInfo";
//
//                        System.out.println(url);
//
//                        OkHttpClient client = new OkHttpClient();
//                        try{
//                            Request request = new Request.Builder()
//                                    //.header("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdHVkZW50SWQiOiIzMTgwMTA1MDkxIiwiaWF0IjoxNjIyNjI4MTc1LCJleHAiOjE2MjI2MzUzNzV9.VokDcHW4HWr8fCJVRjCCGl9RWEAMHbjKHgA4YGAWeSQ")
//                                    .get().url(url).build();
//                            System.out.println(request);
//                            Response reponse = client.newCall(request).execute();
//
//                            System.out.println(reponse.body().string());
//
//                        }catch (Exception e){
//                            System.out.println(e);
//                        }
//                    }
//                }).start();
                intent = new Intent(MainActivity.this, UserInfoActivity.class);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

}
