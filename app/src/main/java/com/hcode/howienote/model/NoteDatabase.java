package com.hcode.howienote.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类
 *
 * @author howie
 * @time 2020-07-25 21:28:28
 */

public class NoteDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";  // 数据库名
    public static final String ID = "_id";  // 主键字段
    public static final String TIME = "time";  // 时间字段
    public static final String CONTENT = "content"; // 内容字段
    public static final String TAG = "tag";  // 标签字段

    public NoteDatabase(Context context) {
        super(context, "my.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr = "create table " + TABLE_NAME
                + "("
                + ID + " integer primary key autoincrement,"
                + TIME + " text not null,"
                + CONTENT + " text not null,"
                + TAG + " integer default 1"
                + ")";
        db.execSQL(sqlStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (oldVersion) {
//            case 1:
//            case 2:
//        }
    }
}
