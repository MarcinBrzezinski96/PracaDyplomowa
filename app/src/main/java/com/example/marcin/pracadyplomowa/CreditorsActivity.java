package com.example.marcin.pracadyplomowa;

import android.content.Intent;
import android.database.Cursor;
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

        if (tabela.moveToFirst()){
            int i = 1;
            do {
                TextView name = new TextView(CreditorsActivity.this);
                name.setText(tabela.getString(1) + " " + tabela.getString(2));
                name.setId(i);
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

                TextView description = new TextView(CreditorsActivity.this);
                description.setText("Termin: " + tabela.getString(4) + " Suma: " + tabela.getString(3) + cyklicznosc);
                description.setId(i+1);
                description.setLayoutParams(firstLabelParams);

                linearLayout_creditors.addView(name);
                linearLayout_creditors.addView(description);
                i++;
                i++;
            } while(tabela.moveToNext());
        }

        TextView pierwszy = findViewById(1);
        TextView drugi = findViewById(2);
        pierwszy.setTextSize(20);
        drugi.setTextSize(10);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditorsActivity.this, AddingCreditorActivity.class);
                startActivity(intent);
            }
        });

    }

}
