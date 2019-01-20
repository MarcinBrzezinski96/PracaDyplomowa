package com.example.marcin.pracadyplomowa;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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

public abstract class AddEditCreditor extends AppCompatActivity {


    CalendarView datePicker;
    TextInputEditText textInputDate;
    TextInputEditText textInputName;
    TextInputEditText textInputSurname;
    TextInputEditText textInputAmount;
    TextInputEditText textInputPhone;
    TextInputEditText textInputNumberOfPayment;
    RadioButton radioButtonDebtor;
    RadioButton radioButtonCreditor;
    TextInputEditText textImputNumberOfPayment;
    Button buttonSave;
    Spinner spinner;

    Calendar calendarDate;
//TODO edytowanie nie działa w przypadku zmiany z wierzyciela na dluznika, w odwrotnym przypadku dziala

    String imie;
    String nazwisko;
    int telefon;
    int iloscPlatnosci;
    double dlug;
    int cyklicznosc;
    boolean czyDluznik = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_creditor);

        datePicker = findViewById(R.id.calendarView);
        textInputDate = findViewById(R.id.textInputDate);
        textInputName = findViewById(R.id.textInputName);
        textInputSurname = findViewById(R.id.textInputSurname);
        textInputAmount = findViewById(R.id.textInputAmount);
        textInputPhone = findViewById(R.id.textInputPhone);
        textInputNumberOfPayment = findViewById(R.id.textInputNumberOfPayment);
        radioButtonDebtor = findViewById(R.id.radioButtonDebtor);
        radioButtonCreditor = findViewById(R.id.radioButtonCreditor);
        textImputNumberOfPayment = findViewById(R.id.textInputNumberOfPayment);
        buttonSave = findViewById(R.id.buttonSave);
        spinner = (Spinner) findViewById(R.id.spinner);
        calendarDate = Calendar.getInstance();
        textInputDate.setEnabled(false);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddEditCreditor.this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cyklicznosc = spinner.getSelectedItemPosition();
                if(cyklicznosc == 0)
                {
                    textImputNumberOfPayment.setText("1");
                    textImputNumberOfPayment.setEnabled(false);
                }
                else
                {
                    textImputNumberOfPayment.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cyklicznosc = spinner.getSelectedItemPosition();
                if(cyklicznosc == 0)
                {
                    textImputNumberOfPayment.setText("1");
                    textImputNumberOfPayment.setEnabled(false);
                }
                else
                {
                    textImputNumberOfPayment.setEnabled(true);
                }
            }
        });


        buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSaveClickValueCheck();

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

                calendarDate.set(year, Integer.parseInt(tmpMonth)-1, Integer.parseInt(tmpDay));
                textInputDate.setText(year + "-" + tmpMonth + "-" + tmpDay);
            }
        });

    }

    public void onSaveClickValueCheck()
    {

        String amount = textInputAmount.getText().toString();
        double dAmount = 0;

        if(textInputName.getText().toString().trim().length() == 0 && textInputSurname.getText().toString().trim().length() == 0)
        {
            Toast.makeText(AddEditCreditor.this, "Proszę wprowadzić imię lub nazwisko", Toast.LENGTH_LONG).show();
        }
        else if(textInputAmount.getText().toString().trim().length() == 0)
        {
            Toast.makeText(AddEditCreditor.this, "Proszę wprowadzić kwotę", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddEditCreditor.this, "Proszę wprowadzić poprawą kwotę", Toast.LENGTH_LONG).show();
            }
            if(textInputPhone.getText().toString().trim().length() != 9)
            {
                Toast.makeText(AddEditCreditor.this, "Proszę wprowadzić numer telefonu", Toast.LENGTH_LONG).show();
            }
            else if(textInputDate.getText().toString().trim().length() == 0)
            {
                Toast.makeText(AddEditCreditor.this, "Proszę wybrać datę z kalendarza", Toast.LENGTH_LONG).show();
            }
            else if(textImputNumberOfPayment.getText().toString().trim().length() == 0)
            {
                Toast.makeText(AddEditCreditor.this, "Proszę prowadzić liczbę płatności", Toast.LENGTH_LONG).show();
            }
            else if(pickedDate.before(currentDate))
            {
                Toast.makeText(AddEditCreditor.this, "Proszę wybrać poprawną datę", Toast.LENGTH_LONG).show();
            }
            else if(!radioButtonCreditor.isChecked() && !radioButtonDebtor.isChecked())
            {
                Toast.makeText(AddEditCreditor.this, "Proszę zaznaczyć status osoby", Toast.LENGTH_LONG).show();
            }
            else
            {
                imie = textInputName.getText().toString();
                nazwisko = textInputSurname.getText().toString();
                telefon = Integer.parseInt(textInputPhone.getText().toString());
                dlug = Double.parseDouble(textInputAmount.getText().toString());
                iloscPlatnosci = Integer.parseInt(textImputNumberOfPayment.getText().toString());
                czyDluznik = true;

                cyklicznosc = spinner.getSelectedItemPosition();

                if(radioButtonDebtor.isChecked())
                {
                    czyDluznik = true;
                }
                else if(radioButtonCreditor.isChecked())
                {
                    czyDluznik = false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditCreditor.this);
                builder.setTitle("Czy chcesz zapisać klienta?");

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveToDatabase();
                    }
                });
                builder.setNegativeButton("Nie", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
    }

    abstract void saveToDatabase();


}
