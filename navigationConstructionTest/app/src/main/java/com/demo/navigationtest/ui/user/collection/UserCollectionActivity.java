package com.demo.navigationtest.ui.user.collection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.infoboard.Article;
import com.demo.navigationtest.ui.infoboard.ArticleDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserCollectionActivity extends AppCompatActivity {

    private List<Article> mArticleList = new ArrayList<>();
    private ArticleAdapter mArticleAdapter;
    private RecyclerView articleRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collection);
        System.out.println("this is UserCollectionActivity");

        //获取资讯列表
        FetchArticleList fetchArticleList = new FetchArticleList();
        fetchArticleList.execute();
        //设置适配器
        mArticleAdapter = new ArticleAdapter(mArticleList);
        //"重写"实现item点击事件
        mArticleAdapter.setOnItemClickListener(new ArticleAdapter.IOnItemClickListener() {
            @Override
            public void onItemCLick(int position, Article article) {
                //传入选中资讯的标题、日期、图片
                Intent intent = new Intent(UserCollectionActivity.this, ArticleDetailActivity.class);
                //传入选中资讯的标题、日期、图片
                intent.putExtra("title",article.title);
                intent.putExtra("date", article.date);
                //intent.putExtra("image", article.imageURI);
                startActivity(intent);
            }
        });
        //添加RecyclerView展示资讯列表
        articleRV = findViewById(R.id.user_collections_rv);
    }

    //获取最新的十条资讯，以列表形式显示
    class FetchArticleList extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);

            return MyRequest.myGet("/users/fetchCollections",token);
        }

        @Override
        protected void onPostExecute(String articles) {
            try{
                JSONObject contentJS = new JSONObject(articles);
                String content = contentJS.getString("content");
                JSONArray jsonArray=new JSONArray(content);
                for(int i=jsonArray.length()-1; i>=0; i--)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String title=jsonObject.getString("title");
                    String tag=jsonObject.getString("tag");
                    String date=jsonObject.getString("date");
                    String image=jsonObject.getString("image");
                    //将Base64字符串解码成bitmap
                    Bitmap item_img;
                    byte[] bitmapArray = Base64.decode(image, Base64.DEFAULT);
                    item_img = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    mArticleList.add(new Article(title, tag, date, item_img));
                }
                //在网络线程里重新绘制RecyclerView
                articleRV.setAdapter(mArticleAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(UserCollectionActivity.this, RecyclerView.VERTICAL, false);
                articleRV.setLayoutManager(layoutManager);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //拓展RecyclerView.ViewHolder实现ArticleVH
    private static class ArticleVH extends RecyclerView.ViewHolder {
        private ImageView article_img;
        private final TextView article_title, article_tag, article_date;
        //ArticleVH的constructor
        public ArticleVH(@NonNull View itemView) {
            super(itemView);
            article_img = itemView.findViewById(R.id.article_img);
            article_title = itemView.findViewById(R.id.article_title);
            article_tag = itemView.findViewById(R.id.article_tag);
            article_date = itemView.findViewById(R.id.article_date);
        }
    }

    //拓展RecyclerView.Adapter实现ArticleAdapter（适配器）
    private static class ArticleAdapter extends RecyclerView.Adapter<ArticleVH> {
        //RecyclerView管理的资讯列表
        final private List<Article> articleList;
        //声明自定义的点击事件监听器接口
        private ArticleAdapter.IOnItemClickListener mItemClickListener;
        //适配器的constructor
        public ArticleAdapter(List<Article> articleList) {
            this.articleList = articleList;
        }

        //根据RecyclerView要求必须实现的三个函数
        @NonNull
        @Override
        public ArticleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ArticleVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article, parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleVH holder, final int position) {
            //绑定item的各项控件
            Article article = articleList.get(position);
            holder.article_title.setText(article.title);
            holder.article_tag.setText(article.tag);
            holder.article_date.setText(article.date);
            holder.article_img.setImageBitmap(article.image);

            //点击某条资讯查看具体内容
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemCLick(position, articleList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return articleList.size();
        }
        //增加点击事件监听器接口
        public interface IOnItemClickListener {
            void onItemCLick(int position, Article article);
        }
        //为监听器提供方法
        public void setOnItemClickListener(ArticleAdapter.IOnItemClickListener listener) {
            mItemClickListener = listener;
        }
    }
}
