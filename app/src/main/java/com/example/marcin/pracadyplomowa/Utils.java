package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

public class Utils extends AppCompatActivity {


    public String messageFormater(String message, int id)
    {
        DatabaseManager dbManager = new DatabaseManager(Utils.this);
        Cursor creditor = dbManager.TakeOneCreditor(id);

        message.replace("{kwota}", creditor.getString(5));
        message.replace("{data}", creditor.getString(5));

        return message;
    }
}
