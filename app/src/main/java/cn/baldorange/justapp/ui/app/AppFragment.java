package cn.baldorange.justapp.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.app.apps.jwgl.GetUpGrade;
import cn.baldorange.justapp.ui.app.apps.jwgl.GetUpGradeActivity;
import cn.baldorange.justapp.ui.util.OKHttpClientBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppFragment extends Fragment {

    private AppViewModel appViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appViewModel =
                ViewModelProviders.of(this).get(AppViewModel.class);
        View root = inflater.inflate(R.layout.fragment_app, container, false);
        final ImageView imageView = root.findViewById(R.id.imageView);
        appViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"成绩定时提醒", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), GetUpGradeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        return root;
    }

}