package cn.baldorange.justapp.ui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.baldorange.justapp.ui.app.apps.jwgl.GetUpGrade;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, GetUpGrade.class);
        context.startService(i);
    }
}