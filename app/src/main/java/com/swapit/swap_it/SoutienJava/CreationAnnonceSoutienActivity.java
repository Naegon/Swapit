package com.swapit.swap_it.SoutienJava;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swapit.swap_it.CallBdd;
import com.swapit.swap_it.MainActivity;
import com.swapit.swap_it.R;
import java.util.Calendar;


public class CreationAnnonceSoutienActivity extends AppCompatActivity {

    Button valider;
    int mYear, mMonth, mDay;
    TextView textview_date;
    AutoCompleteTextView nb_swap;
    AutoCompleteTextView editext_description;
    Spinner spinner_semestre, spinner_matiere, spinner_ue;
    RadioButton radiobutton_license, radiobutton_master;
    RadioGroup radiogroup_cycle;
    String date;

    private static String LOG_TAG = "CreationAnnonceSoutienActivity";
    public static final String IDENTITE_USER = "IdentiteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_annonce_soutien);

        //Association variable bouton
        nb_swap = findViewById(R.id.editText_nombre_Swap);
        valider = findViewById(R.id.button_valider_annonce_soutien);
        editext_description = findViewById(R.id.autoCompleteTextView_description);
        radiobutton_license = findViewById(R.id.radioButton_licence);
        radiobutton_master = findViewById(R.id.radioButton_master);
        radiogroup_cycle = findViewById(R.id.radiogroup_cycle);
        spinner_semestre = findViewById(R.id.spinner_semestre);
        spinner_ue = findViewById(R.id.spinner_ue);
        spinner_matiere = findViewById(R.id.spinner_matiere);
        textview_date = findViewById(R.id.textView_date);


