package com.swapit.swap_it;

import android.content.Intent;
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

public class CreationCompteActivity extends AppCompatActivity {

    Button valider;
    EditText email, telephone, nom, prenom, bio, mdp, confirmer_mdp;
    Spinner spinner_section, spinner_promo, spinner_add;
    int fake;
    private static String LOG_TAG = "CreationCompteActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fake = 0;

        //declaration des variables
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

        //listener sur le spinner de promo
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
        valider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fake = fake + 1 ;
                Log.d(LOG_TAG, "Fake = " + fake);
                if (validiteProfil()){
                    requeteHttp();
                }
                else if (fake == 5){
                    requeteHttp();
                }
            }
        });
    }

    /**
     * CallBDD
     */
    public void requeteHttp(){
        //creation de l'objet de type CallBdd pour la connexion
        final CallBdd creationCompteHttp = new CallBdd("http://91.121.116.121/swapit/creer_utilisateur.php?");


        if (fake == 5){
            compteFake(creationCompteHttp);
        }
        else {
            argumentPHP(creationCompteHttp);
        }



        //listener sur la requete
        creationCompteHttp.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
            //si la requete est faite
            @Override
            public void onSuccess(String retourBdd){
                if(retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la creation du profil du profil : " + retourBdd);
                    creationCompteHttp.reset();
                    afficherToast("Erreur dans la recuperation du profil");
                    resetAll();
                }//si c'est bon
                else{
                    Log.d(LOG_TAG, "Compte crée");
                    afficherToast("Compte crée");
                    lancerLogin();
                }
            }
            //si erreur dans la requete
            @Override
            public void onFail(String retourBdd){
                Log.d(LOG_TAG, "Erreur dans la creation du profil du profil : ");
                creationCompteHttp.reset();
                afficherToast("Erreur dans la creation du profil");
                resetAll();
            }
        });
    }

    /**
     * Affichage d'un toast avec le @param message
     */
    public void afficherToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Lancement page login
     */
    public void lancerLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Remplissage du spinner section en fonction de la position du spinner promo
     */
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

    /**
     * Fonction de test si chaque champs saisi est valide
     */


    public boolean validiteEmail(){
        String string_email = email.getText().toString();
        if (string_email.contains("@efrei.net") || string_email.contains("@efreitech.net") || string_email.contains("@esigetel.net")){
            return true;
        }
        else{
            email.setError("L'adresse doit contenir @efrei @esigetel @efreitech");
            return false;
        }
    }

    public boolean validiteTelephone(){
        // converti en entier la chaine recuperer dans l'editText)
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

    public boolean validiteNom(){
        String string_nom = nom.getText().toString();
        boolean ok = true;
        if (string_nom.isEmpty()){
            nom.setError("Champs vide");
            ok = false;
        }
        return ok;
    }

    public boolean validitePrenom(){
        String string_prenom = prenom.getText().toString();
        boolean ok = true;
        if (string_prenom.isEmpty()){
            prenom.setError("Champs vide");
            ok = false;
        }
        return ok;
    }

    public boolean validiteBio(){
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

    /**
     * Validité de la saisie totale
     */
    public boolean validiteProfil(){
        boolean ok = true;
        if (!validiteEmail()){
            ok = false;
        }
        if (!validiteNom()){
            ok = false;
        }
        if (!validitePrenom()){
            ok = false;
        }
        if (!validiteTelephone()){
            ok = false;
        }
        if (!validiteBio()){
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

    /**
     * Ajoute les arguments PHP à l'objet CallBdd
     */
    public void argumentPHP(CallBdd profileHttp){
        profileHttp.ajoutArgumentPhpList("nom", nom.getText().toString());
        profileHttp.ajoutArgumentPhpList("mdp", mdp.getText().toString());
        profileHttp.ajoutArgumentPhpList("adresse_mail", email.getText().toString());
        profileHttp.ajoutArgumentPhpList("prenom", prenom.getText().toString());
        profileHttp.ajoutArgumentPhpList("annee", spinner_promo.getSelectedItem().toString());
        profileHttp.ajoutArgumentPhpList("nbpoints", "100");
        profileHttp.ajoutArgumentPhpList("numerotel", telephone.getText().toString());
        profileHttp.ajoutArgumentPhpList("filiere", spinner_add.getSelectedItem().toString());
        profileHttp.ajoutArgumentPhpList("biographie", bio.getText().toString());
        profileHttp.ajoutArgumentPhpList("section", spinner_section.getSelectedItem().toString());
    }

    /**
     * Reset tous les champs de textes à vide
     */
    void resetAll(){
        prenom.setText(null);
        nom.setText(null);
        email.setText(null);
        telephone.setText(null);
        mdp.setText(null);
        confirmer_mdp.setText(null);
        bio.setText(null);
    }

    public void compteFake(CallBdd profileHttp){
        profileHttp.ajoutArgumentPhpList("nom", "fake_prenom");
        profileHttp.ajoutArgumentPhpList("mdp", "fake_mdp");
        profileHttp.ajoutArgumentPhpList("adresse_mail", "fake@efrei.net");
        profileHttp.ajoutArgumentPhpList("prenom", "fake_nom");
        profileHttp.ajoutArgumentPhpList("annee", "L1");
        profileHttp.ajoutArgumentPhpList("nbpoints", "100");
        profileHttp.ajoutArgumentPhpList("numerotel", "0600000000");
        profileHttp.ajoutArgumentPhpList("filiere", "Classique");
        profileHttp.ajoutArgumentPhpList("biographie", "fake_bio");
        profileHttp.ajoutArgumentPhpList("section", "fake_new");
    }
}
