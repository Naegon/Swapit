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
        argumentPHP2(requetteHttpSuppression);

        requetteHttpSuppression.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd) {
                //TODO à completer
            }
            @Override
            public void onFail(String retourBdd) {
                //TODO à completer
            }
        });
    }

    public void argumentPHP2(CallBdd requetteHttpSuppression){
        String nom = retrieveDataUser("nom");
        String prenom = retrieveDataUser("prenom");
        String mail = retrieveDataUser("mail");

        requetteHttpSuppression.ajoutArgumentPhpList("prenom" , prenom);
        requetteHttpSuppression.ajoutArgumentPhpList("nom", nom);
        requetteHttpSuppression.ajoutArgumentPhpList("adresse_mail", mail);
    }

    public String argumentPHP(){
        String nom = retrieveDataUser("nom");
        String prenom = retrieveDataUser("prenom");
        String mail = retrieveDataUser("mail");

        String param = "prenom=" + prenom + "&"
                + "nom=" + nom + "&"
                + "adresse_mail=" + mail;
        return param;
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
                String url = urlPHP();

                new MakeNetworkCall().execute(url, "GET");
                Log.i(LOG_TAG,"ok");
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

    public String urlPHP(){
        String url;
        String param = argumentPHP();
        //envoie bdd
        url = "http://91.121.116.121/swapit/delete_utilisateur.php?" + param;
        Log.d(LOG_TAG, "Error : " + url);
        return url;
    }

    InputStream ByGetMethod(String ServerURL) {

        InputStream DataInputStream = null;
        try {

            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection) url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to GET
            cc.setRequestMethod("GET");
            //set it to true as we are connecting for input
            cc.setDoInput(true);

            //reading HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData" + e.getMessage());

        }
        return DataInputStream;

    }

    InputStream ByPostMethod(String ServerURL) {

        InputStream DataInputStream = null;
        try {

            //Post parameters
            String PostParam = "first_name=android&amp;last_name=pala";

            //Preparing
            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to POST
            cc.setRequestMethod("POST");
            //set it to true as we are connecting for input
            cc.setDoInput(true);
            //opens the communication link
            cc.connect();

            //Writing data (bytes) to the data output stream
            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            //flushes data output stream.
            dos.flush();
            dos.close();

            //Getting HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            //HttpURLConnection.HTTP_OK is equal to 200
            if(response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    String ConvertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
            }
        }
        return response.toString();
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            toast("Chargement");
        }

        @Override
        protected String doInBackground(String... arg) {

            InputStream is = null;
            String URL = arg[0];
            Log.d(LOG_TAG, "URL: " + URL);
            String res = "";


            if (arg[1].equals("Post")) {

                is = ByPostMethod(URL);

            } else {

                is = ByGetMethod(URL);
            }
            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            //res = json(res);
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, result);
            Log.d(LOG_TAG, "retour " + result);
            //TODO changer le scipt pour renvoyer vrai si les annonces et le compte sont supprimé et faux dans les autres cas
            if (result.equals("true")){
                toast("Suppression réussie");
                clearSharedPreference();
                lancerLogin();
            }
            else{
                toast("Echec de la suppression du compte");
            }

        }
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
