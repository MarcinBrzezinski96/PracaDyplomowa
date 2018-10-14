package com.example.marcin.pracadyplomowa;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Creditors (" +
                        "nr integer primary key autoincrement, " +
                        "imie text," +
                        "nazwisko text," +
                        "wartosc_dlugu double not null,"+
                        //"data date not null," +
                        "cyklicznosc boolean not null," +
                        "czy_dluznik boolean not null)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void AddCreditor(String imie, String nazwisko, double wartosc_dlugu, boolean cyklicznosc, boolean czy_dluznik)
    {
//TODO popraw date

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imie", imie);
        values.put("nazwisko", nazwisko);
        values.put("wartosc_dlugu", wartosc_dlugu);
        //values.put("data", data);
        values.put("cyklicznosc", cyklicznosc);
        values.put("czy_dluznik", czy_dluznik);
        db.insertOrThrow("Creditors", null ,values);
    }
}
