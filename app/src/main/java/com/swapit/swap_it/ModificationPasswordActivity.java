package com.swapit.swap_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Activité permettant la modification du mot de passe
public class ModificationPasswordActivity extends AppCompatActivity {

    private EditText mConfirmerPassword;
    private EditText mNouveauPassword;
    private EditText mAncienPassword;
    private Button mValide;
    private String test_password = "bonjour";
    private static String LOG_TAG = "ModificationPasswordActivity";
    public static final String IDENTITE_USER = "IdentiteUser";
    String retour_bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_password);

        mAncienPassword = findViewById(R.id.editText_saisie_ancien);
        mConfirmerPassword = findViewById(R.id.editText_saisie_confirmation);
        mNouveauPassword = findViewById(R.id.editText_saisie_nouveau);
        mValide = findViewById(R.id.button_validation);

        mValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validiteConfirmer()){
                    lancementSetting();
                }
            }
        });

    }

    public void lancementSetting(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //TODO comparer la saisie du mot de passe actuel avec celui enregistrer dans le BDD
    public boolean validiteAncien(){
        String url = urlPHP("ancien");
        new MakeNetworkCall().execute(url, "GET");
        mAncienPassword.setError(null);
        if (retour_bdd.equals("false")){
            mAncienPassword.setError("Mot de passe incorrect");
            return false;
        }
        else if (retour_bdd.equals("true")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean validiteConfirmer(){
        String confirmer = mConfirmerPassword.getText().toString();
        String nouveau = mNouveauPassword.getText().toString();
        mConfirmerPassword.setError(null);
        boolean ok = true;
        if ((confirmer.contentEquals(nouveau)) && (validiteNouveau(nouveau))){
            ok = true;
        }
        else{
            mConfirmerPassword.setError("erreur dans la confirmation du mot de passe");
            ok = false;
        }
        return ok;
    }

    public boolean validiteNouveau(String mdp){
        //TODO definir le niveau de securisation du mot de passe
        //String nouveau = mNouveauPassword.getText().toString();
        mNouveauPassword.setError(null);
        //le mot de passe doit contenir au moins 8 caracateres
       if (mdp.length() < 8){
            mNouveauPassword.setError("Mot de passe trop court (<8)");
            return false;
       }
       else{
           return true;
       }
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

    public String urlPHP(String script){
        //test_ancien_mdp
        //update_mdp
        String url;
        String param = argumentPHP();
        if (script.equals("ancien")){
            param = param + mAncienPassword.getText().toString();
        }
        else if (script.equals("update")){
            param = param + mNouveauPassword.getText().toString();
        }
        else{
            param = null;
        }
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
            return res;
        }

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }
    }

    void remplir_bdd(String res){
        retour_bdd = res;
    }
}