        //listener sur les spinners
        chooseCycle(); //remplissage des 3 spinners à la creation du semestre
        spinner_semestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //rempli les spinner matiere en fonction des radios boutons
                if (radiobutton_license.isChecked()){
                    remplisageSpinnerMatiereLicence();
                }
                else if (radiobutton_master.isChecked()){
                    remplissageSpinnerMatiereMaster();
                }
                else{
                    chooseCycle();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_ue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radiobutton_license.isChecked()){
                    remplisageSpinnerMatiereLicence();
                }
                else if (radiobutton_master.isChecked()){
                    remplissageSpinnerMatiereMaster();
                }
                else{
                    chooseCycle();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listner sur le texte viex date
        textview_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDate();
            }
        });

        //validation de la saisie
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validiterSaisie()){
                    //inserer ici appel des fonctions pour envoie des données à la BDD
                    //String u = urlPHP();
                    //new MakeNetworkCall().execute(u , "Get");

                    final CallBdd creationAnnonceSoutienHttp = new CallBdd("http://91.121.116.121/swapit/creer_annonce_soutien.php?");
                    argumentPHP(creationAnnonceSoutienHttp);
                    creationAnnonceSoutienHttp.volleyRequeteHttpCallBack(getApplicationContext(), new CallBdd.CallBackBdd() {
                        @Override
                        public void onSuccess(String retourBdd) {
                            Log.d(LOG_TAG, "Call back success : " + retourBdd);
                            //si creation de l'annonce impossible
                            if(retourBdd.equals("false")){
                                Log.d(LOG_TAG, "Erreur dans la creation de l'annonce");
                                creationAnnonceSoutienHttp.reset();
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
                            creationAnnonceSoutienHttp.reset();
                            afficherToast("Erreur dans la creation de l'annonce");
                            resetChamps();
                        }
                    });
                }
            }
        });
    }


    /**
     * Affichage du toast avec @param message
     */
    public void afficherToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG);
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
                        date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth; // stockage de la date à envoyé (format américain)
                        textview_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + year); // affichage dans de la date dans le texte view (format francais)
                        mDay = dayOfMonth;
                        mYear = year;
                        mMonth = monthOfYear;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    /**
     * Fonction de tests si les champs saisies sont valides
     */
    // TODO definir les limites avec le groupe
    public boolean swapValide() {
        String swap = nb_swap.getText().toString();
        nb_swap.setError(null);
        if (swap.isEmpty()){
            nb_swap.setError("Champs vide");
            return false;
        }
        int nb = Integer.parseInt(nb_swap.getText().toString());
        if (nb > 100){
            nb_swap.setError("Nombre de swap superieur à 100");
            return false;
        }
        else {
            return true;
        }
    }

    @SuppressLint("SimpleDateFormat")
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

    public boolean spinnerValide() {
        if ((spinner_ue.getSelectedItemPosition() == 0) || (spinner_semestre.getSelectedItemPosition() == 0)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean descriptionValide() {
        boolean ok = true;
        editext_description.setError(null);
        String description = editext_description.getText().toString();
        if (description.isEmpty()){
            ok = false;
        }
        if (description.length() > 500){
            editext_description.setError("Description supèrieur à 500 caractères");
            ok = false;
        }
        return ok;
    }

    public boolean validiterSaisie() {
        boolean annonce_valide = true;
        if (!swapValide()) {
            annonce_valide = false;
        }
        if (!spinnerValide()) {
            annonce_valide = false;
        }
        if (!timeValide()){
            annonce_valide = false;
        }
        if (!descriptionValide()) {
            editext_description.setError(null);
            editext_description.setError("Description vide");
            annonce_valide = false;
        }
        return annonce_valide;
    }

    public void chooseCycle(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choose_cycle, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semestre.setAdapter(adapter);
        spinner_ue.setAdapter(adapter);
        spinner_matiere.setAdapter(adapter);
    }

    /**
     * Listener click sur les radiobutton licence et master
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButton_licence:
                if (checked)
                    remplissageSpinnerSemestreLicense();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ue_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ue.setAdapter(adapter);
                break;
            case R.id.radioButton_master:
                if (checked)
                    remplissageSpinnerSemestreMaster();
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.majeure_array, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ue.setAdapter(adapter1);
                break;
        }
    }

    /**
     * Remplissage du spinner matiere
     */
    void chooseItemSpinnerMatiere() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choose_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_matiere.setAdapter(adapter);
    }

    /**
     * Remplissage du spinner semestre pour la licence
     */
    public void remplissageSpinnerSemestreLicense(){
        ArrayAdapter<CharSequence> adapter_semestre_licence = ArrayAdapter.createFromResource(this, R.array.semestre_licence_array, android.R.layout.simple_spinner_item);
        adapter_semestre_licence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semestre.setAdapter(adapter_semestre_licence);
    }

    /**
     * Remplissage du spinner semestre pour le master
     */
    public void remplissageSpinnerSemestreMaster(){
        ArrayAdapter<CharSequence> adapter_semestre_master = ArrayAdapter.createFromResource(this, R.array.semestre_master_array, android.R.layout.simple_spinner_item);
        adapter_semestre_master.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semestre.setAdapter(adapter_semestre_master);
    }

    /**
     * Remplissage des matiere de master en fonnction des deux autres spinnner
     */
    public void remplissageSpinnerMatiereMaster(){
        int ue_pos = spinner_ue.getSelectedItemPosition();
        int semestre_pos = spinner_semestre.getSelectedItemPosition();
        if (!spinnerValide()) {
            chooseItemSpinnerMatiere();
        } else if (semestre_pos == 1) {
            switch (ue_pos){
                case 1:
                    ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_avionique, android.R.layout.simple_spinner_item);
                    adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter0);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_cybersécuité, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter1);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_développement_logiciel, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter2);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_finance_de_marché, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter3);
                    break;
                case 5:
                    ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_IT_pour_entreprise, android.R.layout.simple_spinner_item);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter4);
                    break;
                case 6:
                    ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_réalité_virtuelle, android.R.layout.simple_spinner_item);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter5);
                    break;
                case 7:
                    ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_sciences_des_données_et_intelligence_artificielle, android.R.layout.simple_spinner_item);
                    adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter6);
                    break;
                case 8:
                    ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_systèmes_intelligents_et_robotiques, android.R.layout.simple_spinner_item);
                    adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter7);
                    break;
                case 9:
                    ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this, R.array.matiere_S7_systèmes_réseaux_et_bases_de_données, android.R.layout.simple_spinner_item);
                    adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter8);
                    break;
            }
        }
        else if (semestre_pos == 2){
            switch (ue_pos){
                case 1:
                    ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_avionique, android.R.layout.simple_spinner_item);
                    adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter9);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_cybersécuité, android.R.layout.simple_spinner_item);
                    adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter10);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter11 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_développement_logiciel, android.R.layout.simple_spinner_item);
                    adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter11);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter12 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_financement_de_marché, android.R.layout.simple_spinner_item);
                    adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter12);
                    break;
                case 5:
                    ArrayAdapter<CharSequence> adapter13 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_IT_pour_entreprise, android.R.layout.simple_spinner_item);
                    adapter13.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter13);
                    break;
                case 6:
                    ArrayAdapter<CharSequence> adapter14 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_réalité_virtuelle, android.R.layout.simple_spinner_item);
                    adapter14.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter14);
                    break;
                case 7:
                    ArrayAdapter<CharSequence> adapter15 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_sciences_des_données_et_intelligence_artificielle, android.R.layout.simple_spinner_item);
                    adapter15.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter15);
                    break;
                case 8:
                    ArrayAdapter<CharSequence> adapter16 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_systèmes_intelligents_et_robotiques, android.R.layout.simple_spinner_item);
                    adapter16.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter16);
                    break;
                case 9:
                    ArrayAdapter<CharSequence> adapter17 = ArrayAdapter.createFromResource(this, R.array.matiere_S8_systèmes_réseaux_et_bases_de_données, android.R.layout.simple_spinner_item);
                    adapter17.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter17);
                    break;
            }
        }
        else if (semestre_pos == 3){
            switch (ue_pos){
                case 1:
                    ArrayAdapter<CharSequence> adapter18 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_avionique, android.R.layout.simple_spinner_item);
                    adapter18.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter18);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter19 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_cybersécurité, android.R.layout.simple_spinner_item);
                    adapter19.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter19);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter20 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_Développement_Logiciel, android.R.layout.simple_spinner_item);
                    adapter20.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter20);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter21 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_Finance_de_Marché, android.R.layout.simple_spinner_item);
                    adapter21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter21);
                    break;
                case 5:
                    ArrayAdapter<CharSequence> adapter22 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_IT_Pour_l_entreprise, android.R.layout.simple_spinner_item);
                    adapter22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter22);
                    break;
                case 6:
                    ArrayAdapter<CharSequence> adapter23 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_Image_et_réalité_virtuelle, android.R.layout.simple_spinner_item);
                    adapter23.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter23);
                    break;
                case 7:
                    ArrayAdapter<CharSequence> adapter24 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_sciences_des_données_et_intelligence_artificielle, android.R.layout.simple_spinner_item);
                    adapter24.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter24);
                    break;
                case 8:
                    ArrayAdapter<CharSequence> adapter25 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_Systèmes_intelligents_et_robotique, android.R.layout.simple_spinner_item);
                    adapter25.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter25);
                    break;
                case 9:
                    ArrayAdapter<CharSequence> adapter26 = ArrayAdapter.createFromResource(this, R.array.matiere_S9_Systèmes_réseaux_et_bases_de_données, android.R.layout.simple_spinner_item);
                    adapter26.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter26);
                    break;
            }
        }
    }

    /**
     * Remplissage des matiere de licence en fonnction des deux autres spinnner
     */
    public void remplisageSpinnerMatiereLicence() {
        int ue_pos = spinner_ue.getSelectedItemPosition();
        int semestre_pos = spinner_semestre.getSelectedItemPosition();
        if (!spinnerValide()) {
            chooseItemSpinnerMatiere();
        } else if (semestre_pos == 1) {
            switch (ue_pos) {
                case 1:
                    ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this, R.array.matiere_S1_formation_generale, android.R.layout.simple_spinner_item);
                    adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter0);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.matiere_S1_informatique, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter1);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.matiere_S1_mathematiques, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter2);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.matiere_S1_physique, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter3);
                    break;
            }
        } else if (semestre_pos == 2) {
            switch (ue_pos) {
                case 1:
                    ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.matiere_S2_formation_generale, android.R.layout.simple_spinner_item);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter4);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.matiere_S2_informatique, android.R.layout.simple_spinner_item);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter5);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this, R.array.matiere_S2_mathematiques, android.R.layout.simple_spinner_item);
                    adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter6);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this, R.array.matiere_S2_physique, android.R.layout.simple_spinner_item);
                    adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter7);
                    break;
            }
        } else if (semestre_pos == 3) {
            switch (ue_pos) {
                case 1:
                    ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this, R.array.matiere_S3_formation_generale, android.R.layout.simple_spinner_item);
                    adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter8);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(this, R.array.matiere_S3_informatique, android.R.layout.simple_spinner_item);
                    adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter9);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(this, R.array.matiere_S3_mathematiques, android.R.layout.simple_spinner_item);
                    adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter10);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter11 = ArrayAdapter.createFromResource(this, R.array.matiere_S3_physique, android.R.layout.simple_spinner_item);
                    adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter11);
                    break;
            }
        } else if (semestre_pos == 4) {
            switch (ue_pos) {
                case 1:
                    ArrayAdapter<CharSequence> adapter12 = ArrayAdapter.createFromResource(this, R.array.matiere_S4_formation_generale, android.R.layout.simple_spinner_item);
                    adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter12);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter13 = ArrayAdapter.createFromResource(this, R.array.matiere_S4_informatique, android.R.layout.simple_spinner_item);
                    adapter13.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter13);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter14 = ArrayAdapter.createFromResource(this, R.array.matiere_S4_mathematiques, android.R.layout.simple_spinner_item);
                    adapter14.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter14);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter15 = ArrayAdapter.createFromResource(this, R.array.matiere_S4_physique, android.R.layout.simple_spinner_item);
                    adapter15.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter15);
                    break;
            }
        } else if (semestre_pos == 5){
            switch (ue_pos){
                case 1:
                    ArrayAdapter<CharSequence> adapter16 = ArrayAdapter.createFromResource(this, R.array.matiere_S5_formation_generale, android.R.layout.simple_spinner_item);
                    adapter16.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter16);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter17 = ArrayAdapter.createFromResource(this, R.array.matiere_S5_informatique, android.R.layout.simple_spinner_item);
                    adapter17.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter17);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter18 = ArrayAdapter.createFromResource(this, R.array.no_matiere, android.R.layout.simple_spinner_item);
                    adapter18.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter18);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter19 = ArrayAdapter.createFromResource(this, R.array.no_matiere, android.R.layout.simple_spinner_item);
                    adapter19.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter19);
                    break;
            }
        }else if (semestre_pos == 6){
            switch (ue_pos){
                case 1:
                    ArrayAdapter<CharSequence> adapter20 = ArrayAdapter.createFromResource(this, R.array.matiere_S6_formation_generale, android.R.layout.simple_spinner_item);
                    adapter20.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter20);
                    break;
                case 2:
                    ArrayAdapter<CharSequence> adapter21 = ArrayAdapter.createFromResource(this, R.array.matiere_S6_informatique, android.R.layout.simple_spinner_item);
                    adapter21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter21);
                    break;
                case 3:
                    ArrayAdapter<CharSequence> adapter22 = ArrayAdapter.createFromResource(this, R.array.matiere_S6_mathematiques, android.R.layout.simple_spinner_item);
                    adapter22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter22);
                    break;
                case 4:
                    ArrayAdapter<CharSequence> adapter23 = ArrayAdapter.createFromResource(this, R.array.matiere_S6_physique, android.R.layout.simple_spinner_item);
                    adapter23.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_matiere.setAdapter(adapter23);
                    break;
            }
        }
    }

    /**
     * Creation et retour d'un objet de type Annonce service
     */
    public AnnonceSoutienClass creationObjectAnnonce(){
        AnnonceSoutienClass annonce = new AnnonceSoutienClass();
        String swap = nb_swap.getText().toString();

        annonce.setPrenom_createur(retrieveDataUser("prenom"));
        annonce.setNom_createur(retrieveDataUser("nom"));
        annonce.setNb_swap(swap);
        annonce.setDate_limite(date);
        annonce.setDescripion(editext_description.getText().toString());
        annonce.setMatiere(spinner_matiere.getSelectedItem().toString());
        annonce.setSemestre(spinner_semestre.getSelectedItem().toString());
        annonce.setUe_majeur(spinner_ue.getSelectedItem().toString());
        return annonce;
    }

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
     * Ajout des arguments à l'objet CallBdd
     */
    public void argumentPHP(CallBdd loginHttp){
        //creation de l'objet annonce
        AnnonceSoutienClass annonce = creationObjectAnnonce();
        //recuperation des attributs
        loginHttp.ajoutArgumentPhpList("prenom", annonce.getPrenom_createur());
        loginHttp.ajoutArgumentPhpList("nom", annonce.getNom_createur());
        loginHttp.ajoutArgumentPhpList("nbswap", annonce.getNb_swap());
        loginHttp.ajoutArgumentPhpList("semestre", annonce.getSemestre());
        loginHttp.ajoutArgumentPhpList("ue_majeur", annonce.getUe_majeur());
        loginHttp.ajoutArgumentPhpList("matiere", annonce.getMatiere());
        loginHttp.ajoutArgumentPhpList("date_limite", annonce.getDate_limite());
        loginHttp.ajoutArgumentPhpList("description", annonce.getDescripion());
    }

    /**
     * Remise à 0 de tous les champs de saisie
     */
    public void resetChamps(){
        final Calendar c_date = Calendar.getInstance();
        mYear = c_date.get(Calendar.YEAR);
        mMonth = c_date.get(Calendar.MONTH);
        mDay = c_date.get(Calendar.DAY_OF_MONTH);

        spinner_ue.setSelection(0);
        spinner_semestre.setSelection(0);
        spinner_matiere.setSelection(0);
        textview_date.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
        nb_swap.setText(null);
        editext_description.setText(null);
    }
}