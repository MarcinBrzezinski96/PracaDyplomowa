package com.example.marcin.pracadyplomowa;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.marcin.pracadyplomowa.MainActivity.CHANNEL_ID;

public class Service extends android.app.Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
/*
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .build();

        startForeground(1, notification);
        */

        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();

/*
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("501773821", null, "Test", null, null);
*/

        DatabaseManager databaseManager = new DatabaseManager(this);
        Cursor tabel = databaseManager.TakeActiveCreditors();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        int dodanaWartosc = Calendar.WEEK_OF_MONTH;

        if(tabel.moveToFirst()){
            do{

                try {
                        String dateString = tabel.getString(5);
                        int id = tabel.getInt(0);
                        boolean notified = Boolean.parseBoolean(tabel.getString(10));
                        Date creditorDate = dateFormat.parse((dateString));

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(dodanaWartosc, 1);
                        Date dateRemaining = calendar.getTime();

                        if(creditorDate.before(dateRemaining)){
                            try {
                                if(!notified) {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                    databaseManager.NotifyCreditor(id);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }while(tabel.moveToNext());
        }





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


