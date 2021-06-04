package com.demo.navigationtest.ui.infoboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentSet {
    private static List<Comment> mCommentList = new ArrayList<>();

    public static List<Comment> getData() {
        getCommentList();
        mCommentList.clear();
        mCommentList.add(new Comment("孩子买了一车，已经很喜欢了", "2021/06/12", "yuman"));
        mCommentList.add(new Comment("孩子很喜欢，已经买了一车了", "2021/06/04", "manyu"));
        return mCommentList;
    }
    // 连接数据库，获取资讯列表
    // TODO 审核评论和详情页评论分别处理
    private static void getCommentList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://10.0.2.2:3000/comments/audit") // or user
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    //System.out.println(responseBody);
                    decodeResponseBody(responseBody);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void decodeResponseBody(String responseBody) {
        mCommentList.clear();
        System.out.println(mCommentList);
        try{    // TODO 设计评论格式
            JSONObject contentJS = new JSONObject(responseBody);
            String content = contentJS.getString("content");
            JSONArray jsonArray=new JSONArray(content);
            for(int i=jsonArray.length()-1; i>=0; i--)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String context=jsonObject.getString("context");
                String date=jsonObject.getString("date");
                String user=jsonObject.getString("user");
                mCommentList.add(new Comment(context, date, user));
            }
            System.out.println(mCommentList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
