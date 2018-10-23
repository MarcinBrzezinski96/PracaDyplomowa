package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {


    public final String databaseName = "Creditors";

    @BindView(R.id.button_creditors) Button button_creditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        createDatabase(databaseName);

    }

    public void startCreditorsActivity(View view)
    {
        Intent intent = new Intent(this, CreditorsActivity.class);
        startActivity(intent);
    }

    public void createDatabase(String databaseName)
    {
        //getApplicationContext().deleteDatabase(databaseName);

        Calendar data = Calendar.getInstance();
/*
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.AddCreditor("imietest", "nazwiskotest", 111111111, 100, data, false, true);
        dbManager.AddCreditor("imietest2", "nazwiskotest2", 222222222, 1000, data, false, true);
        */

    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}
