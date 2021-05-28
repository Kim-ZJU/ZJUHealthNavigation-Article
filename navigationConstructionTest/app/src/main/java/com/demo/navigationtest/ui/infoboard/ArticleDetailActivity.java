package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.R;
import com.wx.goodview.GoodView;

public class ArticleDetailActivity extends AppCompatActivity {
    //引用这里的包 https://github.com/venshine/GoodView
    private GoodView mGoodView;
    private EditText comment_edit_text;
    private Button comment_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        this.getSupportActionBar().hide();
        mGoodView = new GoodView(this);
        //TODO：3. 实现评论功能
        comment_edit_text = findViewById(R.id.comment_edit_text);
        comment_btn = findViewById(R.id.comment_btn);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ArticleDetailActivity.this, "评论提交成功!等待审核中",
                        Toast.LENGTH_SHORT).show();
                comment_edit_text.setText("");
            }
        });
    }
    //copy自GoodView提供者的demo，仅实现了点赞效果，没有关联点赞数，也没有再次点击取消点赞的效果
    public void good(View view) {
        ((ImageView) view).setImageResource(R.drawable.good_checked);
        mGoodView.setText("+1");
        mGoodView.show(view);
    }
    //copy自GoodView提供者的demo，仅实现了收藏效果，没有关联用户收藏栏，也没有再次点击取消收藏的效果
    public void bookmark(View view) {
        ((ImageView) view).setImageResource(R.drawable.bookmark_checked);
        mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12);
        mGoodView.show(view);
    }

}
