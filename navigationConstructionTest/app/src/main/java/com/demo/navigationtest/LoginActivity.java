package com.demo.navigationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
    }

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;

    public void login(View v) {
        String studentId = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask(studentId, pwd);
        loginAsyncTask.execute();
    }

    public void register_btn_clicked(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        private String studentId;
        private String password;

        public LoginAsyncTask(String studentId, String password) {
            this.password = password;
            this.studentId = studentId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> params = new HashMap<>();
            params.put("phoneNumber", studentId);
            params.put("password", password);
            String s = MyRequest.myLogin("/users/login", params);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                System.out.println(s);
                JSONObject result = new JSONObject(s);
                String code = result.getString("code");
                if (code.equals("200")) {
                    String token = result.getString("token");
                    String role = result.getString("role");

                    //保存token
                    SharedPreferences userToken = getSharedPreferences("token", 0);
                    SharedPreferences.Editor editor = userToken.edit();
                    editor.putString("token", token);
                    editor.putString("role", role);
                    editor.apply();

                    System.out.println(token);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (code.equals("404")) {
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (code.equals("400")) {
                    Toast.makeText(LoginActivity.this, "用户密码错误", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

