package com.example.marcin.pracadyplomowa;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    FloatingActionButton monthBeforeActionButton;
    FloatingActionButton monthAfterActionButton;
    TextView currentMonth;
    int monthAddedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        final Utils utils = new Utils();
        currentMonth = findViewById(R.id.currentMonth);
        monthBeforeActionButton = findViewById(R.id.monthBefore);
        monthAfterActionButton = findViewById(R.id.monthAfter);
        final GraphView graph = (GraphView) findViewById(R.id.graph);


        monthAddedValue = 0;

        List<Object> calculateGraphsValues = calculateGraphsValues(monthAddedValue);

        drawGraph(calculateGraphsValues);

        Calendar date = (Calendar) calculateGraphsValues.get(3);
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        String monthTranslated = utils.translateMonth(month-1);
        currentMonth.setText(monthTranslated + " - " + String.valueOf(year));


        monthBeforeActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthAddedValue >= 1) {
                    monthAddedValue -= 1;
                    graph.removeAllSeries();
                    List<Object> calculateGraphsValues = calculateGraphsValues(monthAddedValue);
                    drawGraph(calculateGraphsValues);
                    Calendar date = (Calendar) calculateGraphsValues.get(3);
                    int year = date.get(Calendar.YEAR);
                    int month = date.get(Calendar.MONTH);
                    String monthTranslated = utils.translateMonth(month-1);
                    currentMonth.setText(monthTranslated + " - " + String.valueOf(year));
                }
            }
        });

        monthAfterActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthAddedValue += 1;
                graph.removeAllSeries();
                List<Object> calculateGraphsValues = calculateGraphsValues(monthAddedValue);
                drawGraph(calculateGraphsValues);
                Calendar date = (Calendar) calculateGraphsValues.get(3);
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                String monthTranslated = utils.translateMonth(month-1);
                currentMonth.setText(monthTranslated + " - " + String.valueOf(year));
            }
        });

    }

    List<Object> calculateGraphsValues(int monthAddedValue)
    {
        Calendar date = GregorianCalendar.getInstance();
        date.add(Calendar.MONTH, monthAddedValue);
        String currdate = sdf.format(date.getTime());

        int days = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);

        int actualDay;
        if(monthAddedValue == 0) {
            actualDay = date.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            actualDay = 1;
            int varActualDay = -((date.get(Calendar.DAY_OF_MONTH))-1);
            date.add(Calendar.DAY_OF_MONTH, varActualDay);
            currdate = sdf.format(date.getTime());
        }
        int actualMont = date.get(Calendar.MONTH);

        DatabaseManager dbM = new DatabaseManager(this);

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



        return Arrays.asList(days, actualDay, paymentValues, date);
    }


    public void drawGraph(List<Object> calculateGraphsValues)
    {
        int days = (int) calculateGraphsValues.get(0);
        int actualDay = (int) calculateGraphsValues.get(1);
        List<Double> paymentValues = (List<Double>) calculateGraphsValues.get(2);

        int xPoints = days - actualDay;

        GraphView graph = (GraphView) findViewById(R.id.graph);

        //graph.getViewport().setMinX(days);

        graph.getViewport().setMinX(actualDay);
        graph.getViewport().setMaxX(days+1);
        //graph.getViewport().setMinY(100);
        //graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Dni");
        gridLabel.setVerticalAxisTitle("Zł");



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
