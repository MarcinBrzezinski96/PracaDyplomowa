package com.example.marcin.pracadyplomowa;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    public final String databaseName = "Creditors";

    public static final String CHANNEL_ID = "serviceceChannel";

    @BindView(R.id.button_creditors) Button button_creditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if(doesDatabaseExist( this, databaseName)) {
            createDatabase(databaseName);
        }

        askSMSPermission();

        NotifySharedPreferences notifySharedPreferences = new NotifySharedPreferences(this);
        notifySharedPreferences.firstCreate();

        Intent serviceIntent = new Intent(this, Service.class);
        startService(serviceIntent);

        Calendar cal = Calendar.getInstance();

        Intent intent = new Intent(this, Service.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 8 hours
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                 8*60*60*1000, pendingIntent);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informacje o aplikacji");
        builder.setMessage(R.string.info);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);
    }


    public void startCreditorsActivity(View view)
    {
        Intent intent = new Intent(this, CreditorsActivity.class);
        startActivity(intent);
    }

    public void startCalendarActivity(View view){
        Intent intent = new Intent(this, CalendarAcitivity.class);
        startActivity(intent);
    }

    public void startNotifyActivity(View view){
        Intent intent = new Intent(this, NotifyActivity.class);
        startActivity(intent);
    }

    public void startPlotActivity(View view){
        Intent intent = new Intent(this, PlotActivity.class);
        startActivity(intent);
    }

    public void createDatabase(String databaseName)
    {
        DatabaseManager dbManager = new DatabaseManager(this);
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public void askSMSPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pozwolenie na dostęp");
            builder.setMessage(R.string.permission_info);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS))
                    {

                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
                    }

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else {
            // Permission has already been granted
        }
    }

}
