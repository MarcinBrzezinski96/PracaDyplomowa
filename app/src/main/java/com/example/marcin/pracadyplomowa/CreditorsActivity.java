package com.example.marcin.pracadyplomowa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditorsActivity extends AppCompatActivity {

    @BindView(R.id.linearLayout_creditors)
        LinearLayout linearLayout_creditors;
    @BindView(R.id.scrollView_creditors)
        ScrollView scrollView_rceditors;
    //@BindView(R.id.button_editCreditor)
    //        Button editButton;

    int credditorsLabelsNumber = 6;
    int tableLength = 0;
    int focusedCreditorId;
    String czyAktywny;
    LinearLayout.LayoutParams firstLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    final DatabaseManager dbM = new DatabaseManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(CreditorsActivity.this);
        Button deleteButton = findViewById(R.id.button_deleteCreditor);
        Button editButton = findViewById(R.id.button_editCreditor);

        linearLayout_creditors.setOrientation(LinearLayout.VERTICAL);

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(focusedCreditorId >= 0) {
                    dbM.DeleteCreditor(focusedCreditorId);
                    reloadingCreditors();
                }
                else
                {
                    Toast.makeText(CreditorsActivity.this, "Należy najpierw wybrać usobę z listy", Toast.LENGTH_LONG).show();
                }
        }
        });

        editButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(focusedCreditorId >= 0) {
                    Intent intent = new Intent(CreditorsActivity.this, EditingCreditorActivity.class);
                    intent.putExtra("id", focusedCreditorId);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(CreditorsActivity.this, "Należy najpierw wybrać usobę z listy", Toast.LENGTH_LONG).show();
                }
            }
        });

        showCreditors();

    }

    @Override
    public void onResume() {
        super.onResume();
        reloadingCreditors();
    }

    public void reloadingCreditors(){
        linearLayout_creditors.removeAllViews();
        tableLength = 0;
        showCreditors();
    }

    public void startAddingCreditorsActivity(View view) {
        Intent intent = new Intent(CreditorsActivity.this, AddingCreditorActivity.class);
        startActivity(intent);
    }

    public void showCreditors() {

        Cursor tabela = dbM.TakeActiveCreditors();
        if (tabela.moveToFirst()) {
            do {
                final TextView name = new TextView(CreditorsActivity.this);
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

                final TextView date = new TextView(CreditorsActivity.this);
                date.setText("Termin płatności: " + tabela.getString(5));
                date.setId(tableLength + 1);
                date.setLayoutParams(firstLabelParams);

                final TextView amount = new TextView(CreditorsActivity.this);
                amount.setText("Suma: " + tabela.getString(4) + "zł");
                amount.setId(tableLength + 2);
                amount.setLayoutParams(firstLabelParams);

                final TextView phoneNumber = new TextView(CreditorsActivity.this);
                phoneNumber.setText("Numer telefonu: " + tabela.getString(3));
                phoneNumber.setId(tableLength + 3);
                phoneNumber.setLayoutParams(firstLabelParams);

                final TextView periodicity = new TextView(CreditorsActivity.this);
                periodicity.setText("Klient " + cyklicznosc + "  (ilość płatności: " + tabela.getString(9) + ")");
                periodicity.setId(tableLength + 4);
                periodicity.setLayoutParams(firstLabelParams);

                final TextView status = new TextView((CreditorsActivity.this));
                status.setText("Status: " + czyDluznik);
                status.setId(tableLength + 5);
                status.setLayoutParams(firstLabelParams);

                TextView line = new TextView(CreditorsActivity.this);
                line.setHeight(3);
                line.setBackgroundColor(Color.parseColor("#B3B3B3"));

                final int finalTableLength = tableLength;
                name.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                date.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                amount.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                phoneNumber.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                periodicity.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                status.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFocusOnClient(finalTableLength);
                    }
                });

                czyAktywny = tabela.getString(8);

                if (czyAktywny.equals("1")) {
                    linearLayout_creditors.addView(name);
                    linearLayout_creditors.addView(date);
                    linearLayout_creditors.addView((phoneNumber));
                    linearLayout_creditors.addView(amount);
                    linearLayout_creditors.addView(periodicity);
                    linearLayout_creditors.addView(status);
                    linearLayout_creditors.addView(line);
                }
                tableLength++;
                tableLength++;
                tableLength++;
                tableLength++;
                tableLength++;
                tableLength++;
            } while (tabela.moveToNext());

            if (tabela.moveToFirst()) {
                int i = 0;
                do {
                    czyAktywny = tabela.getString(8);
                    if (czyAktywny.equals("1")) {
                        TextView rowName = findViewById(i);
                        TextView rowDate = findViewById(i + 1);
                        TextView rowPhone = findViewById(i + 2);
                        TextView rowAmount = findViewById(i + 3);
                        TextView rowPeriodicity = findViewById(i + 4);
                        TextView rowStatus = findViewById(i + 5);
                        rowName.setTextSize(27);
                        rowDate.setTextSize(20);
                        rowPhone.setTextSize(18);
                        rowAmount.setTextSize(18);
                        rowPeriodicity.setTextSize(18);
                        rowStatus.setTextSize(18);
                        rowStatus.setPadding(0, 0, 0, 32);
                    }
                    i += credditorsLabelsNumber;
                } while (tabela.moveToNext());
            }
        }
    }

    public void setFocusOnClient(int id) {
        int color = getResources().getColor(R.color.colorPrimary);

        final DatabaseManager dbM = new DatabaseManager(this);
        Cursor tabela = dbM.TakeCreditors();

        if (tabela.moveToFirst()) {
            int i = 0;
            do {
                czyAktywny = tabela.getString(8);
                if (czyAktywny.equals("1")) {
                    TextView tmpElement = findViewById(i);
                    TextView tmpElement2 = findViewById(i + 1);
                    TextView tmpElement3 = findViewById(i + 2);
                    TextView tmpElement4 = findViewById(i + 3);
                    TextView tmpElement5 = findViewById(i + 4);
                    TextView tmpElement6 = findViewById(i + 5);

                    tmpElement.setTextColor(Color.GRAY);
                    tmpElement2.setTextColor(Color.GRAY);
                    tmpElement3.setTextColor(Color.GRAY);
                    tmpElement4.setTextColor(Color.GRAY);
                    tmpElement5.setTextColor(Color.GRAY);
                    tmpElement6.setTextColor(Color.GRAY);
                }
                i += 6;
            } while (tabela.moveToNext());
        }

        if(!(focusedCreditorId == id / credditorsLabelsNumber + 1)) {
            TextView rowName = findViewById(id);
            TextView rowDate = findViewById(id + 1);
            TextView rowPhone = findViewById(id + 2);
            TextView rowAmount = findViewById(id + 3);
            TextView rowPeriodicity = findViewById(id + 4);
            TextView rowStatus = findViewById(id + 5);

            rowName.setTextColor(color);
            rowDate.setTextColor(color);
            rowPhone.setTextColor(color);
            rowAmount.setTextColor(color);
            rowPeriodicity.setTextColor(color);
            rowStatus.setTextColor(color);

            focusedCreditorId = id / credditorsLabelsNumber + 1;
        }
        else
        {
            focusedCreditorId = -1;
        }
    }

    }

