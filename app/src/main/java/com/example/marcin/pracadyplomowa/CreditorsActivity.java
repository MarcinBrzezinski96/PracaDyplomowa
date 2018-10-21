package com.example.marcin.pracadyplomowa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditorsActivity extends AppCompatActivity {

    @BindView(R.id.linearLayout_creditors) LinearLayout linearLayout_creditors;
    @BindView(R.id.scrollView_creditors) ScrollView scrollView_rceditors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(CreditorsActivity.this);

/*
        //added LayoutParams
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams firstLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout_creditors.setOrientation(LinearLayout.VERTICAL);

        //add textView
        TextView textView = new TextView(CreditorsActivity.this);
        textView.setText("Theours");
        textView.setId(1);
        textView.setLayoutParams(firstLabelParams);

        // added Button
        Button button = new Button(CreditorsActivity.this);
        button.setText("thyours");
        button.setId(2);
        button.setLayoutParams(buttonParams);

        //added the textView and the Button to LinearLayout
        linearLayout_creditors.addView(textView);
        linearLayout_creditors.addView(button);
*/

        LinearLayout.LayoutParams firstLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout_creditors.setOrientation(LinearLayout.VERTICAL);


        DatabaseManager dbM = new DatabaseManager(this);
        Cursor tabela = dbM.TakeCreditors();

        int tableLength = 0;
        if (tabela.moveToFirst()){
            do {
                TextView name = new TextView(CreditorsActivity.this);
                name.setText(tabela.getString(1) + " " + tabela.getString(2));
                name.setId(tableLength);
                name.setLayoutParams(firstLabelParams);

                String cyklicznosc = tabela.getString(5);
                if(cyklicznosc == "1")
                {
                    cyklicznosc = "Cykliczny";
                }
                else if(cyklicznosc == "0");
                {
                    cyklicznosc = "Jednorazowy";
                }

                TextView date = new TextView(CreditorsActivity.this);
                date.setText("Termin płatności: " + tabela.getString(4));
                date.setId(tableLength+1);
                date.setLayoutParams(firstLabelParams);

                TextView description = new TextView(CreditorsActivity.this);
                description.setText("Suma: " + tabela.getString(3) +"zł" +" Klient " + cyklicznosc);
                description.setId(tableLength+2);
                description.setLayoutParams(firstLabelParams);

                TextView line = new TextView(CreditorsActivity.this);
                line.setHeight(3);
                line.setBackgroundColor(Color.parseColor("#B3B3B3"));

                linearLayout_creditors.addView(name);
                linearLayout_creditors.addView(date);
                linearLayout_creditors.addView(description);
                linearLayout_creditors.addView(line);
                tableLength++;
                tableLength++;
                tableLength++;
            } while(tabela.moveToNext());
        }
        for (int i = 0; i < tableLength; i += 3 )
        {
            TextView pierwszy = findViewById(i);
            TextView drugi = findViewById(i+1);
            TextView trzeci = findViewById(i+2);
            pierwszy.setTextSize(27);
            drugi.setTextSize(20);
            trzeci.setTextSize(18);
            trzeci.setPadding(0,0,0,32);


        }
    }


    public void startAddingCreditorsActivity(View view)
    {
        Intent intent = new Intent(CreditorsActivity.this, AddingCreditorActivity.class);
        startActivity(intent);
    }
}
