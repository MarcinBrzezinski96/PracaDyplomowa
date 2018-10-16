package com.example.marcin.pracadyplomowa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context)
    {
        super(context, "Creditors", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "create table Creditors (" +
                        "nr integer primary key autoincrement, " +
                        "imie text," +
                        "nazwisko text," +
                        "wartosc_dlugu double not null,"+
                        "data date not null," +
                        "cyklicznosc boolean not null," +
                        "czy_dluznik boolean not null)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void AddCreditor(String imie, String nazwisko, double wartosc_dlugu, Calendar data, boolean cyklicznosc, boolean czy_dluznik)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.format(data.getTime());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imie", imie);
        values.put("nazwisko", nazwisko);
        values.put("wartosc_dlugu", wartosc_dlugu);
        values.put("data", dateFormat.format(data.getTime()));
        values.put("cyklicznosc", cyklicznosc);
        values.put("czy_dluznik", czy_dluznik);
        db.insertOrThrow("Creditors", null ,values);
    }

    public Cursor TakeCreditors()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor table = db.rawQuery("select * from Creditors", null);
        return table;
    }

}
