package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    public final String databaseName = "Creditors";

    @BindView(R.id.button_creditors) Button button_creditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);

        doesDatabaseExist(this, "test");

    }

    public void startCreditorsActivity(View view)
    {
        Intent intent = new Intent(this, CreditorsActivity.class);
        startActivity(intent);
    }

    public void createDatabase()

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}
