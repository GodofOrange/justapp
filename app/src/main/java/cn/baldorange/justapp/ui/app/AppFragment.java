package cn.baldorange.justapp.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.app.apps.jwgl.GetUpGradeActivity;


public class AppFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_app, container, false);
        final ImageView imageView = root.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"成绩定时提醒", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GetUpGradeActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

}