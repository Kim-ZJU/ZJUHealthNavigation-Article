package com.demo.navigationtest.ui.user.audit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.infoboard.Article;
import com.demo.navigationtest.ui.infoboard.ArticleDetailActivity;
import com.demo.navigationtest.ui.infoboard.ArticleSet;
import com.demo.navigationtest.ui.infoboard.Comment;
import com.demo.navigationtest.ui.infoboard.CommentSet;
import com.demo.navigationtest.ui.infoboard.InfoboardFragment;
import com.wx.goodview.GoodView;

import java.util.List;

public class AuditCommentActivity extends AppCompatActivity {
    private GoodView mGoodView;
    private AuditAdapter mAuditAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_audit);   // list of unaudited comments

        mGoodView = new GoodView(this);

        //设置适配器
        mAuditAdapter = new AuditCommentActivity.AuditAdapter(CommentSet.getData());

        //添加RecyclerView展示资讯列表
        RecyclerView auditRV = findViewById(R.id.audit_rv);
        auditRV.setLayoutManager(new LinearLayoutManager(this));
        auditRV.setAdapter(mAuditAdapter);
//        articleRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    // 实现audit的viewholder
    private static class AuditVH extends RecyclerView.ViewHolder {
        private ImageView comment_pass, comment_fail;
        private final TextView comment_context, comment_date, comment_user;
        // constructor
        public AuditVH(@NonNull View itemView) {
            super(itemView);
            comment_context = itemView.findViewById(R.id.comment_context);
            comment_date = itemView.findViewById(R.id.comment_date);
            comment_user = itemView.findViewById(R.id.comment_user);
            comment_pass = itemView.findViewById(R.id.comment_pass);
            comment_fail = itemView.findViewById(R.id.comment_fail);
        }
    }

    // 实现audit的adapter
    private static class AuditAdapter extends RecyclerView.Adapter<AuditVH> {
        // RecyclerView管理的资讯列表
        final private List<Comment> commentList;

//        //声明自定义的点击事件监听器接口
//        private InfoboardFragment.ArticleAdapter.IOnItemClickListener mItemClickListener;
        // constructor
        public AuditAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @NonNull
        @Override
        public AuditCommentActivity.AuditVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AuditCommentActivity.AuditVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audit_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AuditCommentActivity.AuditVH holder, final int position) {
            // 绑定item的各项控件
            Comment comment = commentList.get(position);
            holder.comment_context.setText(comment.context);
            holder.comment_date.setText(comment.date);
            holder.comment_user.setText(comment.user);

            // TODO 点击进入对应咨询界面
            //点击某条资讯查看具体内容
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // pass
//                }
//            });
            System.out.println(1);
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }

    public void auditPass(View view) {
        // TODO 和后端通信
        LinearLayout parentView = (LinearLayout) view.getParent();  // 通过parent来唯一确定id具体对应哪个控件
        View pass = parentView.findViewById(R.id.comment_pass);
        pass.setVisibility(View.INVISIBLE);
        View fail = parentView.findViewById(R.id.comment_fail);
        fail.setVisibility(View.INVISIBLE);
        View text = parentView.findViewById(R.id.comment_text_pass);
        text.setVisibility(View.VISIBLE);
    }

    public void auditFail(View view) {
        // TODO 和后端通信
        LinearLayout parentView = (LinearLayout) view.getParent();  // 通过parent来唯一确定id具体对应哪个控件
        View pass = parentView.findViewById(R.id.comment_pass);
        pass.setVisibility(View.INVISIBLE);
        View fail = parentView.findViewById(R.id.comment_fail);
        fail.setVisibility(View.INVISIBLE);
        View text = parentView.findViewById(R.id.comment_text_fail);
        text.setVisibility(View.VISIBLE);
    }
}
