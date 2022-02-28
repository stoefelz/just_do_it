package com.stoefelz.justdoit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Cursor_Adapter extends ResourceCursorAdapter {

    public Cursor_Adapter(Context context, int layout, Cursor c) {
        super(context, layout, c);
    }

    @SuppressLint({"Range"})
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task_name = (TextView) view.findViewById(R.id.task_name);
        TextView task_deadline = (TextView) view.findViewById(R.id.task_deadline);
        View colored_line = (View) view.findViewById(R.id.colored_line);

        //set elements in listview
        if(cursor.getColumnIndex("task") > -1 && cursor.getColumnIndex("deadline") > -1 && cursor.getColumnIndex("color") > -1) {
            task_name.setText(cursor.getString(cursor.getColumnIndex("task")));
            //get Date in milliseconds and format it to string
            SimpleDateFormat sdFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Calendar calendar = Calendar.getInstance();
            long deadline_milliseconds = cursor.getLong(cursor.getColumnIndex("deadline"));
            calendar.setTimeInMillis(deadline_milliseconds);
            task_deadline.setText(sdFormat.format(calendar.getTime()));

            //check if deadline was missed
            if(deadline_milliseconds < System.currentTimeMillis()) {
                task_deadline.setTextColor(ResourcesCompat.getColor(task_deadline.getResources(),R.color.col3, null));
            }
            else {
                task_deadline.setTextColor(ResourcesCompat.getColor(task_deadline.getResources(),R.color.black, null));
            }
            colored_line.setBackgroundColor(cursor.getInt(cursor.getColumnIndex("color")));
            view.setLongClickable(true);
        }
        else {
            task_name.setText("Database Error goes brrr");
        }
    }
}
