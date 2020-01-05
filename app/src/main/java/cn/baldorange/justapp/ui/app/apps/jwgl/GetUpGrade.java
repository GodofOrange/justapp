package cn.baldorange.justapp.ui.app.apps.jwgl;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.icu.util.ValueIterator;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.Info.Grade;
import cn.baldorange.justapp.ui.Info.Info;
import cn.baldorange.justapp.ui.util.AlarmReceiver;
import cn.baldorange.justapp.ui.util.OKHttpClientBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetUpGrade extends Service {
    //创建一个通知管理器
    NotificationManager   notificationManager;
    /** 标识服务如果被杀死之后的行为 */
    int mStartMode;
    SQLiteDatabase db;
    /** 绑定的客户端接口 */
    IBinder mBinder;
    AlarmManager alarmManager = null;
    PendingIntent alarmIntent = null;
    /** 标识是否可以使用onRebind */
    boolean mAllowRebind;
    Info info;
    /** 当服务被创建时调用. */
    @Override
    public void onCreate() {

    }

    /** 调用startService()启动服务时回调 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            this.info = getinfo();
            if(this.info ==null){
                Toast.makeText(getApplicationContext(),"请到我的->个人设置内保存密码", Toast.LENGTH_LONG).show();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getUpGrade();

                    }
                }).start();
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int anHour = 5*60*1000;
                long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
                Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"请到我的->个人设置内保存密码", Toast.LENGTH_LONG).show();
        }


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
        final OkHttpClient mOkHttpClient = OKHttpClientBuilder.buildOKHttpClient()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
        RequestBody body = new FormBody.Builder()
                .add("USERNAME", this.info.getStudyid())
                .add("PASSWORD", this.info.getJwglpassword())
                .build();
        final Request request = new Request.Builder()
                .url("http://jwgl.just.edu.cn:8080/jsxsd/xk/LoginToXk")
                .post(body)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "教务系统崩了", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if( response.body().string().contains("学生个人中心")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "密码正确，成功连接到教务系统正在发起轮询", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            mOkHttpClient.newCall(new Request.Builder()
                                    .url("http://jwgl.just.edu.cn:8080/jsxsd/kscj/cjcx_list")
                                    .build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String htm = response.body().string();
                                    Elements ele = Jsoup.parse(htm).select("tr").select("td");
                                    if(isUpGrade(ele.size())){
                                        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        Notification notification = new Notification.Builder(getApplicationContext(), "grade_info")
                                                .setContentTitle("收到好消息")
                                                .setContentText("教务系统成绩更新了，快来看啊！！"+ new Date())
                                                .setSmallIcon(R.drawable.ic_launcher_background)
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                                .setWhen(System.currentTimeMillis())
                                                .build();
                                        int notifiId = 1;
                                        notificationManager.notify(notifiId, notification);
                                    }
                                }
                            });
                            Looper.loop();
                        }
                    }).start();
                }else{
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "网络出错或密码错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });
    }
    public Info getinfo(){
        db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);
        Cursor c = db.rawQuery("select * from user where id = 1", null);
        Info info = new Info();
        if (c != null) {
            c.moveToNext();
            info.setStudyid(c.getString(c.getColumnIndex("studyid")));
            info.setJwglpassword(c.getString(c.getColumnIndex("jwglpassword")));
            info.setVpnpassword(c.getString(c.getColumnIndex("vpnpassword")));
            info.setTiyupassword(c.getString(c.getColumnIndex("tiyupassword")));
            info.setBanshidatingpassword(c.getString(c.getColumnIndex("banshidatingpassword")));
            info.setBbsid(c.getString(c.getColumnIndex("bbsid")));
            info.setBbspassword(c.getString(c.getColumnIndex("bbspassword")));
            info.setAolanpassword(c.getString(c.getColumnIndex("aolanpassword")));
            info.setShiyanzhanghao(c.getString(c.getColumnIndex("shiyanzhanghao")));
            return info;
        }else{
            c.close();
            return info;
        }
    }
    public boolean isUpGrade(int i){
        if(isTableNull("upgrade")){
            Looper.prepare();
            Toast.makeText(getApplicationContext(),"首次使用该服务，正在缓存当前成绩", Toast.LENGTH_SHORT).show();
            Looper.loop();
            db.execSQL("insert into upgrade values(+"+i+"+)");
            return false;
        }else{
            Cursor c = db.rawQuery("select * from upgrade", null);
            c.moveToNext();
            int a = c.getInt(0);
            if(i!=a) db.execSQL("update upgrade set nums = "+i);
            return i!=a ;
        }
    }

    public boolean isTableNull(String table){
        Cursor c = db.rawQuery("select count(*) from "+table, null);
        if (c != null) {
            while (c.moveToNext()) {
                int count = c.getInt(0);
                if (count > 0) {
                    c.close();
                    return false;
                }
            }
        }else{
            c.close();
            return true;
        }
        return true;
    }
}
