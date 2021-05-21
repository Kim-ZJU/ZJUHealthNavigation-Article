package com.demo.navigationtest.ui.appoint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppointViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AppointViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("挂号界面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
