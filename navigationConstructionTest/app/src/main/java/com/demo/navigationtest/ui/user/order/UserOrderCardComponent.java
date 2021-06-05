package com.demo.navigationtest.ui.user.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.navigationtest.R;

public class UserOrderCardComponent extends RelativeLayout {
    private TextView titleView;
    private TextView contentDepartmentView;
    private TextView contentDoctorView;
    private String namespace = "http://schemas.android.com/apk/res/com.demo.navigationtest";
    private String title;
    private String content_department;
    private String content_docter;
    public UserOrderCardComponent(Context context, AttributeSet attrs){
        super(context,attrs);

        View view = View.inflate(context, R.layout.component_user_order_card,this);
        title = attrs.getAttributeValue(namespace, "user_order_card_component_title");
        content_department = attrs.getAttributeValue(namespace,"user_order_card_component_department");
        content_docter=attrs.getAttributeValue(namespace,"user_order_card_component_doctor");
        titleView = view.findViewById(R.id.component_user_order_card_title);
        contentDoctorView=view.findViewById(R.id.component_user_order_card_doctor);
        contentDepartmentView = view.findViewById(R.id.component_user_order_card_department);
        if(title!=null){
            titleView.setText(title);
        }
        if(content_department!=null){
            contentDepartmentView.setText(content_department);
        }
        if(content_docter!=null){
            contentDoctorView.setText(content_docter);
        }
    }
}