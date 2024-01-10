package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String COL_1 = "ID";
    private static  final String COL_2 = "TASK";
    private static  final String COL_3 = "STATUS";
    private static  final String COL_4 = "TITLE";
    private static  final String COL_5 = "DUEDATE";
    private static  final String COL_6 = "CATEGORY";
    private static  final String COL_7 = "PRIORITY";


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT ,TITLE TEXT, TASK TEXT , STATUS TEXT, CATEGORY TEXT, PRIORITY TEXT, DUEDATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
    }

    public void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTitle());
        values.put(COL_3 , model.getTask());
        values.put(COL_4 , model.getStatus());
        values.put(COL_5 , model.getCategory());
        values.put(COL_6 , model.getPriority());
        values.put(COL_7 , model.getDueDate());
        db.insert(TABLE_NAME , null , values);
    }

    public void updateTask(int id , String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , task);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateTitle(int id , String title){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , title);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id , String status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_4 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateCategory(int id , String category){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_5 , category);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updatePriority(int id , String priority){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_6 , priority);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateDueDate(int id , String dueDate){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_7 , dueDate);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<ToDoModel> getAllTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME , null , null , null , null , null , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTitle(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_3)));
                        task.setStatus(cursor.getString(cursor.getColumnIndex(COL_4)));
                        task.setCategory(cursor.getString(cursor.getColumnIndex(COL_5)));
                        task.setPriority(cursor.getString(cursor.getColumnIndex(COL_6)));
                        task.setDueDate(cursor.getString(cursor.getColumnIndex(COL_7)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}







