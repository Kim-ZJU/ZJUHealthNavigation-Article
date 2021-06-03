package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

import com.demo.navigationtest.R;

public class InfoboardFragment extends Fragment {

    private InfoboardViewModel infoboardViewModel;

    private ArticleAdapter mArticleAdapter;
    private FloatingActionButton newArticleFAB;

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
                Intent intent = new Intent(getContext(), EditArticleActivity.class);
                startActivity(intent);
            }
        });
        //设置适配器
        mArticleAdapter = new ArticleAdapter(ArticleSet.getData());
        //"重写"实现item点击事件
        mArticleAdapter.setOnItemClickListener(new ArticleAdapter.IOnItemClickListener() {
            @Override
            public void onItemCLick(int position, Article article) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                //传入选中资讯的标题、日期、图片
                intent.putExtra("title",article.title);
                intent.putExtra("date", article.date);
                //intent.putExtra("image", article.imageURI);
                startActivity(intent);
            }
        });
        //添加RecyclerView展示资讯列表
        RecyclerView articleRV = root.findViewById(R.id.article_rv);
        articleRV.setAdapter(mArticleAdapter);
        articleRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return root;
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
                    .inflate(R.layout.item_article,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleVH holder, final int position) {
            //绑定item的各项控件
            Article article = articleList.get(position);
            holder.article_title.setText(article.title);
            holder.article_tag.setText(article.tag);
            holder.article_date.setText(article.date);
            //TODO:2.还没处理好上传图片问题，暂时先不绑定图片
            //holder.article_img.setImageURI(Uri.parse(article.imageURI));

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