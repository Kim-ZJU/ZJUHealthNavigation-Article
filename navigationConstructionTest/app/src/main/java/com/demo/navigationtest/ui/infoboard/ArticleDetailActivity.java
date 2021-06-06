package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.wx.goodview.GoodView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleDetailActivity extends AppCompatActivity {

    //资讯详情页的一些小组件
    private GoodView mGoodView; //引用这里的包 https://github.com/venshine/GoodView
    private TextView articleTitle, articleDate, articleContent, like_num;
    private String articleID;
    //private ImageView articleImage;
    private boolean like_flag = false; //为了实现再次点击取消点赞的效果，需要flag判断是否点击过
    private EditText comment_edit_text;
    private Button comment_btn;
    private boolean bookmark_flag = false, mask_flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        this.getSupportActionBar().hide();
        //获取选中资讯的标题、标签、日期、图片
        Intent intent = getIntent();
        articleTitle = findViewById(R.id.article_detail_title);
        articleTitle.setText(intent.getStringExtra("title"));
        articleDate = findViewById(R.id.article_detail_date);
        articleDate.setText(intent.getStringExtra("date"));
        articleContent = findViewById(R.id.article_content);
        FetchArticleContent fetchArticleContent = new FetchArticleContent();
        fetchArticleContent.execute();

        mGoodView = new GoodView(this);
        //TODO：3. 实现评论功能
        comment_edit_text = (EditText) findViewById(R.id.comment_edit_text);
        comment_btn = findViewById(R.id.comment_btn);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";   // 点击按钮后输出的提示信息
                String comment_contex = comment_edit_text.getText().toString();
                if (comment_contex.length() == 0) {
                    text = "评论不能为空。";
                } else if (comment_contex.length() > 200) {
                    text = "评论过长，请重新编辑！";
                } else {
                    text = "评论提交成功！等待审核中。";
                    // 提交评论到后端
                    CreateComment createComment = new CreateComment(comment_contex);
                    createComment.execute();
                }
                Toast.makeText(ArticleDetailActivity.this, text,
                        Toast.LENGTH_SHORT).show();
                comment_edit_text.setText("");  // 清空评论区
            }
        });
    }

    class CreateComment extends AsyncTask<Void, Void, String> {
        private String comment_contex;

        public CreateComment(String comment_contex) {
            this.comment_contex = comment_contex;
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            if (token == null){
                Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            // 获取用户名
            final String[] userName = {""};
            Thread fetchUserName = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    String result = MyRequest.myGet("/users/fetchUserInfo", token);
                    // 解析出用户名
                    try{
                        JSONObject contentJS = new JSONObject(result);
                        String content = contentJS.getString("data");
                        JSONObject userJS = new JSONObject(content);
                        userName[0] = userJS.getString("phoneNumber");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            fetchUserName.start();
            try {
                fetchUserName.join();   // 等待后端回复用户名
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 填充POST所需参数
            Map<String, String> params = new HashMap<>();
            params.put("articleID", articleID);
            params.put("status", "0");  // 待审核
            params.put("user", userName[0]);
            params.put("date", articleDate.getText().toString());
            params.put("context", comment_contex);

            return MyRequest.myPost("/articles/comments/insert", params, token);
        }

        @Override
        protected void onPostExecute(String rst) {
            System.out.println(rst);
        }
    }

    //连接数据库，根据选中资讯的标题获取资讯内容
    class FetchArticleContent extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            if (token == null){
                Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            Map<String, String> params = new HashMap<>();
            params.put("title", articleTitle.getText().toString());
            return MyRequest.myPost("/articles/fetch", params, token);
        }

        @Override
        protected void onPostExecute(String fetchDetailResult) {
            try{
                JSONObject contentJS = new JSONObject(fetchDetailResult);
                String content = contentJS.getString("content");
                JSONArray jsonArray=new JSONArray(content);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                String article_content = jsonObject.getString("article_content");
                articleID = jsonObject.getString("_id");
                articleContent.setText(article_content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //copy自GoodView提供者的demo，仅实现了点赞效果，没有关联点赞数
    //TODO: 4. 实现点赞功能
    public void good(View view) {
        like_num = findViewById(R.id.article_like_num);
        if (!like_flag) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            mGoodView.setText("+1");
            int likes = Integer.parseInt(like_num.getText().toString()) + 1;
            like_num.setText(String.valueOf(likes));
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
            ((ImageView) view).setImageResource(R.drawable.mask_checked);
            mGoodView.setTextInfo("屏蔽成功", Color.parseColor("#ff941A"), 12);
            mGoodView.show(view);
            mask_flag = true;
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.mask);
            mask_flag = false;
        }
    }
}