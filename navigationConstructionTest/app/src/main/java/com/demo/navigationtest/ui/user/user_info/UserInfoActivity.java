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
import com.demo.navigationtest.ui.user.health_info.UserInfoCardComponent;

import org.json.JSONObject;

public class UserInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        FetchUserInfo fetchUserInfo = new FetchUserInfo();
        fetchUserInfo.execute();

        String role = getSharedPreferences("token",0).getString("role",null);
        System.out.println(role);

    }

    class FetchUserInfo extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            if(token == null){
                Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            String s = MyRequest.myGet("/users/fetchUserInfo",token);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject result = new JSONObject(s);
                String code = result.getString("code");
                if(code.equals("0")){
                    JSONObject user = result.getJSONObject("data");

                    UserInfoCardComponent studentIdComponent = (UserInfoCardComponent) findViewById(R.id.user_info_studentId);
                    studentIdComponent.setContent(user.getString("studentId"));
                }
            }catch (Exception e){
                System.out.println(e);
            }

            System.out.println(s);
        }
    }
}
