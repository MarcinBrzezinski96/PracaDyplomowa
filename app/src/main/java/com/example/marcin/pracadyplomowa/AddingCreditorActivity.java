package com.example.marcin.pracadyplomowa;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddingCreditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = textInputAmount.getText().toString();
                double dAmount = 0;

                if(textInputName.getText().toString().trim().length() == 0 && textInputSurname.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(AddingCreditorActivity.this, "Proszę wprowadzić imię lub nazwisko", Toast.LENGTH_LONG).show();
                }
                else if(textInputAmount.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(AddingCreditorActivity.this, "Proszę wprowadzić kwotę", Toast.LENGTH_LONG).show();
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


//TODO zrob sprawdzanie daty

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
                        Toast.makeText(AddingCreditorActivity.this, "Proszę wprowadzić poprawą kwotę", Toast.LENGTH_LONG).show();
                    }
                    if(textInputPhone.getText().toString().trim().length() != 9)
                    {
                        Toast.makeText(AddingCreditorActivity.this, "Proszę wprowadzić numer telefonu", Toast.LENGTH_LONG).show();
                    }
                    else if(textInputDate.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(AddingCreditorActivity.this, "Proszę wybrać datę z kalendarza", Toast.LENGTH_LONG).show();
                    }
                    else if(pickedDate.before(currentDate))
                    {
                        Toast.makeText(AddingCreditorActivity.this, "Proszę wybrać poprawną datę", Toast.LENGTH_LONG).show();
                    }
                    else if(!radioButtonCreditor.isChecked() && !radioButtonDebtor.isChecked())
                    {
                        Toast.makeText(AddingCreditorActivity.this, "Proszę zaznaczyć status osoby", Toast.LENGTH_LONG).show();
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

                        DatabaseManager dbManager = new DatabaseManager(AddingCreditorActivity.this);
                        dbManager.AddCreditor(imie, nazwisko, telefon, dlug, calendarDate,  cyklicznosc, czyDluznik);
                        //Intent intent = new Intent(AddingCreditorActivity.this, CreditorsActivity.class);
                        //startActivity(intent);
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
                    tmpMonth = "0" + String.valueOf(month);
                } else {
                    tmpMonth = String.valueOf(month);
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

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        /*
        public void onItemSelected(AdapterView<?> parent, View view,
        int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }

*/

    }

}
