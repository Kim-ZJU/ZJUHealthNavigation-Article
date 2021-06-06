package com.demo.navigationtest.ui.infoboard;

import android.graphics.Bitmap;

public class Article {
    public String title, tag, date;
    public Bitmap image;

    public Article(String title, String tag, String date, Bitmap image) {
        this.title = title;
        this.tag = tag;
        this.date = date;
        this.image = image;
    }
}
