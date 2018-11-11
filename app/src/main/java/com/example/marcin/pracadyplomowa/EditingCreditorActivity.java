package com.example.marcin.pracadyplomowa;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditingCreditorActivity extends AppCompatActivity {


    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        setContentView(R.layout.activity_adding_creditor);

        final CalendarView datePicker = findViewById(R.id.calendarView);
        final TextInputEditText textInputDate = findViewById(R.id.textInputDate);
        final TextInputEditText textInputName = findViewById(R.id.textInputName);
        final TextInputEditText textInputSurname = findViewById(R.id.textInputSurname);
        final TextInputEditText textInputAmount = findViewById(R.id.textInputAmount);
        final TextInputEditText textInputPhone = findViewById(R.id.textInputPhone);
        final RadioButton radioButtonDebtor = findViewById(R.id.radioButtonDebtor);
        final RadioButton radioButtonCreditor = findViewById(R.id.radioButtonCreditor);
        Button buttonSave = findViewById(R.id.buttonSave);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        textInputDate.setEnabled(false);
        final Calendar calendarDate = Calendar.getInstance();

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final DatabaseManager dbM = new DatabaseManager(EditingCreditorActivity.this);
        Cursor creditor = dbM.TakeOneCreditor(id);

        if (creditor.moveToFirst()) {
            do {
                    textInputName.setText(creditor.getString(1));
                    textInputSurname.setText(creditor.getString(2));
                    textInputAmount.setText(creditor.getString(4));
                    textInputPhone.setText(creditor.getString(3));
                    textInputDate.setText(creditor.getString(5));
                    spinner.setSelection(Integer.parseInt(creditor.getString(6)));
                    if(creditor.getString(7).equals("1"))
                    {
                        radioButtonDebtor.setChecked(true);
                    }
                    else
                    {
                        radioButtonCreditor.setChecked(true);
                    }
            } while (creditor.moveToNext());
        }


        buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = textInputAmount.getText().toString();
                double dAmount = 0;

                if(textInputName.getText().toString().trim().length() == 0 && textInputSurname.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(EditingCreditorActivity.this, "Proszę wprowadzić imię lub nazwisko", Toast.LENGTH_LONG).show();
                }
                else if(textInputAmount.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(EditingCreditorActivity.this, "Proszę wprowadzić kwotę", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        dAmount = Double.parseDouble(amount);
                    }
                    catch (NumberFormatException e)
                    {
                        return;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    String currCallenda = sdf.format(calendar.getTime());

                    Date pickedDate = null;
                    Date currentDate = null;
                    try {
                        pickedDate = sdf.parse(textInputDate.getText().toString());
                        currentDate = sdf.parse(currCallenda);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if(dAmount <= 0)
                    {
                        Toast.makeText(EditingCreditorActivity.this, "Proszę wprowadzić poprawą kwotę", Toast.LENGTH_LONG).show();
                    }
                    if(textInputPhone.getText().toString().trim().length() != 9)
                    {
                        Toast.makeText(EditingCreditorActivity.this, "Proszę wprowadzić numer telefonu", Toast.LENGTH_LONG).show();
                    }
                    else if(textInputDate.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(EditingCreditorActivity.this, "Proszę wybrać datę z kalendarza", Toast.LENGTH_LONG).show();
                    }
                    else if(pickedDate.before(currentDate))
                    {
                        Toast.makeText(EditingCreditorActivity.this, "Proszę wybrać poprawną datę", Toast.LENGTH_LONG).show();
                    }
                    else if(!radioButtonCreditor.isChecked() && !radioButtonDebtor.isChecked())
                    {
                        Toast.makeText(EditingCreditorActivity.this, "Proszę zaznaczyć status osoby", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String imie = textInputName.getText().toString();
                        String nazwisko = textInputSurname.getText().toString();
                        int telefon = Integer.parseInt(textInputPhone.getText().toString());
                        double dlug = Double.parseDouble(textInputAmount.getText().toString());
                        int cyklicznosc;
                        boolean czyDluznik = true;

                        // TextView textView = (TextView)spinner.getSelectedView();
                        //String result = textView.getText().toString();
                        //int test = spinner.getSelectedItemPosition();

                        cyklicznosc = spinner.getSelectedItemPosition();

                        if(radioButtonDebtor.isChecked())
                        {
                            czyDluznik = true;
                        }
                        else if(radioButtonCreditor.isChecked())
                        {
                            czyDluznik = false;
                        }

                        DatabaseManager dbManager = new DatabaseManager(EditingCreditorActivity.this);
                        dbManager.UpdateOneCreditor(id, imie, nazwisko, telefon, dlug, calendarDate,  cyklicznosc, czyDluznik);
                        finish();
                    }
                }
            }
        });

        radioButtonDebtor.setOnClickListener(new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonCreditor.isChecked())
                {
                    radioButtonCreditor.setChecked(false);
                }
            }
        });

        radioButtonCreditor.setOnClickListener(new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonDebtor.isChecked())
                {
                    radioButtonDebtor.setChecked(false);
                }
            }
        });

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String tmpMonth;
                String tmpDay;

                if (month < 10) {
                    tmpMonth = "0" + String.valueOf(month+1);
                } else {
                    tmpMonth = String.valueOf(month+1);
                }

                if (dayOfMonth < 10) {
                    tmpDay = "0" + String.valueOf(dayOfMonth);
                } else {
                    tmpDay = String.valueOf(dayOfMonth);
                }

                calendarDate.set(year, Integer.parseInt(tmpMonth), Integer.parseInt(tmpDay));
                textInputDate.setText(year + "-" + tmpMonth + "-" + tmpDay);
            }


        });
    }

}
