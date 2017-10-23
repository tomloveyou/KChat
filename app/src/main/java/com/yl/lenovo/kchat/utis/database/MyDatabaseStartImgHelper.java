package com.yl.lenovo.kchat.utis.database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


/**
 * Created by lenovo on 2017/9/12.
 */

public class MyDatabaseStartImgHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE = "create table StartImg(login_url text,regist_url text,splash_url text,local_splash_url text)";

    public MyDatabaseStartImgHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

}
