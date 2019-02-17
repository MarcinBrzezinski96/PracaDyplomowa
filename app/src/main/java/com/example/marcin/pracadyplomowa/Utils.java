package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

public class Utils extends AppCompatActivity {


    public String messageFormater(String message, int id)
    {
        DatabaseManager dbManager = new DatabaseManager(Utils.this);
        Cursor creditor = dbManager.TakeOneCreditor(id);

        message.replace("{kwota}", creditor.getString(4) + "zł");
        message.replace("{data}", creditor.getString(5));

        return message;
    }

    public String translateMonth(int month)
    {
        String monthPolish = "";

        switch(month){
            case 0:
                monthPolish = "Styczeń";
                break;
            case 1:
                monthPolish = "Luty";
                break;
            case 2:
                monthPolish = "Marzec";
                break;
            case 3:
                monthPolish = "Kwiecień";
                break;
            case 4:
                monthPolish = "Maj";
                break;
            case 5:
                monthPolish = "Czerwiec";
                break;
            case 6:
                monthPolish = "Lipiec";
                break;
            case 7:
                monthPolish = "Śierpień";
                break;
            case 8:
                monthPolish = "Wrzesień";
                break;
            case 9:
                monthPolish = "Październik";
                break;
            case 10:
                monthPolish = "Listopad";
                break;
            case 11:
                monthPolish = "Grudzień";
                break;
            default:
                monthPolish = "";
        }

        return monthPolish;
    }
}
