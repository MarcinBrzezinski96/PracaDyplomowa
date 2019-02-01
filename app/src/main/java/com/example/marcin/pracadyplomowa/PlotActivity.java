package com.example.marcin.pracadyplomowa;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar date = GregorianCalendar.getInstance();
        String currdate = sdf.format(date.getTime());

        date.add(Calendar.MONTH, 1);

        int days = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);

        int actualDay = date.get(Calendar.DAY_OF_MONTH);
        int actualMont = date.get(Calendar.MONTH);

        DatabaseManager dbM = new DatabaseManager(this);

        /*
        zrób pętle w ktorej będziesz dodawał dni do końca miesiąca.
        każdy krok pętli to dodany dzień.
        Przy każdym kroku bierzesz każdego klienta z osobna i robisz petle w ktorej przewidujesz wszystkie wplaty i daty, jesli jakis krok petli pokrywa sie data z aktualnym krokiem petli wczesniejszej to dodajesz.
         */

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(days);

        graph.getViewport().setMinX(actualDay);
        graph.getViewport().setMaxX(days+1);
        graph.getViewport().setXAxisBoundsManual(true);


        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Dni");
        gridLabel.setVerticalAxisTitle("Zł");

        List<Double> paymentValues = new ArrayList<>();

        Date creditorDate = null;
        Date currentDate = null;

        for(int i = 0; i <= days - actualDay; i++)
        {
            Cursor tabela = dbM.TakeActiveCreditors();
            paymentValues.add(i, (double) 0);
            if (tabela.moveToFirst()) {
                do {

                    int numberOfPayments = tabela.getInt(9);
                    int periodicity = tabela.getInt(6);


                    try {
                        creditorDate = sdf.parse(tabela.getString(5));
                        currentDate = sdf.parse(currdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    while (numberOfPayments >= 1) {
                        if (creditorDate.compareTo(currentDate) == 0) {
                            int isCreditor = 1;
                            if (tabela.getString(7).equals("0")) {
                                isCreditor = -1;
                            }

                            try{
                                paymentValues.set(i, paymentValues.get(i) + (isCreditor * tabela.getDouble(4)));
                            }
                            catch (Exception e)
                            {
                                paymentValues.set(i, (isCreditor * tabela.getDouble(4)));
                            }

                        }
                        /*
                        else
                        {
                            paymentValues.set(i, (double) 0);
                        }
                        */


                        Calendar cal = Calendar.getInstance();
                        cal.setTime(creditorDate);


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



                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(cal.getTime());
                        String calDate = sdf.format(calendar.getTime());

                        try {
                            creditorDate = sdf.parse(calDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        numberOfPayments -= 1;

                    }

                } while (tabela.moveToNext());
            }


            date.add(Calendar.DAY_OF_MONTH, 1);
            currdate = sdf.format(date.getTime());

            try {
                currentDate = sdf.parse(currdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        int xPoints = days - actualDay;

        DataPoint[] dp = new DataPoint[xPoints+1];
        for(int i=0; i<=xPoints; i++){
            try {
                if(i > 0)
                {
                    paymentValues.set(i, paymentValues.get(i) + paymentValues.get(i-1));
                }
                dp[i] = new DataPoint(i + actualDay, paymentValues.get(i));
            }
            catch(Exception e)
            {
                dp[i] = new DataPoint(i + actualDay, 0);
            }
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);



/*
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(10, -200),
                new DataPoint(12, 200),
                new DataPoint(31, -200),
        });
*/
        graph.addSeries(series);



    }
}
