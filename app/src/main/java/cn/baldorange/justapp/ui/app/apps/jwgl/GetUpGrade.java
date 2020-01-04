package cn.baldorange.justapp.ui.app.apps.jwgl;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import androidx.core.app.NotificationManagerCompat;
import cn.baldorange.justapp.MainActivity;
import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.util.AlarmReceiver;
import cn.baldorange.justapp.ui.util.OKHttpClientBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetUpGrade extends Service {
    //创建一个通知管理器
    NotificationManager   notificationManager;
    /** 标识服务如果被杀死之后的行为 */
    int mStartMode;

    /** 绑定的客户端接口 */
    IBinder mBinder;
    AlarmManager alarmManager = null;
    PendingIntent alarmIntent = null;
    /** 标识是否可以使用onRebind */
    boolean mAllowRebind;

    /** 当服务被创建时调用. */
    @Override
    public void onCreate() {

    }

    /** 调用startService()启动服务时回调 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUpGrade();
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(getApplicationContext(), "grade_info")
                        .setContentTitle("收到新消息")
                        .setContentText("教务系统成绩更新了，快来看啊！！"+ new Date())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setWhen(System.currentTimeMillis())
                        .build();
                int notifiId = 1;
                notificationManager.notify(notifiId, notification);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return mStartMode;
    }

    /** 通过bindService()绑定到服务的客户端 */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** 通过unbindService()解除所有客户端绑定时调用 */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** 通过bindService()将客户端绑定到服务时调用*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** 服务不再有用且将要被销毁时调用 */
    @Override
    public void onDestroy() {
        alarmManager = (AlarmManager)getApplication().getSystemService(Context.ALARM_SERVICE);
        Intent intentTo = new Intent(getApplicationContext(),AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intentTo, 0);
        if(null != alarmIntent){
            alarmManager.cancel(alarmIntent);
        }
    }

    private void getUpGrade() {
        OkHttpClient mOkHttpClient = OKHttpClientBuilder.buildOKHttpClient().build();
        Request request = new Request.Builder()
                .url("http://jwgl.just.edu.cn:8080/")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                Looper.loop();
                Log.d("",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
}
