package com.demo.navigationtest.ui.infoboard;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MainActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditArticleActivity extends AppCompatActivity {

    ImageButton uploadArticleImageBtn;
    private Button submitArticleBtn;
    private EditText editTextTitle, editTextContent, editTextTag;
    private String articleDate;
    private InsertArticle insertArticle = new InsertArticle();
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_article);
        this.getSupportActionBar().hide();

        //绑定页面的各个小组件
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        editTextTag = findViewById(R.id.edit_Tag_Content);
        uploadArticleImageBtn = findViewById(R.id.add_img_btn);
        getArticleDate();

        submitArticleBtn = findViewById(R.id.upload_article_button);
        submitArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(EditArticleActivity.this);
                builder.setIcon(null);//设置图标, 这里设为空值
                builder.setTitle("提醒");
                builder.setMessage("确定发布该资讯吗？");
                builder.setPositiveButton("取消", new OnClickListener(){
                    public void onClick(DialogInterface arg0, int arg1){
                        finish();
                    }
                });
                builder.setNegativeButton("确定", new OnClickListener(){
                    public void onClick(DialogInterface arg0,int arg1){
                        String title = editTextTitle.getText().toString();
                        String tag = editTextTag.getText().toString();
                        String content = editTextContent.getText().toString();
                        if (title.length() < 1)
                            Toast.makeText(EditArticleActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
                        else if (title.length() > 30)
                            Toast.makeText(EditArticleActivity.this, "标题过长！", Toast.LENGTH_SHORT).show();
                        else if (tag.length() < 1)
                            Toast.makeText(EditArticleActivity.this, "标签不能为空！", Toast.LENGTH_SHORT).show();
                        else if (tag.length() > 5)
                            Toast.makeText(EditArticleActivity.this, "标签过长！", Toast.LENGTH_SHORT).show();
                        else if (content.length() < 1)
                            Toast.makeText(EditArticleActivity.this, "资讯内容不能为空！", Toast.LENGTH_SHORT).show();
                        else if (content.length()>3000)
                            Toast.makeText(EditArticleActivity.this, "资讯内容过长！", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(EditArticleActivity.this, "数据上传中，请耐心等待……", Toast.LENGTH_LONG).show();
                            insertArticle.execute();
                        }
                    }
                });
                AlertDialog b = builder.create();
                b.show();//显示对话框
            }
        });
    }

    //获取系统时间
    private void getArticleDate() {
        Calendar cd = Calendar.getInstance();
        int month = cd.get(Calendar.MONTH) + 1;
        int day = cd.get(Calendar.DATE);
        articleDate = "".concat(String.valueOf(month)).concat("月")
                .concat(String.valueOf(day)).concat("日");
    }

    //发送post请求插入新资讯,根据返回数据确定是否发布资讯成功
    class InsertArticle extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            if (token == null){
                Intent intent = new Intent(EditArticleActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            //将上传的图片转成Base64的字符串
            ByteArrayOutputStream byStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byStream);
            byte[] byteArray = byStream.toByteArray();
            String imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //封装参数
            Map<String, String> params = new HashMap<>();
            params.put("title", editTextTitle.getText().toString());
            params.put("tag", editTextTag.getText().toString());
            params.put("date",articleDate);
            params.put("image", imgString);
            params.put("article_content", editTextContent.getText().toString());
            //发送post请求
            return MyRequest.myPost("/articles/insert", params, token);
        }

        @Override
        protected void onPostExecute(String insertResult) {
            try{
                JSONObject bodyJS = new JSONObject(insertResult);
                String code = bodyJS.getString("code");
                if (code.equals("0")) {
                    Toast.makeText(EditArticleActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditArticleActivity.this, "服务器错误，发布失败！", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开本地相册选择图片
     */
    public void uploadArticleImage(View view) {
        //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
        Intent intent=new Intent();
        intent.setType("image/*");
        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
        //类型的内容给你选择
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, 1);
    }
    /**
     *把用户选择的图片显示在imageview中
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if(resultCode==RESULT_OK){
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                uploadArticleImageBtn.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }else{
            //操作错误或没有选择图片
            Log.i("EditArticleActivity", "operation error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
