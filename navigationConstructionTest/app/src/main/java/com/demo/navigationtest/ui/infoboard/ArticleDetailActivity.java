package com.demo.navigationtest.ui.infoboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class ArticleDetailActivity extends AppCompatActivity {

    //private String htmlContents = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        this.getSupportActionBar().hide();
        /*
        HtmlTextView htmlTextView = findViewById(R.id.article_content);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        htmlContents = bundle.getCharSequence("contents").toString();
        htmlTextView.setHtml(htmlContents, new HtmlHttpImageGetter(htmlTextView));
         */
    }

}
