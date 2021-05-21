package com.demo.navigationtest.ui.infoboard;

import android.media.Image;
import android.net.Uri;
import java.util.Date;

public class Article {
    public String title, tag, date, imageURI;

    public Article(String title, String tag, String date, String imageURI) {
        this.title = title;
        this.tag = tag;
        this.date = date;
        this.imageURI = imageURI;
    }
}
