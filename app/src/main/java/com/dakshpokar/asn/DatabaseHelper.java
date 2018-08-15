package com.dakshpokar.asn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "notes_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Title";
    private static final String COL3 = "Note";
    private static final String COL4 = "DateTime";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, "+ COL4 + " DATETIME)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String title, String note, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, title);
        contentValues.put(COL3, note);
        contentValues.put(COL4, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public void modifyData(int ID, String title, String info, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE notes_table SET title = '" + title + "', ";
        sql = sql + "note = '" + info + "'";
        sql = sql +" WHERE ID = " + String.valueOf(ID);
        db.execSQL(sql);
    }
    public void remove(int ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = " + ID;
        db.execSQL(sql);
    }
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_NAME, null, null);
    }
    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
