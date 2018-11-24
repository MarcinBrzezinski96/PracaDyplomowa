package com.example.marcin.pracadyplomowa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddingCreditorActivity extends AddEditCreditor {


    @Override
    void saveToDatabase() {
        DatabaseManager dbManager = new DatabaseManager(AddingCreditorActivity.this);
        dbManager.AddCreditor(imie, nazwisko, telefon, dlug, calendarDate,  cyklicznosc, czyDluznik, iloscPlatnosci);
        finish();

    }
}
