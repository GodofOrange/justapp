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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class GetUpGradeActivity extends AppCompatActivity {
    private Switch switch1;
    SQLiteDatabase db;
    NotificationManager   notificationManager;
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
                    Toast.makeText(getApplicationContext(),"成绩更新提醒服务开启", Toast.LENGTH_LONG).show();
                    startService(new Intent(getApplicationContext(),GetUpGrade.class));
                    notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification.Builder(getApplicationContext(), "grade_info")
                            .setContentTitle("科大辅助")
                            .setContentText("您已成功创建成绩更新监听服务。请等待通知"+ new Date())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                            .setWhen(System.currentTimeMillis())
                            .build();
                    int notifiId = 1;
                    notificationManager.notify(notifiId, notification);
                }else {
                    Toast.makeText(getApplicationContext(),"成绩更新提醒服务关闭", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getApplicationContext(), GetUpGrade.class));
                }
            }
        });

        db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);
        /*
        if(!isTableExist("grade")){
            String sql = "create table grade(" +
                    "id varchar(255)" +
                    ",start varchar(255)" +
                    ",number varchar(255)" +
                    ",claName varchar(255)" +
                    ",grade varchar(255)" +
                    ",xuefen varchar(255)" +
                    ",alltime varchar(255)" +
                    ",kaohe varchar(255)" +
                    ",shuxing varchar(255)" +
                    ",prop varchar(255))";
            db.execSQL(sql);
        }*/
        if(!isTableExist("upgrade")){
            String sql ="create table upgrade(nums int)";
            db.execSQL(sql);
        }
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

    public boolean isTableExist(String table) {

        Cursor c = db.rawQuery("select count(*) from sqlite_master where type='table' and name='"+table+"'", null);
        if (c != null) {
            while (c.moveToNext()) {
                int count = c.getInt(0);
                if (count > 0) {
                    c.close();
                    return true;
                }
            }
        }else{
            c.close();
        }
        return false;
    }

}
