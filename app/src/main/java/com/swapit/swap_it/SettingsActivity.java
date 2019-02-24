package com.swapit.swap_it;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Page des paramètres
public class SettingsActivity extends AppCompatActivity {
    private static String LOG_TAG = "SettingActivity";
    public static final String IDENTITE_USER = "IdentiteUser";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /**
     * Lancement des pages notifications, modification du mdp, suppression du compte
     */
    public void notification(View view){
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
    public void modificationPassword(View view){
        Intent intentPassword = new Intent(this, ModificationPasswordActivity.class);
        startActivity(intentPassword);
    }

    public void supprimerCompte(View view){
        //TODO completer pour call php
        afficherDialog();
    }

    public void requeteHttp(){
        CallBdd requetteHttpSuppression = new CallBdd("http://91.121.116.121/swapit/delete_utilisateur.php?");
        argumentPHP(requetteHttpSuppression);

        requetteHttpSuppression.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd) {
                Log.d(LOG_TAG, "OnSuccess : " + retourBdd);
                if (retourBdd.equals("true")){
                    clearSharedPreference();
                    lancerLogin();
                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "OnFail : " + retourBdd);
                toast("Erreur");
            }
        });
    }

    public void argumentPHP(CallBdd requetteHttpSuppression){
        String nom = retrieveDataUser("nom");
        String prenom = retrieveDataUser("prenom");
        String mail = retrieveDataUser("mail");

        requetteHttpSuppression.ajoutArgumentPhpList("prenom" , prenom);
        requetteHttpSuppression.ajoutArgumentPhpList("nom", nom);
        requetteHttpSuppression.ajoutArgumentPhpList("adresse_mail", mail);
    }

    /**
     * Affiche pop up de suppression du compte
     */
    public void afficherDialog(){
        context = getApplicationContext();

        /*Dialog myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dialog_confirmer_suppression_compte);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.suppression_compte_texte);
        builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requeteHttp();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Enleve la boite de dialogue automatiquement
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String retrieveDataUser(String id){
        SharedPreferences prefs_id = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE);
        String data = null;
        if (id.equals("nom")){
            data = prefs_id.getString("nom", "null");
        }
        else if (id.equals("prenom")){
            data = prefs_id.getString("prenom", "null");
        }
        else if (id.equals("mail")){
            data = prefs_id.getString("mail", "null");
        }
        return data;
    }

    public void toast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG);
        toast.show();
    }

    public void lancerLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void clearSharedPreference(){
        SharedPreferences.Editor editor = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
