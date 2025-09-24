package com.stoefelz.justdoit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Add_Task extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Button submit_btn;
    Button color_btn;
    TextView selected_date;
    TextView selected_time;
    EditText task_name;
    TextView heading_add;
    int task_color;
    //because same activity for editing and for creating task
    Boolean is_on_edit = false;
    //database row index of edited task, send over intend extra
    int when_edit_row_id = -1;
    DB_Connection db_connection;
    int db_connection_version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //edge to edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //set statusbar color depending light/night mode
        boolean isNightMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(!isNightMode);

        submit_btn = findViewById(R.id.submit_add);
        color_btn = findViewById(R.id.color_add);
        task_name = findViewById(R.id.task_add);
        heading_add = findViewById(R.id.heading_add);
        selected_date = findViewById(R.id.selected_date_add);
        selected_time = findViewById(R.id.selected_time_add);
        db_connection = new DB_Connection(getApplicationContext(), "tasks", null, db_connection_version);

        task_name.requestFocus();
        task_name.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(task_name, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 200);

        //default values for date and time, atm date and time 1 week later
        DateFormat date_format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar actual_date = Calendar.getInstance();
        //add 7 days to actual date
        actual_date.add(Calendar.DAY_OF_MONTH, 7);
        selected_date.setText(date_format.format(actual_date.getTime()));
        date_format = new SimpleDateFormat("HH:mm");
        selected_time.setText(date_format.format(Calendar.getInstance().getTime()));

        task_color  = getColor(R.color.col1);

        //check if edit intent was called -> set correct data from db
        Intent get_intent = getIntent();
        String id_code = Objects.requireNonNull(get_intent.getExtras()).getString("id_code");
        assert id_code != null;
        if(id_code.equals("edit")) {
            is_on_edit = true;
            heading_add.setText(R.string.heading_change);
            submit_btn.setText(R.string.submit_change);
            task_name.setText(get_intent.getExtras().getString("task_name"));
            task_color = get_intent.getExtras().getInt("task_color");
            color_btn.setBackgroundColor(get_intent.getExtras().getInt("task_color"));
            when_edit_row_id = get_intent.getExtras().getInt("task_id");
            String get_date = get_intent.getExtras().getString("task_date");
            //say nothing, it is working
            assert get_date != null;
            if(get_date.length() == 16) {
                selected_date.setText(get_date.substring(0, 10));
                selected_time.setText(get_date.substring(11, 16));
            }
        }

        //set listener for views

        submit_btn.setOnClickListener(v -> {
            String task_string = task_name.getText().toString();
            if(task_string.length() < 2 || task_string.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.error_task_length_short, Toast.LENGTH_LONG).show();
            }
            else if(task_string.trim().length() > 60) {
                Toast.makeText(getApplicationContext(), R.string.error_task_length_long, Toast.LENGTH_LONG).show();
            }
            else {
                long date_for_inserting = 0;

                try {
                    //conversion from view strings to milliseconds
                    date_for_inserting = read_datetime();
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                if(is_on_edit) {
                    db_connection.update_task(when_edit_row_id, task_name.getText().toString(), task_color, date_for_inserting);
                }
                else {
                    db_connection.insert_task(task_name.getText().toString(), task_color, date_for_inserting);
                }

                setResult(4);
                finish();
            }
        });

        color_btn.setOnClickListener(v -> {
            Intent get_color = new Intent(getApplicationContext(), Color_Picker.class);
            startActivityForResult(get_color, 1);
        });

        selected_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker date_picker = new Date_Picker();
                date_picker.show(getSupportFragmentManager(), "date_picker_dialog");
            }
        });

        selected_time.setOnClickListener(v -> {
            Time_Picker time_picker = new Time_Picker();
            time_picker.show(getSupportFragmentManager(), "time_picker_dialog");
        });
    }

    //get button color answer
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == 2) {
                assert data != null;
                task_color = data.getIntExtra("color", R.color.col1);
                color_btn.setBackgroundColor(task_color);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        DateFormat date_format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String selected_date_string = date_format.format(calendar.getTime());
        selected_date.setText(selected_date_string);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        DateFormat date_format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        String selected_time_string = date_format.format(calendar.getTime());
        selected_time.setText(selected_time_string);
    }

    //reads the view date time from views and convert it into long milliseconds
    public long read_datetime() throws ParseException {
        String date = selected_date.getText().toString();
        String time = selected_time.getText().toString();
        String composed_date = date + " " + time;
        DateFormat date_format = new SimpleDateFormat(("dd.MM.yyyy HH:mm"));
        Date parsed_date = date_format.parse(composed_date);
        return  new Long(parsed_date.getTime());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_connection.close();
    }
}
