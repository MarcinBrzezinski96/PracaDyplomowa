package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class NotifySharedPreferences {

    private Context context;
    private SharedPreferences preferencesManager;
    private SharedPreferences.Editor preferencesEditor;

    NotifySharedPreferences(Context context)
    {
        this.context = context;
        preferencesManager = PreferenceManager.getDefaultSharedPreferences(context);
        preferencesEditor = preferencesManager.edit();
    }

    public void firstCreate()
    {
        if(!preferencesManager.contains("sms"))
        {
            preferencesEditor.putString("sms", "tesc sms'a2");
            preferencesEditor.putInt("DaysBeforeNotification", 0);
            preferencesEditor.putBoolean("IfSendSMS", true);
            preferencesEditor.putBoolean("IfShowNotify", true);
            preferencesEditor.commit();
        }
    }

    public void setPreferences(String smsString, int daysBeforeNotification,Boolean sendSMS, Boolean showNotify)
    {
        preferencesEditor.putString("sms", smsString);
        preferencesEditor.putInt("DaysBeforeNotification", daysBeforeNotification);
        preferencesEditor.putBoolean("IfSendSMS", sendSMS);
        preferencesEditor.putBoolean("IfShowNotify", showNotify);
        preferencesEditor.commit();
    }

    public String getSMSString()
    {
        return preferencesManager.getString("sms", "null");
    }

    // initialize SharedPreferences var
    //SharedPreferences sharedPref;


    // get or create SharedPreferences
    //sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

    // save your string in SharedPreferences
    //sharedPref.edit().putString("user_id", userIDString).commit();

    //sharedPref.contains()

    //String userId = sharedPref.getString("user_id", "default if empty");

}
