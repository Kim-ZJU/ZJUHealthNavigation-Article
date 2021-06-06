package com.demo.navigationtest.ui.infoboard;

public class Comment {
    public String ID, articleID, context, date, user;

    public Comment(String ID, String articleID, String context, String date, String user) {
        this.ID = ID;
        this.articleID = articleID;
        this.context = context;
        this.date = date;
        this.user = user;
    }
}
