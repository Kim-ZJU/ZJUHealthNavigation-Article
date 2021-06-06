package com.demo.navigationtest.ui.user.health_info;

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
import com.demo.navigationtest.MainActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.user.help.UserHelpCardComponent;
import com.demo.navigationtest.ui.user.user_info.UserInfoActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserHealthInfoActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_healthinfo);
        token = getSharedPreferences("token", 0).getString("token", null);
        HealthFecthTask healthFecthTask = new HealthFecthTask(token);
        healthFecthTask.execute();
    }

    public void healthInfoClicked(View v) {
        final int id = v.getId();
        final View dialogView = LayoutInflater.from(UserHealthInfoActivity.this).inflate(R.layout.dialog_user_health_easy_editor, null);
        TextView textView = dialogView.findViewById(R.id.user_health_easy_dialog_text);
        switch (id) {
            case R.id.user_health_blood_type:
                final String[] blood_types = {"A", "B", "O", "AB"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                listDialog.setTitle("请选择血型");
                listDialog.setItems(blood_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "blood", blood_types[which], "", id);
                        healthUpdateTask.execute();
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
            case R.id.user_health_RH:
                final String[] items = {"是", "否", "不详"};
                listDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                listDialog.setTitle("是否为RH血型阴性");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "bloodRH", items[which], "", id);
                        healthUpdateTask.execute();
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

            case R.id.user_health_allergy:
                final String[] items1 = {"是", "否", "不详"};
                listDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                listDialog.setTitle("是否有过敏史");
                listDialog.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "allergy", items1[which], "", id);
                        healthUpdateTask.execute();
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

            case R.id.user_health_inheritance:
                final String[] items2 = {"是", "否", "不详"};
                listDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                listDialog.setTitle("是否有遗传病史");
                listDialog.setItems(items2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "inheritance", items2[which], "", id);
                        healthUpdateTask.execute();
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

            case R.id.user_health_height:
                textView.setText("cm");
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入身高").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "height", str, " cm", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.user_health_weight:
                textView.setText("kg");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入体重").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "weight", str, " kg", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.user_health_bust:
                textView.setText("cm");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入胸围").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "bust", str, " cm", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_health_waist:
                textView.setText("cm");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入腰围").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "waist", str, " cm", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_health_hipline:
                textView.setText("cm");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入臀围").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "hipline", str, " cm", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_health_systolic:
                textView.setText("mmHg");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入收缩压").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "systolic", str, " mmHg", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.user_health_diastolic:
                textView.setText("mmHg");
                inputDialog =
                        new AlertDialog.Builder(UserHealthInfoActivity.this);
                inputDialog.setTitle("请输入舒张压").setView(dialogView);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dialogView.findViewById(R.id.user_health_easy_dialog_edit_text);
                                String str = editText.getText().toString().trim();
                                HealthUpdateTask healthUpdateTask = new HealthUpdateTask(token, "diastolic", str, " mmHg", id);
                                healthUpdateTask.execute();
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

        }

    }

    private class HealthUpdateTask extends AsyncTask<Void, Void, String> {
        private String token, key, value, postStr;
        private int id;

        public HealthUpdateTask(String token, String key, String value, String postStr, int id) {
            this.token = token;
            this.key = key;
            this.value = value;
            this.id = id;
            this.postStr = postStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> params = new HashMap<>();
            params.put(key, value);
            String s = MyRequest.myPost("/users/healthfile/update", params, token);
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
                    userInfoCardComponent.setContent(value + postStr);
                    Toast.makeText(UserHealthInfoActivity.this,
                            "更新成功！",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserHealthInfoActivity.this,
                            "更新失败！",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class HealthFecthTask extends AsyncTask<Void, Void, String> {
        private String token;

        public HealthFecthTask(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String s = MyRequest.myGet("/users/healthfile/fetch", token);
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
                    JSONObject data = result.getJSONObject("data");
                    try {
                        String blood = data.getString("blood");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_blood_type);
                        userInfoCardComponent.setContent(blood);
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String height = data.getString("height");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_height);
                        userInfoCardComponent.setContent(height + " cm");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String weight = data.getString("weight");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_weight);
                        userInfoCardComponent.setContent(weight + " kg");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String bloodRH = data.getString("bloodRH");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_RH);
                        userInfoCardComponent.setContent(bloodRH);
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String bust = data.getString("bust");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_bust);
                        userInfoCardComponent.setContent(bust + " cm");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String waist = data.getString("waist");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_waist);
                        userInfoCardComponent.setContent(waist + "  cm");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String hipline = data.getString("hipline");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_hipline);
                        userInfoCardComponent.setContent(hipline + " cm");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String systolic = data.getString("systolic");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_systolic);
                        userInfoCardComponent.setContent(systolic + " mmHg");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String diastolic = data.getString("diastolic");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_diastolic);
                        userInfoCardComponent.setContent(diastolic + " mmHg");
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String allergy = data.getString("allergy");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_allergy);
                        userInfoCardComponent.setContent(allergy);
                    } catch (Exception e) {
                    }
                    ;

                    try {
                        String inheritance = data.getString("inheritance");
                        UserInfoCardComponent userInfoCardComponent = (UserInfoCardComponent) findViewById(R.id.user_health_inheritance);
                        userInfoCardComponent.setContent(inheritance);
                    } catch (Exception e) {
                    }
                    ;

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
