package com.demo.navigationtest.ui.user.user_info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MainActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;

import org.json.JSONObject;

public class UserInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        FetchUserInfo fetchUserInfo = new FetchUserInfo();
        fetchUserInfo.execute();

    }

    class FetchUserInfo extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token",0);
            System.out.println(sp);
            String token = sp.getString("token",null);
            System.out.println(token);
            if(token == null){
                Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            String s = MyRequest.myGet("/users/fetchUserInfo",token);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }
}
