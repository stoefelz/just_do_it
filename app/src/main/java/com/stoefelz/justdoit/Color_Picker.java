package com.stoefelz.justdoit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Color_Picker extends AppCompatActivity {
    Button color_btn_1;
    Button color_btn_2;
    Button color_btn_3;
    Button color_btn_4;
    Button color_btn_5;
    Button color_btn_6;
    Button color_btn_7;
    Button color_btn_8;
    Button color_btn_9;
    Button color_btn_10;
    Button color_btn_11;
    Button color_btn_12;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        //sets only click listener on every button with intent answer
        color_btn_1 = findViewById(R.id.color_button_1);
        color_btn_2 = findViewById(R.id.color_button_2);
        color_btn_3 = findViewById(R.id.color_button_3);
        color_btn_4 = findViewById(R.id.color_button_4);
        color_btn_5 = findViewById(R.id.color_button_5);
        color_btn_6 = findViewById(R.id.color_button_6);
        color_btn_7 = findViewById(R.id.color_button_7);
        color_btn_8 = findViewById(R.id.color_button_8);
        color_btn_9 = findViewById(R.id.color_button_9);
        color_btn_10 = findViewById(R.id.color_button_10);
        color_btn_11 = findViewById(R.id.color_button_11);
        color_btn_12 = findViewById(R.id.color_button_12);
        tv = findViewById(R.id.heading_color_picker);

        set_click_listeners(color_btn_1);
        set_click_listeners(color_btn_2);
        set_click_listeners(color_btn_3);
        set_click_listeners(color_btn_4);
        set_click_listeners(color_btn_5);
        set_click_listeners(color_btn_6);
        set_click_listeners(color_btn_7);
        set_click_listeners(color_btn_8);
        set_click_listeners(color_btn_9);
        set_click_listeners(color_btn_10);
        set_click_listeners(color_btn_11);
        set_click_listeners(color_btn_12);

    }

    public void set_click_listeners(Button btn) {
        btn.setOnClickListener(v -> {
            int color = btn.getCurrentTextColor();
            Intent color_answer = new Intent();
            color_answer.putExtra("color", color);
            setResult(2, color_answer);
            finish();
        });
    }

}