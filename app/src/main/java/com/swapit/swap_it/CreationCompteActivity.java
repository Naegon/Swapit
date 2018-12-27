package com.swapit.swap_it;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreationCompteActivity extends AppCompatActivity {

    Button valider;
    EditText email, telephone, nom, prenom, bio, mdp, confirmer_mdp;
    Spinner spinner_section, spinner_promo, spinner_add;
    private static String LOG_TAG = "DEBUG_V";
    String retour_bdd;
    public static final String ISCREATION = "iscreation";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        valider = findViewById(R.id.validate_edit);
        email = findViewById(R.id.editMail);
        telephone = findViewById(R.id.editPhone);
        nom = findViewById(R.id.editSurname);
        prenom = findViewById(R.id.editName);
        spinner_section = findViewById(R.id.spinner_section);
        spinner_add = findViewById(R.id.spinner_add);
        spinner_promo = findViewById(R.id.spinner_promo);
        bio = findViewById(R.id.editDescription);
        mdp = findViewById(R.id.edit_password);
        confirmer_mdp = findViewById(R.id.edit_confirm_password);


        //listener sur le spinner de promp
        spinner_promo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                remplirSpinnerSection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listener sur le bouton valider
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validitéProfil()){
                    String url = urlPHP();
                    new MakeNetworkCall().execute(url, "GET");
                }
            }
        });
    }

    //affichage des toast echec/reussite
    public void afficherToastVrai(){
        Toast toast  = Toast.makeText(getApplicationContext(), "Compte crée", Toast.LENGTH_LONG);
        toast.show();
        lancerLogin();
    }

    public void afficherToastFaux(){
        Toast toast = Toast.makeText(getApplicationContext(), "Erreur dans la création du compte", Toast.LENGTH_LONG);
        toast.show();
        resetAll();
    }

    public void lancerLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void remplirSpinnerSection(int pos){
        if (pos > 3){ //remplissage majeur
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.majeure_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_section.setAdapter(adapter);
        }
        else{
            ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this, R.array.section_arrays, android.R.layout.simple_spinner_item);
            adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_section.setAdapter(adapter0);
        }
    }

    ///Fonctions de validité de la saisie
    //l'adresse mail doit contenir les champs efrei,esigetel ou efreitech
    //TODO : a completer en fonction de ce qu'on veut
    public boolean validitéEmail(){
        String string_email = email.getText().toString();
        if (string_email.contains("@efrei.net") || string_email.contains("@efreitech.net") || string_email.contains("@esigetel.net")){
            return true;
        }
        else{
            email.setError("L'adresse doit contenir @efrei @esigetel @efreitech");
            return false;
        }
    }

    public boolean validitéTelephone(){
        // converti en entier la chaine recuperer dans l'editText)
        //int num = Integer.parseInt(telephone.getText().toString());
        String string_numero = telephone.getText().toString();
        boolean ok = true;
        valider.setError(null);
        if (string_numero.length() < 10){
            telephone.setError("Numéro trop court");
            ok = false;
        }
        //rq : gere aussi le cas où le champs est vide
        if ((!string_numero.startsWith("06")) && (!string_numero.startsWith("07"))){
            telephone.setError("Numéro invalide");
            ok = false;
        }
        return ok;
    }

    public boolean validitéNom(){
        String string_nom = nom.getText().toString();
        boolean ok = true;
        if (string_nom.isEmpty()){
            nom.setError("Champs vide");
            ok = false;
        }
        return ok;
    }

    public boolean validitéPrénom(){
        String string_prenom = prenom.getText().toString();
        boolean ok = true;
        if (string_prenom.isEmpty()){
            prenom.setError("Champs vide");
            ok = false;
        }
        return ok;
    }

    public boolean validitéBio(){
        String string_bio = bio.getText().toString();
        boolean ok = true;
        if (string_bio.isEmpty()){
            bio.setError("champs vide");
            ok = false;
        }
        return ok;
    }

    public boolean validiteMdp(){
        // inferieur à 8 char
        // comparaison entre le valide et la confirmation
        boolean ok = true;
        mdp.setError(null);
        confirmer_mdp.setError(null);
        String password = mdp.getText().toString();
        String confirm = confirmer_mdp.getText().toString();


        if (!confirm.equals(password)){
            confirmer_mdp.setError("Mot de passe different");
            ok = false;
        }
        if (mdp.length() < 8){
            mdp.setError("Mot de passe inferieur à 8 characteres");
            ok = false;
        }
        if (password == null) {
            mdp.setError("Champs vide");
            ok = false;
        }
        if (confirm == null){
            confirmer_mdp.setError("Champs vide");
            ok = false;
        }
        return ok;
    }
    public boolean validitéProfil(){
        boolean ok = true;
        if (!validitéEmail()){
            ok = false;
        }
        if (!validitéNom()){
            ok = false;
        }
        if (!validitéPrénom()){
            ok = false;
        }
        if (!validitéTelephone()){
            ok = false;
        }
        if (!validitéBio()){
            ok = false;
        }
        if ((spinner_section.getSelectedItemPosition() == 0) || (spinner_promo.getSelectedItemPosition() == 0) || (spinner_add.getSelectedItemPosition() == 0)){
            ok = false;
        }
        if (!validiteMdp()){
            ok = false;
        }
        return ok;
    }

    //recuperer dans une chaine pour les parametre du script annonce
    public String argumentPHP(){
        //TODO enlever mot de passe en hardcode
        String param = "nom=" + nom.getText().toString() + "&"
                + "mdp=" + mdp.getText().toString() + "&"
                + "adresse_mail=" + email.getText().toString() + "&"
                + "prenom=" + prenom.getText().toString() + "&"
                + "annee=" + spinner_promo.getSelectedItem().toString() + "&"
                + "nbpoints=" + "100" + "&"
                + "numerotel=" + telephone.getText().toString() + "&"
                + "filiere=" + spinner_add.getSelectedItem().toString() + "&"
                + "biographie=" + bio.getText().toString() + "&"
                + "section=" + spinner_section.getSelectedItem().toString();
        return param;

    }

    public String urlPHP(){
        String url;
        String param = argumentPHP();
        //envoie bdd
        //url = "http://91.121.116.121/swapit/creer_utilisateur.php?" + param;
        //TODO changer call bdd
        url = "http://91.121.116.121/swapit/creer_utilisateur.php?" + param;
        Log.d(LOG_TAG, "URL : " + url);
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

    public void DisplayMessage(String a) {
        //TODO ICI affichage test
        //TextView TxtResult = (TextView) findViewById(R.id.textViewTest);
        //TxtResult.setText(a);
        //valider.setText(a);
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
            String res = "";


            is = ByGetMethod(URL);

            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            Log.d(LOG_TAG, "1 Retour BDD background: " + res);
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //remplirRetour(result);
            //remplirShared(result);
            if (result.equals("true")){
                afficherToastVrai();
            }
            else{
                afficherToastFaux();
            }
            Log.d(LOG_TAG, "3 Retour call BDD : " + result);
        }
    }

    //reset tout les champs à l'etat initial
    void resetAll(){
        prenom.setText(null);
        nom.setText(null);
        email.setText(null);
        telephone.setText(null);
        mdp.setText(null);
        confirmer_mdp.setText(null);
        bio.setText(null);
    }
}
