package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.R;
import com.wx.goodview.GoodView;

public class ArticleDetailActivity extends AppCompatActivity {
    //引用这里的包 https://github.com/venshine/GoodView
    private GoodView mGoodView;
    private TextView like_num;
    private boolean like_flag = false; //为了实现再次点击取消点赞的效果，需要flag判断是否点击过
    private EditText comment_edit_text;
    private Button comment_btn;
    private boolean bookmark_flag = false;
    private boolean mask_flag = false;

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
    //copy自GoodView提供者的demo，仅实现了点赞效果，没有关联点赞数
    //TODO: 4. 实现点赞功能
    public void good(View view) {
        like_num = findViewById(R.id.article_like_num);
        if (!like_flag) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            mGoodView.setText("+1");
            like_num.setText("1");
            mGoodView.show(view);
            like_flag = true;
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.good);
            like_num.setText("0");
            like_flag = false;
        }
    }
    //copy自GoodView提供者的demo，仅实现了收藏效果，没有关联用户收藏栏
    //TODO: 5. 实现收藏功能
    public void bookmark(View view) {
        if (!bookmark_flag) {
            ((ImageView) view).setImageResource(R.drawable.bookmark_checked);
            mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12);
            mGoodView.show(view);
            bookmark_flag = true;
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.bookmark);
            bookmark_flag = false;
        }
    }
    //TODO: 6. 实现分享功能，可参考"利用 Android 系统原生 API 实现分享功能" https://www.jianshu.com/p/1d4bd2c5ef69
    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // 指定发送的内容
        sendIntent.putExtra(Intent.EXTRA_TEXT, "文章很有用，快来浙大校医院导航APP看！");
        // 指定发送内容的类型
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享到..."));
    }
    //TODO: 7. 实现屏蔽功能
    public void mask(View view) {
        if (!mask_flag) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            mGoodView.setTextInfo("屏蔽成功", Color.parseColor("#ff941A"), 12);
            mGoodView.show(view);
            mask_flag = true;
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.good);
            mask_flag = false;
        }
    }
}