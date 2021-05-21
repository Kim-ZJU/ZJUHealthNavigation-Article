package com.demo.navigationtest.ui.appoint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.navigationtest.R;
import com.demo.navigationtest.ui.appoint.AppointViewModel;

public class AppointFragment extends Fragment{
    private AppointViewModel appointViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
        //                ViewModelProviders.of(this).get(HomeViewModel.class);
        appointViewModel =
                ViewModelProviders.of(this).get(AppointViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appoint, container, false);
        final TextView textView = root.findViewById(R.id.text_appoint);
        appointViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
