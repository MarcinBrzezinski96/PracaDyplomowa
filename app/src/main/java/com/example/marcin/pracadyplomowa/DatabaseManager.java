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
                        "id integer primary key autoincrement, " +
                        "imie text," +
                        "nazwisko text," +
                        "telefon int not null,"+
                        "wartosc_dlugu double not null,"+
                        "data date not null," +
                        "cyklicznosc int not null," +
                        "czy_dluznik boolean not null," +
                        "czy_aktywni boolean default 1)"
        );
    }

    /*
    cyklicznosc:
    0 = jednorazowy
    1 = tygodniowo
    2 = dwutygodniwo
    3 = miesiecznie
    4 = kwartalnie
    5 = polrocznie
    6 = rocznie
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void AddCreditor(String imie, String nazwisko, int telefon, double wartosc_dlugu, Calendar data, int cyklicznosc, boolean czy_dluznik)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.format(data.getTime());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imie", imie);
        values.put("nazwisko", nazwisko);
        values.put("telefon", telefon);
        values.put("wartosc_dlugu", wartosc_dlugu);
        values.put("data", dateFormat.format(data.getTime()));
        values.put("cyklicznosc", cyklicznosc);
        values.put("czy_dluznik", czy_dluznik);
        db.insertOrThrow("Creditors", null ,values);
    }

    public void deleteCreditor(int idCreditor)
    {

        SQLiteDatabase db = getWritableDatabase();
/*
        String strSQL = "UPDATE Creditors SET czy_aktywni = 0 WHERE id = "+ id;

        db.execSQL(strSQL);
*/
        ContentValues args = new ContentValues();
        args.put("czy_aktywni", 0);
        db.update("Creditors", args, "id=" + idCreditor, null);
    }

    public Cursor TakeCreditors()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor table = db.rawQuery("select * from Creditors", null);
        return table;
    }

}
