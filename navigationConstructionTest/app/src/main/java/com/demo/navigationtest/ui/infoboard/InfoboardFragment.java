package com.demo.navigationtest.ui.infoboard;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.navigationtest.LoginActivity;
import com.demo.navigationtest.MyRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.demo.navigationtest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoboardFragment extends Fragment {

    private InfoboardViewModel infoboardViewModel;

    private List<Article> mArticleList = new ArrayList<>();
    private ArticleAdapter mArticleAdapter;
    private FloatingActionButton newArticleFAB;
    RecyclerView articleRV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoboardViewModel =
                ViewModelProviders.of(this).get(InfoboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_infoboard, container, false);
        //给发布资讯按钮添加点击事件
        newArticleFAB = root.findViewById(R.id.new_article_fab);
        newArticleFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String role = getContext().getSharedPreferences("token", 0).getString("role", null);
                if (role.equals("student"))
                    Toast.makeText(getContext(), "您没有权限发布资讯！", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getContext(), EditArticleActivity.class);
                    startActivity(intent);
                }
            }
        });
        //图片较大，导致获取资讯有点慢，需要用户耐心等待
        Toast.makeText(getContext(), "图片较大，数据传输需要时间哦！获取资讯列表中……", Toast.LENGTH_LONG).show();
        //获取资讯列表
        FetchArticleList fetchArticleList = new FetchArticleList();
        fetchArticleList.execute();
        //设置适配器
        mArticleAdapter = new ArticleAdapter(mArticleList);
        //"重写"实现item点击事件
        mArticleAdapter.setOnItemClickListener(new ArticleAdapter.IOnItemClickListener() {
            @Override
            public void onItemCLick(int position, Article article) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                //传入选中资讯的标题、日期
                intent.putExtra("title",article.title);
                intent.putExtra("date", article.date);
                startActivity(intent);
            }
        });
        //添加RecyclerView展示资讯列表
        articleRV = root.findViewById(R.id.article_rv);
        return root;
    }

    //获取最新的十条资讯，以列表形式显示
    class FetchArticleList extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences sp = getActivity().getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            if (token == null){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
            return MyRequest.myGet("/articles/init",token);
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
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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
        private IOnItemClickListener mItemClickListener;
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
        public void setOnItemClickListener(IOnItemClickListener listener) {
            mItemClickListener = listener;
        }
    }

}