package com.stoefelz.justdoit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_Connection extends SQLiteOpenHelper {
    public DB_Connection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //deadline in milliseconds
        db.execSQL("create table tasks (_id integer primary key, task text, color int, deadline int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    public boolean insert_task(String task, int color, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put("task", task);
        content_values.put("color", color);
        content_values.put("deadline", date);
        db.insert("tasks", null, content_values);
        return true;
    }

    public boolean update_task(int id, String task, int color, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put("task", task);
        content_values.put("color", color);
        content_values.put("deadline", date);
        db.update("tasks", content_values, "_id=?", new String[]{String.valueOf(id)});
        return true;
    }

    public Cursor get_tasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tasks ORDER BY deadline ASC", null);
        return res;
    }

    public boolean delete_task(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "_id =" + id, null);
        return true;
    }

    public boolean delete_all() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tasks");
        return true;
    }

    public Cursor get_task_values(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tasks WHERE _id =" + id, null);
        return res;
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
