package com.example.douchebag.da_project_android.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "DIARY_TABLE";
    private static final String COL1 = "header";
    private static final String COL2 = "content";
    private static final String COL3 = "date";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                COL1 + " TEXT, \n" + COL2 + " TEXT NOT NULL, \n" + COL3 + " TEXT NOT NULL);";
        database.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public Cursor getListContent(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public Cursor getListContentBySearch(String search){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE header LIKE ? OR content LIKE ? ORDER BY id DESC";
        Cursor data = database.rawQuery(query, new String[]{search, search});

        return data;
    }

    public boolean addDiary(String[] item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("header", item[0]);
        contentValues.put("content", item[1]);
        contentValues.put("date", item[2]);

        long result = database.insert(TABLE_NAME, null, contentValues);
        return checkResultDiary(result);
    }

    public boolean editDairy(String id, String[] item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("header", item[0]);
        contentValues.put("content", item[1]);
        contentValues.put("date", item[2]);

        long result = database.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        return checkResultDiary(result);
    }

    public void deleteDiary(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, "id = ?", new String[]{id});
    }

    private boolean checkResultDiary(long result){
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }
}