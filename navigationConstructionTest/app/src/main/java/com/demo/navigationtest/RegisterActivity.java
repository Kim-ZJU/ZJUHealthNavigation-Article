package com.demo.navigationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText age;
    EditText studentId;
    RadioGroup sex;
    Button register;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String agestr = age.getText().toString().trim();
                String student = studentId.getText().toString().trim();
                System.out.println(agestr);
                String sexstr = ((RadioButton) RegisterActivity.this.findViewById(sex.getCheckedRadioButtonId())).getText().toString();

                RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(phone, pass, agestr, sexstr, student);
                registerAsyncTask.execute();
//                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findViews() {
        username = (EditText) findViewById(R.id.usernameRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        age = (EditText) findViewById(R.id.ageRegister);
        sex = (RadioGroup) findViewById(R.id.sexRegister);
        register = (Button) findViewById(R.id.Register);
        studentId = (EditText) findViewById(R.id.studentRegister);
    }

    class RegisterAsyncTask extends AsyncTask<Void, Void, String> {
        private String studentId;
        private String password;
        private String phone;
        private String gender;
        private String age;

        public RegisterAsyncTask(String phone, String password, String age, String gender, String studentId) {
            this.password = password;
            this.studentId = studentId;
            this.phone = phone;
            this.age = age;
            this.gender = gender;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> params = new HashMap<>();
            params.put("phoneNumber", phone);
            params.put("studentId", studentId);
            params.put("password", password);
            params.put("age", age);
            params.put("gender", gender);
            String s = MyRequest.myLogin("/users/register", params);
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

                    //保存token
                    SharedPreferences userToken = getSharedPreferences("token", 0);
                    SharedPreferences.Editor editor = userToken.edit();
                    editor.putString("token", token);
                    editor.apply();

                    System.out.println(token);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (code.equals("404")) {
                    Toast.makeText(RegisterActivity.this, "该手机号已经注册过", Toast.LENGTH_LONG).show();
                } else if (code.equals("400")) {
                    Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
