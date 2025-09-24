package com.stoefelz.justdoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        //set statusbar color depending light/night mode
        boolean isNightMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(!isNightMode);

        //html link interpreter
        ((TextView) findViewById(R.id.link_about)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.link_about)).setText(Html.fromHtml(getResources().getString(R.string.link_about)));

    }
}