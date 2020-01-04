package cn.baldorange.justapp.ui.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这里是工具");
    }

    public LiveData<String> getText() {
        return mText;
    }
}