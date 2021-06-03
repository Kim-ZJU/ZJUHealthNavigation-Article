package com.demo.navigationtest.ui.infoboard;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.navigationtest.MainActivity;
import com.demo.navigationtest.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditArticleActivity extends AppCompatActivity {

    ImageButton uploadArticleImageBtn;
    private Button submitArticleBtn;
    private EditText editTextTitle, editTextContent, editTextTag;
    private String articleDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_article);
        this.getSupportActionBar().hide();
        //绑定页面的各个小组件
        editTextTitle = findViewById(R.id.edit_text_title);
        //editTextTitle.getText();
        editTextContent = findViewById(R.id.edit_text_content);
        //editTextContent.getText();
        editTextTag = findViewById(R.id.edit_Tag_Content);
        //editTextTag.getText();
        uploadArticleImageBtn = findViewById(R.id.add_img_btn);
        getArticleDate();

        submitArticleBtn = findViewById(R.id.upload_article_button);
        submitArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertArticle();
                AlertDialog.Builder builder = new Builder(EditArticleActivity.this);
                builder.setIcon(null);//设置图标, 这里设为空值
                builder.setTitle("提醒");
                builder.setMessage("确定发布该资讯吗？");
                builder.setPositiveButton("取消", new OnClickListener(){

                    public void onClick(DialogInterface arg0, int arg1){
                        Toast.makeText(EditArticleActivity.this,"正文不能为空！",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("确定", new OnClickListener(){
                    public void onClick(DialogInterface arg0,int arg1){
                        Toast.makeText(EditArticleActivity.this, "标签过长！", Toast.LENGTH_SHORT).show();
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
    //发送post请求插入新资讯
    private void insertArticle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("title", editTextTitle.getText().toString())
                            .add("tag", editTextTag.getText().toString())
                            .add("date",articleDate)
                            .add("image", "tmp")//TODO：1. 暂时不知道怎么处理图片
                            .add("article_content", editTextContent.getText().toString())
                            .build();
                    System.out.println(requestBody);
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url("http://10.0.2.2:3000/articles/insert")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    decodeInsertResponse(responseBody);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //根据返回数据确定是否发布资讯成功
    private void decodeInsertResponse(String responseBody) {
        try {
            JSONObject bodyJS = new JSONObject(responseBody);
            String code = bodyJS.getString("code");
            if (code.equals("0")) {
                Toast.makeText(this, "发布成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "服务器错误，发布失败！", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //uploadArticleImageBtn.setImageBitmap(bitmap);
                Glide.with(this)
                        .load(bitmap)
                        .error(R.drawable.add_picture_failed)
                        .into(uploadArticleImageBtn);
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
