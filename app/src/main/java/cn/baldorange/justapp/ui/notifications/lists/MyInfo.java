package cn.baldorange.justapp.ui.notifications.lists;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;


import cn.baldorange.justapp.R;
import cn.baldorange.justapp.ui.Info.Info;

public class MyInfo extends AppCompatActivity {
    SQLiteDatabase db;
     EditText studyid;
    EditText jwglpassword;
     EditText vpnpassword;
    EditText aolanpassword;
    EditText tiyupassword;
     EditText banshidatingpassword;
     EditText bbsid;
     EditText bbspassword;
     EditText shiyanzhanghao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Info info = new Info();
                info.setStudyid(studyid.getText().toString());
                info.setJwglpassword(jwglpassword.getText().toString());
                info.setBbspassword(bbspassword.getText().toString());
                info.setBbsid(bbsid.getText().toString());
                info.setBanshidatingpassword(banshidatingpassword.getText().toString());
                info.setVpnpassword(vpnpassword.getText().toString());
                info.setTiyupassword(tiyupassword.getText().toString());
                info.setAolanpassword(aolanpassword.getText().toString());
                info.setShiyanzhanghao(shiyanzhanghao.getText().toString());
                updata(info);
                Snackbar.make(view, "所有密码均保存到手机", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }

    protected void init(){
        studyid = findViewById(R.id.editText);
        jwglpassword = findViewById(R.id.editText2);
        vpnpassword = findViewById(R.id.editText3);
        aolanpassword = findViewById(R.id.editText4);
        tiyupassword = findViewById(R.id.editText5);
        banshidatingpassword = findViewById(R.id.editText6);
        bbsid = findViewById(R.id.editText7);
        bbspassword = findViewById(R.id.editText8);
        shiyanzhanghao = findViewById(R.id.editText9);
       db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);
       if(!isTableExist("user")) {
           String sql = "create table user" +
                   "(id int," +
                   "studyid varchar(255)," +
                   "jwglpassword varchar(255)," +
                   "vpnpassword varchar(255)," +
                   "aolanpassword varchar(255)," +
                   "tiyupassword varchar(255)," +
                   "banshidatingpassword varchar(255)," +
                   "bbsid varchar(255)," +
                   "bbspassword varchar(255)," +
                   "shiyanzhanghao varchar(255)" +
                   ")";
           db.execSQL(sql);
       }
       if(isTableNull("user")){
           String sql = "insert into user values(1,'','','','','','','','','')";
           db.execSQL(sql);
       }
       Info info = getinfo();
       studyid.setText(info.getStudyid());
       jwglpassword.setText(info.getJwglpassword());
       vpnpassword.setText(info.getVpnpassword());
       aolanpassword.setText(info.getAolanpassword());
       tiyupassword.setText(info.getTiyupassword());
       banshidatingpassword.setText(info.getBanshidatingpassword());
       bbsid.setText(info.getBbsid());
       bbspassword.setText(info.getBbspassword());
       shiyanzhanghao.setText(info.getShiyanzhanghao());
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
    public boolean updata(Info info){
        try {
            String sql = "update user set " +
                    "studyid = '"+info.getStudyid() +
                    "' ,jwglpassword = '" + info.getJwglpassword() +
                    "' ,vpnpassword = '" + info .getVpnpassword() +
                    "' ,aolanpassword = '" + info .getAolanpassword() +
                    "' ,tiyupassword = '" + info .getTiyupassword() +
                    "',banshidatingpassword = '" + info.getBanshidatingpassword() +
                    "' ,bbsid = '" + info.getBbsid() +
                    "',bbspassword = '" + info . getBbspassword()+
                    "',shiyanzhanghao = '"+info.getShiyanzhanghao()+
                    "' where id = 1";
            db.execSQL(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Info getinfo(){
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
}
