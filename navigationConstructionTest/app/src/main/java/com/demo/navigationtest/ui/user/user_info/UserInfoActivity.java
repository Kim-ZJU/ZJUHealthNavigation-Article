package com.demo.navigationtest.ui.user.user_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.user.health_info.UserInfoCardComponent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        FetchUserInfo fetchUserInfo = new FetchUserInfo();
        fetchUserInfo.execute();

        token = getSharedPreferences("token", 0).getString("token", null);
    }


    public void userInfoClicked(View v) {
        final int id = v.getId();
        final View dialogView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.dialog_user_health_easy_editor, null);
        TextView textView = dialogView.findViewById(R.id.user_health_easy_dialog_text);
        switch (id) {

            case R.id.user_info_phone:
                Toast.makeText(UserInfoActivity.this,
                        "手机号是唯一id，不可以更改！",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.user_info_studentId:
                textView.setText("");
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(UserInfoActivity.this);
                inputDialog.setTitle("请输入学号").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                UserInfoUpdateTask userInfoUpdateTask = new UserInfoUpdateTask(token, "studentId", str, id);
                                userInfoUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_info_name:
                textView.setText("");
                inputDialog =
                        new AlertDialog.Builder(UserInfoActivity.this);
                inputDialog.setTitle("请输入姓名").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                UserInfoUpdateTask userInfoUpdateTask = new UserInfoUpdateTask(token, "name", str, id);
                                userInfoUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_info_gender:

                final String[] gender_types = {"男", "女"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(UserInfoActivity.this);
                listDialog.setTitle("请选择性别");
                listDialog.setItems(gender_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoUpdateTask userInfoUpdateTask = new UserInfoUpdateTask(token, "gender", gender_types[which], id);
                        userInfoUpdateTask.execute();
                    }
                });
                listDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

        }
    }

    class FetchUserInfo extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token", 0);
            String token = sp.getString("token", null);
            if (token == null) {
                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            String s = MyRequest.myGet("/users/fetchUserInfo", token);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject result = new JSONObject(s);
                String code = result.getString("code");
                if (code.equals("0")) {
                    JSONObject user = result.getJSONObject("data");

                    UserInfoCardComponent studentIdComponent = (UserInfoCardComponent) findViewById(R.id.user_info_studentId);
                    studentIdComponent.setContent(user.getString("studentId"));

                    UserInfoCardComponent phoneComponent = (UserInfoCardComponent) findViewById(R.id.user_info_phone);
                    phoneComponent.setContent(user.getString("phoneNumber"));

                    UserInfoCardComponent genderComponent = (UserInfoCardComponent) findViewById(R.id.user_info_gender);
                    genderComponent.setContent(user.getString("gender"));

                    UserInfoCardComponent nameComponent = (UserInfoCardComponent) findViewById(R.id.user_info_name);
                    nameComponent.setContent(user.getString("name"));
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            System.out.println(s);
        }
    }

    private class UserInfoUpdateTask extends AsyncTask<Void, Void, String> {
        private String token, key, value, postStr;
        private int id;

        public UserInfoUpdateTask(String token, String key, String value, int id) {
            this.token = token;
            this.key = key;
            this.value = value;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> params = new HashMap<>();
            params.put(key, value);
            String s = MyRequest.myPost("/users/update", params, token);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                System.out.println(s);
                JSONObject result = new JSONObject(s);
                String code = result.getString("code");
                if (code.equals("0")) {
                    UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(id);
                    userInfoCardComponent.setContent(value + " ");
                    Toast.makeText(UserInfoActivity.this,
                            "更新成功！",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserInfoActivity.this,
                            "更新失败！",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
