package com.kazak.todolistmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TodoListSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "com.kazak.todo_db";
    public static final String TABLE_NAME = "todo";
    public static final String TASK_DB_COLUMN = "task";
    public static final String DUE_DATE_COLUMN = "due_date";
    public static final String _ID = BaseColumns._ID;

    public TodoListSQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String createTodoListTable = "CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_DB_COLUMN + " TEXT, "+ DUE_DATE_COLUMN+" LONG)";
        System.out.println(createTodoListTable);
        sqlDB.execSQL(createTodoListTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }
}