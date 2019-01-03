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
    private static String LOG_TAG = "ProfileActivity";
    public static final String IDENTITE_USER = "IdentiteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TODO : ajouter un bouton recharger la page

        textView_description = findViewById(R.id.textView_profil_description_user);
        textView_mail = findViewById(R.id.textView_profil_mail);
        textView_nom = findViewById(R.id.textView_profil_nom);
        textView_prenom = findViewById(R.id.textView_profil_prenom);
        textView_swap = findViewById(R.id.textView_profil_swap);
        textView_tel = findViewById(R.id.textView_profil_telephone);
        json_retour = null;

        viderTextview();
        final CallBdd profileHttp = new CallBdd("http://91.121.116.121/swapit/renvoyer_info_compte.php?");
        argumentPHP(profileHttp);

        //call BDD
        profileHttp.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd) {
                if(retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la recuperation du profil : " + retourBdd);
                    profileHttp.reset();
                    afficherToast("Erreur dans la recuperation du profil");
                }//si c'est bon
                else{
                    Log.d(LOG_TAG, "Profil utilisateur  : " + retourBdd);
                    json(retourBdd);
                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "Erreur dans le call BDD : " + retourBdd);
                profileHttp.reset();
                afficherToast("Erreur dans l'appel à la BDD");
            }
        });
    }


    /**
     * Remise à 0 des textviews
     */
    public void viderTextview(){
        textView_tel.setText(null);
        textView_prenom.setText(null);
        textView_swap.setText(null);
        textView_nom.setText(null);
        textView_description.setText(null);
        textView_mail.setText(null);
    }

    /**
     * Ajoute les arguments PHP à l'objet CallBdd
     */
    public void argumentPHP(CallBdd profileHttp){
        profileHttp.ajoutArgumentPhpList("prenom", retrieveDataUser("prenom"));
        profileHttp.ajoutArgumentPhpList("nom", retrieveDataUser("nom"));
        profileHttp.ajoutArgumentPhpList("adresse_mail", retrieveDataUser("mail"));
    }

    /**
     * Renvoie la donnée de l'utilisateur stocké dans le shared preference à la clé @param id
     */
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

    /**
     * Remplie les tewtviews avec les données de l'utilisateur à partir de la chaine de retour du Call BDD
     */
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

    /**
     * Affiche un toast d'erreur avec le @param message
     */
    public void afficherToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
