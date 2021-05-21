package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.R;
import com.wx.goodview.GoodView;

public class ArticleDetailActivity extends AppCompatActivity {
    //引用这里的包 https://github.com/venshine/GoodView
    private GoodView mGoodView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        this.getSupportActionBar().hide();
        mGoodView = new GoodView(this);
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
