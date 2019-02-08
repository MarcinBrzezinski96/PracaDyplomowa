package com.example.marcin.pracadyplomowa;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

public class NotifyActivity extends AppCompatActivity {

    private SharedPreferences preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        TextInputEditText textInputEditTextSMS = findViewById(R.id.textInputSMS);
        TextInputEditText textInputEditTextNumberOfDays = findViewById(R.id.textInputNumberOfDaydBeforePaymentToNotify);
        final Switch radioButtonIfSendSMS = findViewById(R.id.radioButtonIfSendSMS);
        final Switch radioButtonIfNotify = findViewById(R.id.radioButtonIfNotify);


        preferencesManager = PreferenceManager.getDefaultSharedPreferences(NotifyActivity.this);


        textInputEditTextSMS.setText(preferencesManager.getString("sms", "Nie można wyświetliść treści sms"));
        textInputEditTextNumberOfDays.setText(String.valueOf(preferencesManager.getInt("DaysBeforeNotification", 0)));
        radioButtonIfSendSMS.setChecked(preferencesManager.getBoolean("IfSendSMS", true));
        radioButtonIfNotify.setChecked(preferencesManager.getBoolean("IfShowNotify", true));
    }

    @Override
    protected void onStop() {
        super.onStop();

        TextInputEditText textInputEditTextSMS = findViewById(R.id.textInputSMS);
        TextInputEditText textInputEditTextNumberOfDays = findViewById(R.id.textInputNumberOfDaydBeforePaymentToNotify);
        Switch radioButtonIfSendSMS = findViewById(R.id.radioButtonIfSendSMS);
        Switch radioButtonIfNotify = findViewById(R.id.radioButtonIfNotify);

        Boolean ifNotify;
        Boolean ifSendSMS;

        String smsText = String.valueOf(textInputEditTextSMS.getText());

        String var = String.valueOf(textInputEditTextNumberOfDays.getText());
        int numberOfDaysBeforePaymentToNotify = Integer.valueOf(var);


        if(radioButtonIfSendSMS.isChecked())
        {
            ifSendSMS = true;
        }
        else
        {
            ifSendSMS = false;
        }


        if(radioButtonIfNotify.isChecked())
        {
            ifNotify = true;
        }
        else
        {
            ifNotify = false;
        }

        NotifySharedPreferences notifySharedPreferences = new NotifySharedPreferences(this);

        notifySharedPreferences.setPreferences(smsText, numberOfDaysBeforePaymentToNotify, ifSendSMS, ifNotify);
    }

}

