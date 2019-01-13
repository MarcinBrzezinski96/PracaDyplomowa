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
                        "id integer primary key autoincrement, " +//0
                        "imie text," +//1
                        "nazwisko text," +//2
                        "telefon int not null,"+//3
                        "wartosc_dlugu double not null,"+//4
                        "data date not null," +//5
                        "cyklicznosc int not null," +//6
                        "czy_dluznik boolean not null," +//7
                        "czy_aktywni boolean default 1," +//8
                        "liczba_zaplat int not null default 1," +//9
                        "czy_powiadomiony boolean default 0)"//10
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

    public void AddCreditor(String imie, String nazwisko, int telefon, double wartosc_dlugu, Calendar data, int cyklicznosc, boolean czy_dluznik, int liczba_zaplat)
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
        values.put("liczba_zaplat", liczba_zaplat);
        db.insertOrThrow("Creditors", null ,values);
    }

    public void DeleteCreditor(int idCreditor)
    {

        SQLiteDatabase db = getWritableDatabase();

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

    public Cursor TakeActiveCreditors()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor table = db.rawQuery("select * from Creditors WHERE czy_aktywni=1", null);
        return table;
    }
    public Cursor TakeOneCreditor(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor creditor = db.rawQuery("SELECT * FROM Creditors WHERE id= ?" , new String[] {String.valueOf(id)});
        return creditor;
    }


    public void UpdateOneCreditor(int id, String imie, String nazwisko, int telefon, double wartosc_dlugu, Calendar data, int cyklicznosc, boolean czy_dluznik, int liczba_zaplat)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.format(data.getTime());

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Creditors SET imie='"+imie+"', nazwisko='"+nazwisko+"', telefon='"+telefon+"', wartosc_dlugu='"+wartosc_dlugu+"', data='"+dateFormat.format(data.getTime())+"', cyklicznosc='"+cyklicznosc+"', czy_dluznik='"+czy_dluznik+"', liczba_zaplat='"+liczba_zaplat+"' WHERE id=" +id);

    }

    public void NotifyCreditor(int id)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("czy_powiadomiony", 1);
        db.update("Creditors", args, "id=" + id, null);
    }



}
