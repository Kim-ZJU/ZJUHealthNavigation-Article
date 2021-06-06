package com.demo.navigationtest.ui.user.audit;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.navigationtest.MyRequest;
import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.infoboard.Comment;
import com.wx.goodview.GoodView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditCommentActivity extends AppCompatActivity {
    private GoodView mGoodView;
    private List<Comment> mCommentList = new ArrayList<>();
    private AuditAdapter mAuditAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_audit);   // list of unaudited comments
        mGoodView = new GoodView(this);

        // 获取待审核评论列表
        FetchCommentList fetchCommentList = new FetchCommentList();
        fetchCommentList.execute();

        //设置适配器
        mAuditAdapter = new AuditCommentActivity.AuditAdapter(mCommentList);


//        articleRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    class FetchCommentList extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences("token",0);
            String token = sp.getString("token",null);
            Map<String, String> params = new HashMap<>();
            return MyRequest.myPost("/articles/comments/fetch", params, token);
        }

        @Override
        protected void onPostExecute(String comments) {
            try{
                JSONObject contentJS = new JSONObject(comments);
                String content = contentJS.getString("content");
                JSONArray jsonArray=new JSONArray(content);
                for(int i=jsonArray.length()-1; i>=0; i--)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String ID=jsonObject.getString("_id");
                    String articleID=jsonObject.getString("articleID");
                    String context=jsonObject.getString("context");
                    String date=jsonObject.getString("date");
                    String user=jsonObject.getString("user");
                    mCommentList.add(new Comment(ID, articleID, context, date, user));
                }
                //在网络线程里重新绘制RecyclerView
                RecyclerView auditRV = findViewById(R.id.audit_rv);
                auditRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                auditRV.setAdapter(mAuditAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 实现audit的viewholder
    private static class AuditVH extends RecyclerView.ViewHolder {
        private ImageView comment_pass, comment_fail;
        private TextView comment_context, comment_date, comment_user;
        private TextView comment_ID, comment_articleID;
        // constructor
        public AuditVH(@NonNull View itemView) {
            super(itemView);
            comment_context = itemView.findViewById(R.id.comment_context);
            comment_date = itemView.findViewById(R.id.comment_date);
            comment_user = itemView.findViewById(R.id.comment_user);
            comment_pass = itemView.findViewById(R.id.comment_pass);
            comment_fail = itemView.findViewById(R.id.comment_fail);
            comment_ID = itemView.findViewById(R.id.comment_ID);
            comment_articleID = itemView.findViewById(R.id.comment_articleID);
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
            holder.comment_ID.setText(comment.ID);
            holder.comment_articleID.setText(comment.articleID);

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
        final LinearLayout parentView = (LinearLayout) view.getParent();  // 通过parent来唯一确定id具体对应哪个控件
        View pass = parentView.findViewById(R.id.comment_pass);
        pass.setVisibility(View.INVISIBLE);
        View fail = parentView.findViewById(R.id.comment_fail);
        fail.setVisibility(View.INVISIBLE);
        View text = parentView.findViewById(R.id.comment_text_pass);
        text.setVisibility(View.VISIBLE);
        // 和后端通信
        TextView comment_ID = (TextView) parentView.findViewById(R.id.comment_ID);
        final String commentID = comment_ID.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("token",0);
                String token = sp.getString("token",null);
                Map<String, String> params = new HashMap<>();
                params.put("commentID", commentID);
                params.put("status", "1");
                MyRequest.myPost("/articles/comments/update", params, token);
            }
        }).start();
        // TODO 通知用户
    }

    public void auditFail(View view) {
        LinearLayout parentView = (LinearLayout) view.getParent();  // 通过parent来唯一确定id具体对应哪个控件
        View pass = parentView.findViewById(R.id.comment_pass);
        pass.setVisibility(View.INVISIBLE);
        View fail = parentView.findViewById(R.id.comment_fail);
        fail.setVisibility(View.INVISIBLE);
        View text = parentView.findViewById(R.id.comment_text_fail);
        text.setVisibility(View.VISIBLE);
        // 和后端通信
        TextView comment_ID = (TextView) parentView.findViewById(R.id.comment_ID);
        final String commentID = comment_ID.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("token",0);
                String token = sp.getString("token",null);
                Map<String, String> params = new HashMap<>();
                params.put("commentID", commentID);
                params.put("status", "2");
                MyRequest.myPost("/articles/comments/update", params, token);
            }
        }).start();
    }


}
