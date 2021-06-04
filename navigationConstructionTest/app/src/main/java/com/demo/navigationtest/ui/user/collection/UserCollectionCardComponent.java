package com.demo.navigationtest.ui.user.collection;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.navigationtest.R;

public class UserCollectionCardComponent extends RelativeLayout {
    private TextView titleView;
    private TextView contentView;
    private String namespace = "http://schemas.android.com/apk/res/com.demo.navigationtest";
    private String title;
    private String content;
    public UserCollectionCardComponent(Context context, AttributeSet attrs){
        super(context,attrs);

        View view = View.inflate(context, R.layout.component_user_collection_card,this);
        title = attrs.getAttributeValue(namespace, "user_collection_card_component_title");
        content = attrs.getAttributeValue(namespace,"user_collection_card_component_content");
        titleView = view.findViewById(R.id.component_user_collection_card_title);
        contentView = view.findViewById(R.id.component_user_collection_card_content);
        if(title!=null){
            titleView.setText(title);
        }
        if(content!=null){
            contentView.setText(content);
        }
    }
}
