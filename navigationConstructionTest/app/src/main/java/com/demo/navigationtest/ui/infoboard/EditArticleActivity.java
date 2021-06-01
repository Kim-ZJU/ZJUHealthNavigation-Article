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
import com.demo.navigationtest.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import java.io.FileNotFoundException;

public class EditArticleActivity extends AppCompatActivity {

    ImageButton uploadArticleImageBtn;
    private Button submitArticleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_article);
        this.getSupportActionBar().hide();

        uploadArticleImageBtn = findViewById(R.id.add_img_btn);

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
