package com.demo.navigationtest.ui.infoboard;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleSet {

    public static List<Article> getData() {
        List<Article> result = new ArrayList<>();
        //TODO:1.此处需要连接数据库获得数据
        result.add(new Article("牙齿痛得死去活来？学会这四招", "牙周炎", "5月20日", "https://tse2-mm.cn.bing.net/th/id/OIP.ziZVTC9juephnyiYnXuC0QHaFU?w=255&h=183&c=7&o=5&dpr=2&pid=1.7"));
        result.add(new Article("身体虚弱你真的知道怎么食补吗？", "保健", "4月20日", "https://th.bing.com/th/id/R6418c915e8b43ee1fee3e907b9c81123?rik=KGt9OquVjzhCiQ&riu=http%3a%2f%2fwww.jkyschina.org.cn%2fuploads%2fallimg%2f161114%2f1-161114122445194.png&ehk=1jYSIzdEjin5qX4xQy6uSMESwGg5e%2biUL1i9t5ftc%2fY%3d&risl=&pid=ImgRaw"));
        result.add(new Article("有什么提高睡眠质量的方法", "睡眠", "4月20日", "https://tse1-mm.cn.bing.net/th/id/OIP.kKZXgkWFu0VRzSxXN3Ll-gHaFi?w=249&h=186&c=7&o=5&dpr=2&pid=1.7"));
        result.add(new Article("这四种水果很多人吃错了！", "其他", "4月19日", "https://p3.itc.cn/q_70/images01/20210418/7c070766ccff4de3b1a24e0bb975644a.jpeg"));
        result.add(new Article("换季不敢出游？重度鼻炎患者日常生活注意这六点", "鼻炎", "4月15日", "https://tse2-mm.cn.bing.net/th/id/OIP.7BeGaterwQ0BoJmDR-5cGgHaEK?w=315&h=180&c=7&o=5&dpr=2&pid=1.7"));
        result.add(new Article("身体的这个地方颤动，中风的风险增加五倍！", "房颤", "4月13日", "https://tse1-mm.cn.bing.net/th/id/OIP.ENFjWn3Phh9fahzZcYhbdQHaEt?w=288&h=183&c=7&o=5&dpr=2&pid=1.7"));
        return result;
    }

    public static void getArticleList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    //MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");
                    //RequestBody requestBody = RequestBody.create("title:A", MEDIA_TYPE_TEXT);
                    FormBody requestBody = new FormBody.Builder()
                            .add("title","A")
                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url("http://10.0.2.2:3000/articles/fetch")
                            .build();
                    System.out.println(request);
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    System.out.println(result);
                    //showData(Data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
