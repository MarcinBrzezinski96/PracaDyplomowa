package com.example.marcin.pracadyplomowa;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotifyActivity extends AppCompatActivity {

    // initialize SharedPreferences var
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        // get or create SharedPreferences
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        // save your string in SharedPreferences
        //sharedPref.edit().putString("user_id", userIDString).commit();

        //sharedPref.contains()

        String userId = sharedPref.getString("user_id", "default if empty");

    }
}
