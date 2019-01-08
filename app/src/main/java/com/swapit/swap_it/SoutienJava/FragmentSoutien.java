package com.swapit.swap_it.SoutienJava;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swapit.swap_it.CallBdd;
import com.swapit.swap_it.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentSoutien extends Fragment {
    View v;
    private RecyclerView myrecyclerview;
    private List<Soutien> lstSoutien;
    private static String LOG_TAG = "FragmentSoutienTest";

    public FragmentSoutien() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.soutien_fragment, container, false);

        callBddSoutien();
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstSoutien = new ArrayList<>();

/*
        lstSoutien.add(new Soutien("Champs éléctromagnétiques", "Sarah Dote", "26-12-18", "42", R.drawable.ic_school, "Bonjour, j'ai vraiment besoin d'aide en champs électromagnétiques, surtout à l'approche des DE"));
        lstSoutien.add(new Soutien("Mathématiques du réel", "Anna Bolisant", "01-02-18", "50", R.drawable.ic_school, "Salut salut ! Les maths c'est pas trop ma spé, un ptit coup de main serait pas de refus. \nCheers !"));
        lstSoutien.add(new Soutien("Physique quantique", "Anny Versaire", "17-02-18", "20", R.drawable.ic_school, "Hey !\nMoi c'est Anny, je patauge sévère en physique quantique, j'aurais bien besoin d'aide pour refaire les TD :/\nJ'ai assez peur que les examens tombent sur quelque chose que l'on à jamais vu ni en cours ni en TD... Absurde non ?"));
        lstSoutien.add(new Soutien("Économie", "Alain Terrieur", "08-10-18", "12", R.drawable.ic_school, "Salut les amis\nJe recherche une âme charitable pour donner un peu de son temps afin de rattraper mon niveau déplorable en éco.\nDe l'aide pour faire les TAF serait la bienvenue !!"));
        lstSoutien.add(new Soutien("Histoire des Sciences", "Lara Clayte", "06-11-18", "42", R.drawable.ic_school, "Bonjour j'ai pas trop suivit en cours d'histoire des sciences si quelqu'un pouvais m'aider à rattraper mon retard s'il vous plait"));
        lstSoutien.add(new Soutien("Communication", "Jacques Ouwzi", "04-09-18", "31", R.drawable.ic_school, "Bonjour, j'ai besoin d'aide afin de comprendre mieux la méthode de la dissertation. J'ai vraiment du mal et je ne sais pas trop comment m'exercer !"));
        lstSoutien.add(new Soutien("Système de transmission", "Alain Terrieur", "24-12-18", "26", R.drawable.ic_school, "Bande de Bessel ? DSBSC sans porteuse cosinus de truc ? Connais pas :/ Si quelqu'un a un peu de patience pour m'expliquer tout ca la... C'est pas de refus"));
        lstSoutien.add(new Soutien("Système de transmission", "Baptiste Mathien", "21-12-18", "20", R.drawable.ic_school, "Besoin de comprendre la PLL"));
*/
    }

    /**
     * Apple base de données pour récuperer toutes annonces en JSON
     * Si appel réussi : creer le recycler view
     */
    public void callBddSoutien(){
        CallBdd lstSoutienHttp = new CallBdd("http://91.121.116.121/swapit/renvoyer_info_annonce_soutien_max.php");
        lstSoutienHttp.volleyRequeteHttpCallBack(getContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd){
                if (retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                    afficherToast("Une erreur s'est produite");
                }
                else {
                    Log.d(LOG_TAG, "Récupération réussi : " + retourBdd);
                    remplissageSoutien(retourBdd);
                    setupRecyclerViewSoutien();
                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                afficherToast("Une erreur s'est produite");
            }
        });
    }

    public void setupRecyclerViewSoutien(){
        myrecyclerview = (RecyclerView) v.findViewById(R.id.soutien_recycler);
        RecyclerSoutienAdapter recyclerAdapter = new RecyclerSoutienAdapter(getContext(), lstSoutien);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
    }

    public void afficherToast(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Récupere chaque annonce depuis la chaine de retour de la BDD au format JSON
     * Ajoute les annonces à la liste
     */
    public void remplissageSoutien(String retour_BDD){
        String titre = "";
        String nom = "";
        String prenom = "";
        String user = "";
        String date = "";
        String swap = "";
        String description = "";
        String res = "";

        try {
            JSONObject json = new JSONObject(retour_BDD);
            JSONArray array = new JSONArray(json.getString("annonce"));
            for (int i = 0; i < array.length() ; i++ ){
                Log.d(LOG_TAG, "Annonce n° : " + i);

                JSONObject obj = new JSONObject(array.getString(i));

                titre = obj.getString("matiere");
                Log.d(LOG_TAG, "titre : " + titre);
                nom = obj.getString("nom");
                Log.d(LOG_TAG, "nom : " + nom);
                prenom = obj.getString("prenom");
                Log.d(LOG_TAG, "prenom : " + prenom);
                user = prenom + " " + nom;
                Log.d(LOG_TAG, "user : " + user);
                swap = obj.getString("nbswap");
                Log.d(LOG_TAG, "swap : " + swap);
                date = obj.getString("date_limite");
                Log.d(LOG_TAG, "date : " + date);
                description = obj.getString("description");
                Log.d(LOG_TAG, "desc : " + description);

                lstSoutien.add(new Soutien(titre,user , date , swap , R.drawable.ic_school , description));
                //lstSoutien.add(new Soutien("Économie", "Alain Terrieur", "08/10", "12", R.drawable.ic_school, "Salut les amis\nJe recherche une âme charitable pour donner un peu de son temps afin de rattraper mon niveau déplorable en éco.\nDe l'aide pour faire les TAF serait la bienvenue !!"));
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Erreur");
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
