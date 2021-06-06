package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.wx.goodview.GoodView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class ArticleDetailActivity extends AppCompatActivity {

    //资讯详情页的一些小组件
    private GoodView mGoodView; //引用这里的包 https://github.com/venshine/GoodView
    private TextView articleTitle, articleDate, articleContent, like_num;
    private String articleId;
    private ImageView articleImage;
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
        articleImage = findViewById(R.id.article_detail_img);
        articleContent = findViewById(R.id.article_content);
        FetchArticleContent fetchArticleContent = new FetchArticleContent();
        fetchArticleContent.execute();

        mGoodView = new GoodView(this);
        //实现评论功能
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

    //连接数据库，根据选中资讯的标题获取资讯图片和内容和ID
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
                JSONObject jsonObject=new JSONObject(content);
                String article_content = jsonObject.getString("article_content");
                articleId = jsonObject.getString("_id");
                articleContent.setText(article_content);
                Bitmap item_img;
                byte[] bitmapArray = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                item_img = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                articleImage.setImageBitmap(item_img);
                String likes = jsonObject.getString("likes");
                like_num = findViewById(R.id.article_like_num);
                like_num.setText(likes);    // 设置点赞数
                // 设置点赞按钮
                ImageView view = findViewById(R.id.article_like);
                String is_like = contentJS.getString("is_like");
                like_flag = is_like.equals("true");
                if (like_flag)
                    view.setImageResource(R.drawable.good_checked);
                else view.setImageResource(R.drawable.good);
                // 设置收藏按钮
                view = findViewById(R.id.article_bookmark);
                String is_favorite = contentJS.getString("is_favorite");
                bookmark_flag = is_favorite.equals("true");
                if (bookmark_flag)
                    view.setImageResource(R.drawable.bookmark_checked);
                else view.setImageResource(R.drawable.bookmark);
                // 设置屏蔽按钮
                view = findViewById(R.id.article_mask);
                String is_mask = contentJS.getString("is_mask");
                mask_flag = is_mask.equals("true");
                if (mask_flag)
                    view.setImageResource(R.drawable.mask_checked);
                else view.setImageResource(R.drawable.mask);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //连接数据库，插入评论内容
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
            params.put("articleID", articleId);
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

    //连接数据库，增加用户收藏
    /*class AddArticleCollection extends AsyncTask<Void, Void, String> {
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
            params.put("_id", articleId);
            return MyRequest.myPost("/users/collections/add", params, token);
        }
    }*/

    //连接数据库，删除用户收藏
    /*class RemoveArticleCollection extends AsyncTask<Void, Void, String> {
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
            params.put("_id", articleId);
            return MyRequest.myPost("/users/collections/delete", params, token);
        }
    }*/

    //实现点赞功能
    public void good(View view) {
        like_num = findViewById(R.id.article_like_num);
        if (!like_flag) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            mGoodView.setText("+1");
            like_num.setText(String.valueOf(Integer.parseInt(like_num.getText().toString()) + 1));
            mGoodView.show(view);
            like_flag = true;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "1");
                    String result = MyRequest.myPost("/articles/like", params, token);
                }
            };
            addLike.start();
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.good);
            like_num.setText(String.valueOf(Integer.parseInt(like_num.getText().toString()) - 1));
            like_flag = false;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "0");
                    String result = MyRequest.myPost("/articles/like", params, token);
                }
            };
            addLike.start();
        }
    }

    //实现了收藏效果，关联用户收藏栏
    //TODO: 5. 实现收藏功能连接到用户
    public void bookmark(View view) {
        if (!bookmark_flag) {
            ((ImageView) view).setImageResource(R.drawable.bookmark_checked);
            mGoodView.setTextInfo("收藏成功", Color.parseColor("#ff941A"), 12);
            mGoodView.show(view);
            bookmark_flag = true;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "1");
                    String result = MyRequest.myPost("/articles/favorite", params, token);
                }
            };
            addLike.start();
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.bookmark);
            bookmark_flag = false;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "0");
                    String result = MyRequest.myPost("/articles/favorite", params, token);
                }
            };
            addLike.start();
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
    //实现屏蔽功能到用户
    public void mask(View view) {
        if (!mask_flag) {
            ((ImageView) view).setImageResource(R.drawable.mask_checked);
            mGoodView.setTextInfo("屏蔽成功", Color.parseColor("#ff941A"), 12);
            mGoodView.show(view);
            mask_flag = true;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "1");
                    String result = MyRequest.myPost("/articles/mask", params, token);
                }
            };
            addLike.start();
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.mask);
            mask_flag = false;
            // 连接后端
            Thread addLike = new Thread() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("token",0);
                    String token = sp.getString("token",null);
                    if (token == null){
                        Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("articleID", articleId);
                    params.put("status", "0");
                    String result = MyRequest.myPost("/articles/mask", params, token);
                }
            };
            addLike.start();
        }
    }
}