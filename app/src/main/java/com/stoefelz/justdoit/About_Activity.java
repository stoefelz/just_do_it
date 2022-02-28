package com.stoefelz.justdoit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //html link interpreter
        ((TextView) findViewById(R.id.link_about)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.link_about)).setText(Html.fromHtml(getResources().getString(R.string.link_about)));

    }
}