package com.swapit.swap_it;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.swapit.swap_it.R;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Sous-page des paramètres concernant les préférences de notification
public class NotificationActivity extends AppCompatActivity {

    public Switch mNotificationAll;
    public Switch mNotificationService;
    public Switch mNotificationSoutien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mNotificationAll = findViewById(R.id.switch_notification_all);
        mNotificationService = findViewById(R.id.switch_service);
        mNotificationSoutien = findViewById(R.id.switch_soutien);


        mNotificationAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                changeStateSwitches();
            }
        });
/*
        mNotificationService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeStateAllSwitch();
            }
        });

        mNotificationSoutien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeStateAllSwitch();
            }
        });
*/
    }

    //changement des etats de tous les boutons en fonction de switch_all
    private void changeStateSwitches(){
        if (mNotificationAll.isChecked()){
            mNotificationService.setChecked(true);
            mNotificationSoutien.setChecked(true);
        }
        else{
            mNotificationSoutien.setChecked(false);
            mNotificationService.setChecked(false);
        }
    }
/*
    private void changeStateAllSwitch(){
        if (mNotificationService.isChecked() && !mNotificationSoutien.isChecked() && mNotificationAll.isChecked()){
            mNotificationAll.setChecked(false);
        }
        else if (!mNotificationService.isChecked() && mNotificationSoutien.isChecked() && mNotificationAll.isChecked()){
            mNotificationAll.setChecked(false);
        }
        else if (!mNotificationService.isChecked() && !mNotificationSoutien.isChecked()){
            mNotificationAll.setChecked(false);
        }
        else{
            mNotificationAll.setChecked(true);
        }
    }
    */
}
