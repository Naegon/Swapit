package com.swapit.swap_it.ServiceJava;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.swapit.swap_it.CallBdd;
import com.swapit.swap_it.MainActivity;
import com.swapit.swap_it.R;
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
                    /*
                    String u = urlPHP();
                    new MakeNetworkCall().execute(u , "GET");
                    //TODO securiser le lancement
                    */

                    final CallBdd creationAnnonceServiceHttp = new CallBdd("http://91.121.116.121/swapit/creer_annonce_service.php?");
                    argumentPHP(creationAnnonceServiceHttp);
                    creationAnnonceServiceHttp.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
                        @Override
                        public void onSuccess(String retourBdd) {
                            Log.d(LOG_TAG, "Call back success : " + retourBdd);
                            //si creation de l'annonce impossible
                            if(retourBdd.equals("false")){
                                Log.d(LOG_TAG, "Erreur dans la creation de l'annonce");
                                creationAnnonceServiceHttp.reset();
                                resetChamps();
                            }//si c'est bon
                            else{
                                Log.d(LOG_TAG, "Annonce crée");
                                afficherToast("Annonce crée");
                                lancerMainPage();
                            }
                        }
                        @Override
                        public void onFail(String retourBdd) {
                            Log.d(LOG_TAG, "Erreur dans la creation de l'annonce : " + retourBdd);
                            creationAnnonceServiceHttp.reset();
                            afficherToast("Erreur dans la creation de l'annonce");
                            resetChamps();
                        }
                    });
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

    /**
     * Affichage du toast avec @param message
     */
    public void afficherToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Lance la MainActivity
     */
    public void lancerMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Fonction de tests si les champs saisies sont valides
     */
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

    /**
     * Choix de la date + remplissage du textviex date
     */
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

    /**
     * Remplissage du spinner sous catégories en fonction du catégorie
     */
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

    /**
     * Remplissage du spinner catégorie
     */
    public void remplissageSpinnerCategorie(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorie_service, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categorie.setAdapter(adapter);
    }

    /**
     * Ajout des arguments à l'objet CallBdd
     */
    public void argumentPHP(CallBdd loginHttp){
        //creation de l'objet annonce
        AnnonceServiceClass annonce = creationObjectAnnonce();
        //recuperation des attributs
        loginHttp.ajoutArgumentPhpList("prenom", annonce.getPrenom());
        loginHttp.ajoutArgumentPhpList("nom", annonce.getNom());
        loginHttp.ajoutArgumentPhpList("nb_swap", annonce.getNb_swap());
        loginHttp.ajoutArgumentPhpList("categorie", annonce.getCategorie());
        loginHttp.ajoutArgumentPhpList("sous_categorie", annonce.getSous_categorie());
        loginHttp.ajoutArgumentPhpList("date",  annonce.getDate());
        loginHttp.ajoutArgumentPhpList("description", annonce.getDescription());
    }

    /**
     * Creation et retour d'un objet de type Annonce service
     */
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

    /**
     * Recupere les données utilisateur stockés dans le sharedpreference
     */
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

    /**
     * Remise à 0 de tous les champs de saisie
     */
    public void resetChamps(){
        final Calendar c_date = Calendar.getInstance();
        mYear = c_date.get(Calendar.YEAR);
        mMonth = c_date.get(Calendar.MONTH);
        mDay = c_date.get(Calendar.DAY_OF_MONTH);

        spinner_categorie.setSelection(0);
        spinner_sous_categorie.setSelection(0);
        textview_date.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        nb_swap.setText(null);
        description.setText(null);
    }
}
