package com.swapit.swap_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;


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

import com.swapit.swap_it.CallBdd;

// Page pour se connecter à son compte
public class LoginActivity extends AppCompatActivity {

    Button button_creer_compte, button_connexion;
    EditText editText_email, editText_mdp;
    CheckBox checkBox_rester_co;
    private static String LOG_TAG = "LoginActivity";
    public static final String IDENTITE_USER = "IdentiteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciation des variables
        button_connexion = findViewById(R.id.button_connexion);
        button_creer_compte = findViewById(R.id.button_creer_compte);
        editText_email = findViewById(R.id.edittext_login_email);
        editText_mdp = findViewById(R.id.edittext_login_mdp);
        checkBox_rester_co = findViewById(R.id.checkBox_login_connexion);
        final SharedPreferences pref_user = getSharedPreferences(IDENTITE_USER, MODE_PRIVATE);
        SharedPreferences.Editor pref_user_editor = getSharedPreferences(IDENTITE_USER,MODE_PRIVATE).edit();

        //Gestion option garder en mémoire mail/mdp
        //TODO il faut rappeler la connexion ( /!\ si l'utilisateur modifie la saisie)
        if (pref_user.getString("keep", "null").equals("true")){
            editText_mdp.setText(pref_user.getString("mdp", "null"));
            editText_email.setText(pref_user.getString("mail", "null"));
            checkBox_rester_co.setChecked(true);
            button_connexion.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    requeteHttp();
                }
            });
        }

        //Gestion bouton création du compte
        button_creer_compte.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerPage(1);
            }
        });

        //Gestion bouton login
        button_connexion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //si la saisie est valide (champs non vide)
                if (validiteSaisie()){
                    requeteHttp();
                }
            }
        });
    }

    /**
     * Call BDD
     */
    public void requeteHttp(){
        //creation de l'objet de type CallBdd pour la connexion
        final CallBdd loginHttp = new CallBdd("http://91.121.116.121/swapit/login.php?");
        argumentPHP(loginHttp);

        //listener sur la requete
        loginHttp.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
            //si la requete est faite
            @Override
            public void onSuccess(String retourBdd){
                Log.d(LOG_TAG, "Call back success");
                //si le mdp/login est faux
                if(retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Mauvais login/mdp : " + retourBdd);
                    loginHttp.reset();
                    badMdp();
                }//si c'est bon
                else{
                    Log.d(LOG_TAG, "Bon login : " + retourBdd);
                    json(retourBdd);
                    lancerPage(0);
                }
            }
            //si erreur dans la requete
            @Override
            public void onFail(String retourBdd){
                Log.d(LOG_TAG, "Call back fail, erreur : " + retourBdd);
                loginHttp.reset();
                badMdp();
                ///TODO insert toast "une erreur s'est produite"
            }
        });
    }

    /**
     * Quitte l'appli sur retour en arrière
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        super.onBackPressed();
    }

    /**
     * Lance page principale ou creation compte
     * @param num 0 = main page 1 = creation profil
     */
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

    /**
     * @return true = champs mail non vide, false = champs mail vide
     */
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

    /**
     * @return true = champs mdp non vide, false = champs mdp vide
     */
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

    /**
     * Saisie valide
     */
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

    /**
     * Ajout des arguments (couple clé/valeur) à la liste
     */
    public void argumentPHP(CallBdd loginHttp){
        loginHttp.ajoutArgumentPhpList("adresse_mail", editText_email.getText().toString());
        loginHttp.ajoutArgumentPhpList("mdp", editText_mdp.getText().toString());
    }

    /**
     * Json to string
     * Garde infos utilisateur dans le shared preference
     */
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
            Log.d(LOG_TAG, "Erreur convertir json");
        }
    }

    /**
     * Reset tous les champs
     */
    public void badMdp(){
        editText_mdp.setError("Email/Mot de passe erroné");
        editText_email.setError("Email/Mot de passe erroné");
        editText_email.setText(null);
        editText_mdp.setText(null);
    }
}

