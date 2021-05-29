package com.demo.navigationtest.ui.user.collection;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.navigationtest.R;

public class UserCollectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collection);
        System.out.println("this is UserCollectionActivity");
    }
}
