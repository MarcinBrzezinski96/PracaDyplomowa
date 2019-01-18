package com.example.marcin.pracadyplomowa;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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



//        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();

/*
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("501773821", null, "Test", null, null);
*/

        DatabaseManager databaseManager = new DatabaseManager(this);
        Cursor tabel = databaseManager.TakeActiveCreditors();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        SharedPreferences preferencesManager = PreferenceManager.getDefaultSharedPreferences(this);


        ArrayList<String> creaditorsNotified = new ArrayList<>();

        if(tabel.moveToFirst()){
            do{
                try {
                        String dateString = tabel.getString(5);
                        int id = tabel.getInt(0);
                        String notified = tabel.getString(10);
                        Date creditorDate = dateFormat.parse((dateString));

                        Calendar calendar = Calendar.getInstance();
                        Date dateToInactive = calendar.getTime();
                        calendar.add(Calendar.DAY_OF_MONTH, preferencesManager.getInt("DaysBeforeNotification", 7));
                        Date dateRemaining = calendar.getTime();

                        if(creditorDate.before(dateRemaining)){
                            try {
                                if(notified.equals("0")) {
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                    databaseManager.NotifyCreditor(id);
                                    creaditorsNotified.add(tabel.getString(1) + " " + tabel.getString(2) + " - " + tabel.getString(4) + "zł");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(creditorDate.before(dateToInactive))
                        {
                            if(tabel.getInt(9) <= 1)
                            {
                                databaseManager.InactiveCreditor(id);
                            }
                            else
                            {
                                int periodicity = tabel.getInt(6);
                                Calendar cal = Calendar.getInstance();

                                if(periodicity == 1)
                                {
                                    cal.add(Calendar.DAY_OF_MONTH, 7);
                                }
                                else if(periodicity == 2)
                                {
                                    cal.add(Calendar.DAY_OF_MONTH, 14);
                                }
                                else if (periodicity == 3)
                                {
                                    cal.add(Calendar.MONTH, 1);
                                }
                                else if (periodicity == 4)
                                {
                                    cal.add(Calendar.MONTH, 3);
                                }
                                else if (periodicity == 5)
                                {
                                    cal.add(Calendar.MONTH, 6);
                                }
                                else if (periodicity == 6)
                                {
                                    cal.add(Calendar.MONTH, 12);
                                }

                                int numberOfPayments = tabel.getInt(9);
                                databaseManager.CountDownNumberOfPayments(id, numberOfPayments-1, cal);
                            }

                        }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }while(tabel.moveToNext());
        }


        if(!creaditorsNotified.isEmpty()) {
            createNotificationChannel();
            createNotification(creaditorsNotified);
            creaditorsNotified.clear();
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

    public void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Creditor Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            //manager.createNotificationChannel(serviceChannel);

        }
    }

    public void createNotification(ArrayList<String> creaditorsNotified)
    {
        Intent notificationIntent = new Intent(this, Service.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
/*
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Usuń", pendingIntent).build();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("test ttile")
                .setContentText(creaditorsNotified.toString())
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(action)
                .build();

        startForeground(1, notification);
*/

        // prepare intent which is triggered if the
// notification is selected

        Intent intent = new Intent(this, Service.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            n = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("New mail from " + "test@gmail.com")
                    .setContentText(creaditorsNotified.toString())
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.notification_icon, "Call", pIntent)
                    .addAction(R.drawable.notification_icon, "More", pIntent)
                    .addAction(R.drawable.notification_icon, "And more", pIntent).build();
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }

}


