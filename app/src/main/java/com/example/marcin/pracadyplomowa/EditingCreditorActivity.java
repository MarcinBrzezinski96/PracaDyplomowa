package com.example.marcin.pracadyplomowa;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditingCreditorActivity extends AddEditCreditor {


    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadValues();

    }


    public void loadValues()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        final DatabaseManager dbM = new DatabaseManager(EditingCreditorActivity.this);
        Cursor creditor = dbM.TakeOneCreditor(id);

        if (creditor.moveToFirst()) {
            do {
                textInputName.setText(creditor.getString(1));
                textInputSurname.setText(creditor.getString(2));
                textInputAmount.setText(creditor.getString(4));
                textInputPhone.setText(creditor.getString(3));
                textInputDate.setText(creditor.getString(5));
                spinner.setSelection(Integer.parseInt(creditor.getString(6)));
                textImputNumberOfPayment.setText(creditor.getString(9));
                if(creditor.getString(7).equals("1"))
                {
                    radioButtonDebtor.setChecked(true);
                }
                else
                {
                    radioButtonCreditor.setChecked(true);
                }
                try {
                    datePicker.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(creditor.getString(5)).getTime(), true, true);
                    calendarDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(creditor.getString(5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (creditor.moveToNext());
        }
    }

    @Override
    void saveToDatabase() {
        DatabaseManager dbManager = new DatabaseManager(EditingCreditorActivity.this);
        dbManager.UpdateOneCreditor(id, imie, nazwisko, telefon, dlug, calendarDate,  cyklicznosc, czyDluznik, iloscPlatnosci);
        finish();

    }


}
