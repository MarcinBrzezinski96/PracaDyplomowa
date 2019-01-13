package com.example.marcin.pracadyplomowa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        Calendar date = GregorianCalendar.getInstance();
        int days = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(days);

        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(days);
        graph.getViewport().setXAxisBoundsManual(true);


        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Dni");
        gridLabel.setVerticalAxisTitle("Zł");


        DataPoint[] dp = new DataPoint[days];
        for(int i=0;i<days;i++){
            dp[i] = new DataPoint(i, i+1);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);



        /*
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 3),
                new DataPoint(2, 1)
        });
*/
        graph.addSeries(series);



    }
}
