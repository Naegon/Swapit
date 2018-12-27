package com.swapit.swap_it;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import com.swapit.swap_it.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Page d'affichage du profile utilisateur
public class ProfileActivity extends AppCompatActivity {

    TextView textView_nom, textView_prenom, textView_swap, textView_mail, textView_tel, textView_description;
    String url, json_retour;
    private static String LOG_TAG = "MainActivity";
    public static final String IDENTITE_USER = "IdentiteUser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView_description = findViewById(R.id.textView_profil_description_user);
        textView_mail = findViewById(R.id.textView_profil_mail);
        textView_nom = findViewById(R.id.textView_profil_nom);
        textView_prenom = findViewById(R.id.textView_profil_prenom);
        textView_swap = findViewById(R.id.textView_profil_swap);
        textView_tel = findViewById(R.id.textView_profil_telephone);
        json_retour = null;

        viderTextview();
        url = urlPHP();
        new MakeNetworkCall().execute(url, "GET");
    }

    public void viderTextview(){
        textView_tel.setText(null);
        textView_prenom.setText(null);
        textView_swap.setText(null);
        textView_nom.setText(null);
        textView_description.setText(null);
        textView_mail.setText(null);
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

    //recupere les data_user
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
        url = "http://91.121.116.121/swapit/renvoyer_info_compte.php?" + param;
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
            afficherErreurBddTimeout();
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

       //textView_description.setText(a);
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
            json(result);
            //DisplayMessage(result);
            Log.d(LOG_TAG, "Result: " + result);
        }
    }


    //recupere et stock l'obet Json renvoyer depuis PHP
    public void json(String a){
        try {
            JSONObject json = new JSONObject(a);
            String nom = json.getString("nom");
            String prenom = json.getString("prenom");
            String nbswap = json.getString("nbpoints");
            String telephone = json.getString("numerotel");
            String mail = json.getString("adresse_mail");
            String description = json.getString("biographie");

            textView_tel.setText(telephone);
            textView_prenom.setText(prenom);
            textView_swap.setText(nbswap);
            textView_nom.setText(nom);
            textView_description.setText(description);
            textView_mail.setText(mail);

        } catch (JSONException e) {
            Log.v(LOG_TAG, "Erreur convertir json");
        }
    }

    public void afficherErreurBddTimeout(){
        Toast toast = Toast.makeText(getApplicationContext(), "Erreur dans le chargement du profil", Toast.LENGTH_LONG);
        toast.show();
    }
}
