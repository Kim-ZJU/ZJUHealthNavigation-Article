package com.demo.navigationtest.ui.infoboard;
import android.content.Intent;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.demo.navigationtest.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class EditArticleActivity extends AppCompatActivity {

    private Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_article);
        this.getSupportActionBar().hide();

        button1 = findViewById(R.id.upload_article_button);
        button1.setOnClickListener(new View.OnClickListener() {
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



}
