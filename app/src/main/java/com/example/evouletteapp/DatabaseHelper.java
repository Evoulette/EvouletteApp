package com.example.evouletteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.evouletteapp.objects.Ticket;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";

    public static final String Col1 = "ID";
    public static final String Col2 = "name";
    public static final String Col3 = "location";
    public static final String Col4 = "date";
    public static final String Col5 = "time";
    public static final String Col6 = "description";

    public static final String path = Environment.getExternalStorageDirectory().getPath();


    public static final String TABLE_NAME = "Tickets";
    

    public DatabaseHelper( Context context) {
        super(context, path+TABLE_NAME, null, 7);
        SQLiteDatabase.openOrCreateDatabase(path +TABLE_NAME,null );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Col2 + " TEXT," + Col3 + " TEXT," + Col4 + " TEXT," + Col5 + " TEXT," + Col6 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String name, String location, String date, String time, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2, name);
        contentValues.put(Col3, location);
        contentValues.put(Col4, date);
        contentValues.put(Col5, time);
        contentValues.put(Col6, description);

        Log.i(TAG, "addData: Adding " + name + " ," + location+ " ," +date+ " ," +time+ " ," +description +" to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean deleteData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Col1+"="+id, null);
        Log.i(TAG, "item gelöscht "+id);
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Ticket getTicketById(int ticketId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=null;
        Ticket ticket = null;
        cursor =  db.rawQuery("select * from " + TABLE_NAME + " where " + Col1 + "=" + ticketId  , null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int id = cursor.getInt(cursor.getColumnIndex(Col1));
                String name = cursor.getString(cursor.getColumnIndex(Col2));
                String location = cursor.getString(cursor.getColumnIndex(Col3));
                String date = cursor.getString(cursor.getColumnIndex(Col4));
                String time = cursor.getString(cursor.getColumnIndex(Col5));
                String description = cursor.getString(cursor.getColumnIndex(Col6));

                ticket=new Ticket();
                ticket.setId(id);
                ticket.setName(name);
                ticket.setLocation(location);
                ticket.setDate(date);
                ticket.setTime(time);
                ticket.setDescription(description);

            }
            cursor.close();
        }
        return ticket;

    }
}
