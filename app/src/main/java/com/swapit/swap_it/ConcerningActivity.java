package com.swapit.swap_it;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.swapit.swap_it.R;

public class ConcerningActivity extends AppCompatActivity {

    private static String LOG_TAG = "ConcerningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concerning);
    }
}
