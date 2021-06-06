package com.demo.navigationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Pair;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyRequest {
    private static final String myUrl = "http://10.0.2.2:3000";

    //通用get请求  token使用方法可以参考  UserInfoActivity中的demo
    public static String myGet(String url,String token){
        String realUrl = myUrl + url;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("token",token)
                    .get().url(realUrl).build();
            System.out.println(request);
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println(result);
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    ;
    //通用post请求  token使用方法可以参考  UserInfoActivity中的demo
    public static String myPost(String url, Map<String, String> params,String token) {
        String realUrl = myUrl + url;
        OkHttpClient client = new OkHttpClient();
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
            Request request = new Request.Builder()
                    .header("token",token)
                    .post(builder.build()).url(realUrl).build();
            System.out.println(request);
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //System.out.println(result);
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String myLogin(String url, Map<String, String> params) {
        String realUrl = myUrl + url;
        OkHttpClient client = new OkHttpClient();
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
            Request request = new Request.Builder()
                    .post(builder.build()).url(realUrl).build();
            System.out.println(request);
            Response reponse = client.newCall(request).execute();
            String s = reponse.body().string();
            return s;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
