package com.example.marcin.pracadyplomowa;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CalendarAcitivity extends AppCompatActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat(("MMMM- yyyy"), Locale.getDefault());
    DatabaseManager dbM;
    Cursor tabela;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LinearLayout.LayoutParams firstLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    String czyAktywny;
    int tableLength = 0;
    LinearLayout linearLayout_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        linearLayout_calendar = findViewById(R.id.linearLayout_calendarActivity);

        dbM = new DatabaseManager(CalendarAcitivity.this);
        tabela = dbM.TakeActiveCreditors();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October

        Event ev1 = new Event(Color.RED, 1542286683000L, "Teachers' Professional Day");
        compactCalendar.addEvent(ev1);

        addEvents();

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                removeCreditors();

                Long dateStr = dateClicked.getTime();
                String dateClick = dateFormat.format(dateStr);

                showCreditors(dateClick);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

    }

    public void addEvents() {
        if (tabela.moveToFirst()) {
            do {
                String dateString = tabela.getString(5);
                String imie = tabela.getString(1);

                try {
                    Date date = dateFormat.parse(dateString);

                    long startDate = date.getTime();

                    Event ev1 = new Event(Color.RED, startDate, imie);
                    compactCalendar.addEvent(ev1);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (tabela.moveToNext());

        }
    }

    public void removeCreditors(){
        linearLayout_calendar.removeAllViews();
        tableLength = 0;
    }

    public void showCreditors(String dateClick){

        if (tabela.moveToFirst()) {
            do {
                String dateCreditor = tabela.getString(5);
                if(dateClick.equals(dateCreditor)) {
                    final TextView name = new TextView(CalendarAcitivity.this);
                    name.setText(tabela.getString(1) + " " + tabela.getString(2));
                    name.setId(tableLength);
                    name.setLayoutParams(firstLabelParams);

                    String cyklicznosc = tabela.getString(6);
                    String czyDluznik = tabela.getString(7);


                    if (cyklicznosc.equals("0"))
                    {
                        cyklicznosc = "Jednorazowy";
                    }
                    else if(cyklicznosc.equals("1"))
                    {
                        cyklicznosc = "Tygodniowo";
                    }
                    else if(cyklicznosc.equals("2"))
                    {
                        cyklicznosc = "Dwutygodniowo";
                    }
                    else if(cyklicznosc.equals("3"))
                    {
                        cyklicznosc = "Miesiecznie";
                    }
                    else if(cyklicznosc.equals("4"))
                    {
                        cyklicznosc = "Kwartalnie";
                    }
                    else if(cyklicznosc.equals("5"))
                    {
                        cyklicznosc = "Półrocznie";
                    }
                    else if(cyklicznosc.equals("6"))
                    {
                        cyklicznosc = "Rocznie";
                    }

                    if (czyDluznik.equals("1")) {
                        czyDluznik = "Dłużnik";
                    } else {
                        czyDluznik = "Wierzyciel";
                    }

                    final TextView date = new TextView(CalendarAcitivity.this);
                    date.setText("Termin płatności: " + tabela.getString(5));
                    date.setId(tableLength + 1);
                    date.setLayoutParams(firstLabelParams);

                    final TextView amount = new TextView(CalendarAcitivity.this);
                    amount.setText("Suma: " + tabela.getString(4) + "zł");
                    amount.setId(tableLength + 2);
                    amount.setLayoutParams(firstLabelParams);

                    final TextView phoneNumber = new TextView(CalendarAcitivity.this);
                    phoneNumber.setText("Numer telefonu: " + tabela.getString(3));
                    phoneNumber.setId(tableLength + 3);
                    phoneNumber.setLayoutParams(firstLabelParams);

                    final TextView periodicity = new TextView(CalendarAcitivity.this);
                    periodicity.setText("Klient " + cyklicznosc);
                    periodicity.setId(tableLength + 4);
                    periodicity.setLayoutParams(firstLabelParams);

                    final TextView status = new TextView((CalendarAcitivity.this));
                    status.setText("Status: " + czyDluznik);
                    status.setId(tableLength + 5);
                    status.setLayoutParams(firstLabelParams);

                    TextView line = new TextView(CalendarAcitivity.this);
                    line.setHeight(3);
                    line.setBackgroundColor(Color.parseColor("#B3B3B3"));


                    czyAktywny = tabela.getString(8);
                    if (czyAktywny.equals("1")) {
                        linearLayout_calendar.addView(name);
                        linearLayout_calendar.addView(date);
                        linearLayout_calendar.addView((phoneNumber));
                        linearLayout_calendar.addView(amount);
                        linearLayout_calendar.addView(periodicity);
                        linearLayout_calendar.addView(status);
                        linearLayout_calendar.addView(line);

                        TextView rowName = findViewById(tableLength);
                        TextView rowDate = findViewById(tableLength + 1);
                        TextView rowPhone = findViewById(tableLength + 2);
                        TextView rowAmount = findViewById(tableLength + 3);
                        TextView rowPeriodicity = findViewById(tableLength + 4);
                        TextView rowStatus = findViewById(tableLength + 5);
                        rowName.setTextSize(27);
                        rowDate.setTextSize(20);
                        rowPhone.setTextSize(18);
                        rowAmount.setTextSize(18);
                        rowPeriodicity.setTextSize(18);
                        rowStatus.setTextSize(18);
                        rowStatus.setPadding(0, 0, 0, 32);

                    }
                    tableLength++;
                    tableLength++;
                    tableLength++;
                    tableLength++;
                    tableLength++;
                    tableLength++;
                }

            } while (tabela.moveToNext());
        }

    }

}
