package cn.baldorange.justapp.ui.app.apps.jwgl;

import androidx.appcompat.app.AppCompatActivity;
import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.util.AlarmReceiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GetUpGradeActivity extends AppCompatActivity {
    private Switch switch1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_up_grade);
        switch1 = findViewById(R.id.switch1);
        switch1.setChecked(isServiceRunning(getApplicationContext(),"cn.baldorange.justapp.ui.app.apps.jwgl.GetUpGrade"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"成绩定时提醒服务开启", Toast.LENGTH_LONG).show();
                    startService(new Intent(getApplicationContext(),GetUpGrade.class));

                }else {
                    Toast.makeText(getApplicationContext(),"成绩定时提醒服务关闭", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getApplicationContext(), GetUpGrade.class));
                }
            }
        });
    }
    /**
     * 查看服务是否开启
     */
    public static Boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}
