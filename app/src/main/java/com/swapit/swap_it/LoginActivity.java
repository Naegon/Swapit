package com.swapit.swap_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// Page pour se connecter à son compte
public class LoginActivity extends AppCompatActivity {

    Button button_creer_compte, button_connexion;
    EditText editText_email, editText_mdp;
    String url;
    TextView test;
    CheckBox checkBox_rester_co;
    private static String LOG_TAG = "LogActvity";
    public static final String IDENTITE_USER = "IdentiteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_connexion = findViewById(R.id.button_connexion);
        button_creer_compte = findViewById(R.id.button_creer_compte);
        editText_email = findViewById(R.id.edittext_login_email);
        editText_mdp = findViewById(R.id.edittext_login_mdp);
        checkBox_rester_co = findViewById(R.id.checkBox_login_connexion);
        final SharedPreferences pref_user = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE);
        SharedPreferences.Editor pref_user_editor = getSharedPreferences(IDENTITE_USER,MODE_PRIVATE).edit();

        if (pref_user.getString("keep", "null").equals("true")){
            editText_mdp.setText(pref_user.getString("mdp", "null"));
            editText_email.setText(pref_user.getString("mail", "null"));
            button_connexion.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lancerPage(0);
                }
            });
        }

        button_creer_compte.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerPage(1);
            }
        });

        button_connexion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validiteSaisie()){
                    url = urlPHP();
                    new MakeNetworkCall().execute(url, "GET");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //TODO ca plante
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        super.onBackPressed();
    }

    public void lancerPage(int num){
        //0 pour main 1 pour compte
        Intent intent_main = new Intent(this, MainActivity.class);
        Intent intent_profil = new Intent(this, CreationCompteActivity.class);
        if (num == 0){
            startActivity(intent_main);
        }
        else if (num == 1){
            startActivity(intent_profil);
        }
    }

    public boolean validiteEmail(){
        boolean ok = true;
        editText_email.setError(null);
        String email = editText_email.getText().toString();
        if (email.isEmpty()){
            ok = false;
            editText_email.setError("Champs vide");
        }
        return ok;
    }

    public boolean validiteMdp(){
        boolean ok = true;
        editText_mdp.setError(null);
        String mdp = editText_mdp.getText().toString();
        if (mdp.isEmpty()){
            ok = false;
            editText_mdp.setError("Champs vide");
        }
        return ok;
    }



    public boolean validiteSaisie(){
        boolean ok = true;
        if (!validiteEmail()){
            ok = false;
        }
        if (!validiteMdp()){
            ok = false;
        }
        return ok;
    }

    public String argumentPHP(){
        String mdp = editText_mdp.getText().toString();
        String mail = editText_email.getText().toString();
        String param = "adresse_mail=" + mail + "&"
                + "mdp=" + mdp;
        return param;
    }

    public String urlPHP(){
        String url;
        String param = argumentPHP();
        //envoie bdd
        url = "http://91.121.116.121/swapit/login.php?" + param;
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
        //json(response.toString());
        //getJsonRetour(response.toString());
        return response.toString();
    }

    public void DisplayMessage(String a) {
        //TODO ICI affichage test

    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO afficher le please wait dans un popup
            DisplayMessage("Please Wait ...");
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
            if (result.equals("false")){
                badMdp();
            }
            else{
                json(result);

                lancerPage(0);
            }
            Log.d(LOG_TAG, "Result: " + result);
        }
    }


    public void json(String a){
        try {
            JSONObject json = new JSONObject(a);
            String nom = json.getString("nom");
            String prenom = json.getString("prenom");
            String mail = editText_email.getText().toString();
            String mdp = editText_mdp.getText().toString();

            //TODO garder mdp avec les shared preference n'est pas securiser du tout !!!
            SharedPreferences.Editor editor = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE ).edit();
            editor.putString("nom", nom);
            editor.putString("prenom", prenom);
            editor.putString("mail", mail);
            if (checkBox_rester_co.isChecked()){
                editor.putString("mdp", mdp);
                editor.putString("keep", "true");
            }
            else{
                editor.putString("keep", "false");
            }
            editor.apply();

        } catch (JSONException e) {
            Log.v(LOG_TAG, "Erreur convertir json");
        }
    }

    public void badMdp(){
        editText_mdp.setError("Email/Mot de passe erroné");
        editText_email.setError("Email/Mot de passe erroné");
        editText_email.setText(null);
        editText_mdp.setText(null);
    }
}

