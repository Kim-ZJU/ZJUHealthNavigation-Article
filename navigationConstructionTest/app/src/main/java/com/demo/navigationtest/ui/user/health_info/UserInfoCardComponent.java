package com.demo.navigationtest.ui.user.health_info;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.navigationtest.R;

import java.io.File;

public class UserInfoCardComponent extends RelativeLayout {
    private TextView titleView;
    private TextView contentView;
    private String namespace = "http://schemas.android.com/apk/res/com.demo.navigationtest";
    private String title;
    private String content;
    public UserInfoCardComponent(Context context, AttributeSet attrs){
        super(context,attrs);

        View view = View.inflate(context, R.layout.component_user_info_card,this);
        title = attrs.getAttributeValue(namespace, "user_info_card_component_title");
        content = attrs.getAttributeValue(namespace,"user_info_card_component_content");
        titleView = view.findViewById(R.id.component_user_info_card_title);
        contentView = view.findViewById(R.id.component_user_info_card_content);
        if(title!=null){
            titleView.setText(title);
        }
        if(content!=null){
            contentView.setText(content);
        }
    }
}
