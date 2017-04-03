package com.example.grocery.grocery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME = "Grocery";
    public static final String TABLE_NAME = "lists";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    //    db.execSQL("create table contacts " + "(name text, password text,location text,mobile1 text, mobile2 text,my_mobile text,mail text)");
        db.execSQL("create table lists " + "(name text,email text,password text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insert1(String name,String email,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor getData1(String location){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res1 =  db.rawQuery( "select * from lists where item= " + location +"",null );
        return res1;
    }
    public Cursor get_name() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from lists", null);
        return res;
    }
    public String getData(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from lists where item=" + name + " location=" + password + "", null);
        //  SELECT count(uname) FROM users where uname='".$uname."' and pwd='".$pwd."'
        String res1=res.toString();
        return res1;
    }
    public Cursor display() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res1 = db.rawQuery("select * from '" + TABLE_NAME + "'", null);
        return res1;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public boolean update(String mob1,String mob2) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put("MOBILE1", mob1);
        contentValues.put("MOBILE2", mob2);
        db.update(TABLE_NAME,contentValues,null,null);
        return true;
    }


    public void delete() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res2= db.rawQuery("Delete * from contacts where name=mag",null);

    }




}
