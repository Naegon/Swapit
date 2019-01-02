package com.swapit.swap_it.ServiceJava;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swapit.swap_it.MainActivity;
import com.swapit.swap_it.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class CreationAnnonceServiceActivity extends AppCompatActivity {

    EditText nb_swap, description;
    Button valider;
    TextView textview_date;
    int mYear, mMonth, mDay;
    Spinner spinner_categorie, spinner_sous_categorie;
    public static final String IDENTITE_USER = "IdentiteUser";
    private static String LOG_TAG = "CreationAnnonceServiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_annonce_service);
        nb_swap = findViewById(R.id.editText_nbswap);
        valider = findViewById(R.id.button_valider_annonce_service);
        textview_date = findViewById(R.id.textview_date_service);
        spinner_categorie = findViewById(R.id.spinner_categorie);
        spinner_sous_categorie = findViewById(R.id.spinner_sous_categorie);
        description = findViewById(R.id.editText_description_service);


        //listener sur le spinner categories
        remplissageSpinnerCategorie();
        spinner_categorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                remplissageSpinnerSousCategorie();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listener sur le bouton valide
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validiteSaisie()){
                    String u = urlPHP();
                    new MakeNetworkCall().execute(u , "GET");
                    //TODO securiser le lancement
                    Toast toast = Toast.makeText(getApplicationContext(), "Annonce créée", Toast.LENGTH_LONG);
                    toast.show();
                    lancerMainPage();
                }
            }
        });

        //listener sur le textview date
        textview_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDate();
            }
        });
    }


    public void lancerMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //test de validité de la saisie du nb de swap
    // TODO definir les limites avec le groupe
    public boolean swapValide() {
        String swap = nb_swap.getText().toString();
        if (swap.isEmpty()){
            return false;
        }
        int nb = Integer.parseInt(nb_swap.getText().toString());
        if (nb > 100){
            return false;
        }
        else {
            return true;
        }
    }

    //test de validité de la date choisie
    public boolean timeValide() {
        //Recuperation de la date actuelle puis conversion des jours, mois... en chaines de char
        Calendar calendrier_now = Calendar.getInstance();
        int int_jour = calendrier_now.get(Calendar.DAY_OF_MONTH);
        int int_mois = calendrier_now.get(Calendar.MONTH);
        int int_annee = calendrier_now.get(Calendar.YEAR);

        if ((int_annee > mYear) || ((int_annee == mYear) && (int_mois > mMonth)) || ((int_annee == mYear) && (int_mois == mMonth) && (int_jour > mDay))){
            return false;
        }
        else{
            return true;
        }
    }

    //test de validité de la description
    public boolean validiteDescription(){
        boolean ok = true;
        description.setError(null);
        if (description.getText().toString().isEmpty()){
            ok = false;
            description.setError("Champs vide");
        }
        if (description.length() > 500){
            ok = false;
            description.setError("Description supérieur à 500 caractères");
        }
        return ok;
    }

    //test de validité de la saisie entiere
    public boolean validiteSaisie(){
        boolean ok = true;
        if (!swapValide()){
            ok = false;
        }
        if (!validiteDescription()){
            ok = false;
        }
        if (!timeValide()){
            ok = false;
        }
        if (spinner_sous_categorie.getSelectedItemPosition() == 0){
            ok = false;
        }
        return ok;
    }

    //choix de la date
    public void choiceDate() {
        final Calendar c_date = Calendar.getInstance();
        mYear = c_date.get(Calendar.YEAR);
        mMonth = c_date.get(Calendar.MONTH);
        mDay = c_date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textview_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mDay = dayOfMonth;
                        mYear = year;
                        mMonth = monthOfYear;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //remplissage spiner sous-categories
    public void remplissageSpinnerSousCategorie(){
        int pos = spinner_categorie.getSelectedItemPosition();
        if (pos == 1){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sous_categorie_service_pret_de_materiel, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sous_categorie.setAdapter(adapter);
        }
        else if (pos == 2){
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sous_categorie_services, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sous_categorie.setAdapter(adapter1);
        }
        else{
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.choose_categorie_service, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sous_categorie.setAdapter(adapter2);
        }
    }

    public void remplissageSpinnerCategorie(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorie_service, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categorie.setAdapter(adapter);
    }

    //construction des arguments à passer dans l'url
    public String argumentPHP(){
        AnnonceServiceClass annonce = creationObjectAnnonce();
        String param = "prenom=" + annonce.getPrenom() + "&"
                + "nom=" + annonce.getNom() + "&"
                + "nb_swap=" + annonce.getNb_swap() + "&"
                + "categorie=" + annonce.getCategorie() + "&"
                + "sous_categorie=" + annonce.getSous_categorie() + "&"
                + "date=" + annonce.getDate() + "&"
                + "description=" + annonce.getDescription();
        return param;

    }

    //creation de l'url pour la saisie BDD
    public String urlPHP(){
        String url;
        String param = argumentPHP();
        //envoie bdd
        url = "http://91.121.116.121/swapit/creer_annonce_service.php?" + param;
        return url;
    }

    //creation d'un objet annonce service
    public AnnonceServiceClass creationObjectAnnonce(){
        AnnonceServiceClass annonce = new AnnonceServiceClass();
        String swap = nb_swap.getText().toString();

        //TODO changer nom et prenom
        annonce.setPrenom(retrieveDataUser("prenom"));
        annonce.setNom(retrieveDataUser("nom"));
        annonce.setNb_swap(swap);
        annonce.setDate(textview_date.getText().toString());
        annonce.setDescription(description.getText().toString());
        annonce.setCategorie(spinner_categorie.getSelectedItem().toString());
        annonce.setSous_categorie(spinner_sous_categorie.getSelectedItem().toString());

        return annonce;
    }

    //recupere les données stockes de l'utilisateur
    public String retrieveDataUser(String id){
        SharedPreferences prefs_id = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE);
        String data = null;
        if (id.equals("nom")){
            data = prefs_id.getString("nom", "root");
        }
        else if (id.equals("prenom")){
            data = prefs_id.getString("prenom", "root");
        }
        return data;
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

    public void DisplayMessage(String a) {
        //TODO mettre popup
        /*
        TextView TxtResult = (TextView) findViewById(R.id.textView_test_service);
        TxtResult.setText(a);
        */
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                Log.e(LOG_TAG, "something went wrong");
            }

            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //DisplayMessage(result);
            Log.d(LOG_TAG, "Result: " + result);
        }
    }
}
