package com.vic.battlesimulation.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "simulation.db";
    private static final String ENEMY_TABLE = "Enemy";
    private static final String MY_TABLE = "MyAttr";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MyDBHelper(Context context, String name,int version){
        this(context,name,null,version);
    }
    public MyDBHelper(Context context,String name)
    {
        this(context, name, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + ENEMY_TABLE +
                "(id integer primary key AUTOINCREMENT, name text, power real, fire integer, defence " +
                "integer,speed integer,luck integer)";
        sqLiteDatabase.execSQL(sql);
        String sql2 = "create table if not exists "+ MY_TABLE + "(id integer primary key AUTOINCREMENT," +
                "name text, power real, fire integer, defence integer,speed integer,luck integer," +
                "supernum integer ,medium integer,lownum integer,satellite real,loss integer," +
                "level integer,clonezeng real,clonejian real,clonebao real,clonefan real," +
                "angel integer,insect integer,nano integer,mutant integer,dragon integer,nickname text)";
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
