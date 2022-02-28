package com.stoefelz.justdoit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main_Activity extends AppCompatActivity {
    DB_Connection db_connection;
    ListView list_view;
    Cursor_Adapter adapter;
    Cursor db_data_cursor;
    TextView empty_list;
    View colored_line;
    Button new_button;
    int db_version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new_button = findViewById(R.id.new_button);
        colored_line = findViewById(R.id.colored_line_main);
        empty_list = findViewById(R.id.empty);
        list_view = findViewById(R.id.list_view);

        db_connection = new DB_Connection(getApplicationContext(), "tasks", null, db_version);
        db_data_cursor = db_connection.get_tasks();
        adapter = new Cursor_Adapter(this, R.layout.list_item, db_data_cursor);

        if(!adapter.isEmpty()) {
            empty_list.setVisibility(View.GONE);
            colored_line.setVisibility(View.GONE);
        }

        list_view.setAdapter(adapter);
        //to get context menu on listview
        registerForContextMenu(list_view);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_Task.class);
                intent.putExtra("id_code", "new");
                startActivityForResult(intent, 3);
            }
        });

        empty_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_Task.class);
                intent.putExtra("id_code", "new");
                startActivityForResult(intent, 3);
            }
        });

    }

    //answer after editing/creating task
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3) {
            if(resultCode == 4) {
                refresh_list();
            }
        }
        else if(requestCode == 5) {
            if(resultCode == 4) {
                refresh_list();
            }
        }
    }

    //get main menu content
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //get context menu content
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    //main menu selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu:
                Intent intent = new Intent(getApplicationContext(), About_Activity.class);
                startActivity(intent);
                return true;
            case R.id.delete_all_menu:
                db_connection.delete_all();
                refresh_list();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //context menu selection
    @SuppressLint("Range")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //get db row number for getting right dataset
        int info_int = (int) info.id;

        switch (item.getItemId()) {
            case R.id.edit_context:
                Intent intent = new Intent(getApplicationContext(), Add_Task.class);
                //read database for data for intent
                Cursor task_name = db_connection.get_task_values(info_int);
                //necessary because otherwise crash
                task_name.moveToFirst();
                intent.putExtra("id_code", "edit");
                intent.putExtra("task_id", task_name.getInt(task_name.getColumnIndex("_id")));
                intent.putExtra("task_name", task_name.getString(task_name.getColumnIndex("task")));
                intent.putExtra("task_color", task_name.getInt(task_name.getColumnIndex("color")));
                //send date in one string
                DateFormat date_format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(task_name.getLong(task_name.getColumnIndex("deadline")));
                intent.putExtra("task_date", date_format.format(calendar.getTime()));

                startActivityForResult(intent, 5);
                return true;

            case R.id.del_context:
                db_connection.delete_task(info_int);
                refresh_list();
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    //update listview
    public void refresh_list() {
        db_data_cursor = db_connection.get_tasks();
        adapter = new Cursor_Adapter(getApplicationContext(), R.layout.list_item, db_data_cursor);
        list_view.setAdapter(adapter);
        if(!adapter.isEmpty()) {
            empty_list.setVisibility(View.GONE);
            colored_line.setVisibility(View.GONE);
        }
        else {
            empty_list.setVisibility(View.VISIBLE);
            colored_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_connection.close();
    }
}