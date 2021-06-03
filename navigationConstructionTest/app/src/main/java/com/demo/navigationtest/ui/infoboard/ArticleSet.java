package com.demo.navigationtest.ui.infoboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleSet {
    private static List<Article> mArticleList = new ArrayList<>();

    public static List<Article> getData() {
        getArticleList();
        return mArticleList;
    }
    //连接数据库，获取资讯列表
    private static void getArticleList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://10.0.2.2:3000/articles/init")
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
        mArticleList.clear();
        System.out.println(mArticleList);
        try{
            JSONObject contentJS = new JSONObject(responseBody);
            String content = contentJS.getString("content");
            JSONArray jsonArray=new JSONArray(content);
            for(int i=jsonArray.length()-1; i>=0; i--)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String title=jsonObject.getString("title");
                String tag=jsonObject.getString("tag");
                String date=jsonObject.getString("date");
                String image=jsonObject.getString("image");
                mArticleList.add(new Article(title, tag, date, image));
            }
            System.out.println(mArticleList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
