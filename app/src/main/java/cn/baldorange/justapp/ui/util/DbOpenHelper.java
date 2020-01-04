package cn.baldorange.justapp.ui.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static String name = "just.db";
    private static int version = 1;

    public DbOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 只能支持基本数据类型
        String sql = "create table user" +
                "(id integer primary key autoincrement," +
                "name varchar(64),address varchar(64))";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql="alter table person add sex varchar(8)";
        db.execSQL(sql);
    }
}