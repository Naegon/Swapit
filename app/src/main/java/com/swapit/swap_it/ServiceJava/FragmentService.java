package com.swapit.swap_it.ServiceJava;

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

public class FragmentService extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<Service> lstService;
    private static String LOG_TAG = "FragmentServiceTest";

    public FragmentService() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        Log.d(LOG_TAG,"Lst 3 : " + lstService.toString());


        v = inflater.inflate(R.layout.service_fragment, container, false);

        /*myrecyclerview = (RecyclerView) v.findViewById(R.id.service_recycler);
        RecyclerServiceAdapter recyclerAdapter = new RecyclerServiceAdapter(getContext(), lstService);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
*/
        CallBdd lstServiceHttp = new CallBdd("http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php");
        lstServiceHttp.volleyRequeteHttpCallBack(getContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd){
                if (retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                    afficherToast("Une erreur s'est produite");
                }
                else {
                    Log.d(LOG_TAG, "Récupération réussi : " + retourBdd);
                    remplissageService(retourBdd);

                    myrecyclerview = (RecyclerView) v.findViewById(R.id.service_recycler);
                    RecyclerServiceAdapter recyclerAdapter = new RecyclerServiceAdapter(getContext(), lstService);
                    myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    myrecyclerview.setAdapter(recyclerAdapter);

                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                afficherToast("Une erreur s'est produite");
            }
        });

        Log.d(LOG_TAG,"Lst 4 : " + lstService.toString());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");
        lstService = new ArrayList<>();


        /*
        lstService.add(new Service("Recherche hebergement post-POD", "Yves Remord", "04-12-18", "10","Help !\n Je cherche quelqu'un aui voudrais bien me prêter un petit coin de sol dans son chez lui après le pod ; on commence tôt le lendemain et j'habite loin\n\nPs : je ne ronfle pas :)"));
        lstService.add(new Service("Besoin d'un chargeur ASAP :(", "Lorie Culaire", "01-12-18", "25", "Viiiiiiiiite j'ai presque plus de batterie et j'attends un coup de téléphone important pour une opportunité de stage"));
        lstService.add(new Service("Trousse perdu en E34", "Agathe Zeblouse", "17-12-18", "18", "J'ai encore perdu ma trouse hier en cours d'anglais...\nElle est blu avec des paillettes ; y'a ma clef USB dedans avec mon nom sur l'étiquette"));
        lstService.add(new Service("Rendu d'un devoir à Mr. Lary Gollade", "Gaspard Alizan", "09-12-18", "51", "Je ne pourrais pas être à l'école le jour où on doit rendre un dossier important.\nIl suffit de le déposer dans le bon casier !"));
        lstService.add(new Service("Me cherche un thon/mayo au Franprix", "Pierre Kiroule", "18-12-18", "2", "Il fait très faim et je suis bloqué en séance de TP sur l'heure du déjeuné :(\nAidez moi à remplir mon estomac gargouillant"));
        lstService.add(new Service("Recherche covoiturage", "Harry Cover", "21-12-18", "30", "Vendredi prochain c'est encore ces maudîtes grèves et mon metro ne fonctionnera pas.\nJe recherche une âme charitable pour me permettre d'aller en cours ! J'habite 6 rue de l'église, à Ville-sur-Fleuve"));
        lstService.add(new Service("Besoin d'une calculatrice", "Baptiste Mathien", "18-12-18", "10", "Besoin d'une calculatrice"));
*/

        Log.d(LOG_TAG,"Lst 1 : " + lstService.toString());
        /*
        CallBdd lstServiceHttp = new CallBdd("http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php");
        lstServiceHttp.volleyRequeteHttpCallBack(getContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd){
                if (retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                    afficherToast("Une erreur s'est produite");
                }
                else {
                    Log.d(LOG_TAG, "Récupération réussi : " + retourBdd);
                    remplissageService(retourBdd);
                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                afficherToast("Une erreur s'est produite");
            }
        });
        */
        Log.d(LOG_TAG,"Lst 2 : " + lstService.toString());
    }

    public void callBddService(){
        CallBdd lstServiceHttp = new CallBdd("http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php");
        lstServiceHttp.volleyRequeteHttpCallBack(getContext(), new CallBdd.CallBackBdd() {
            @Override
            public void onSuccess(String retourBdd){
                if (retourBdd.equals("false")){
                    Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                    afficherToast("Une erreur s'est produite");
                }
                else {
                    Log.d(LOG_TAG, "Récupération réussi : " + retourBdd);
                    remplissageService(retourBdd);
                }
            }
            @Override
            public void onFail(String retourBdd) {
                Log.d(LOG_TAG, "Erreur dans la récupération des annonces services : " + retourBdd);
                afficherToast("Une erreur s'est produite");
            }
        });
    }

    public void afficherToast(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void remplissageService(String retour_BDD){
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

                //titre = obj.getString("matiere");
                //Log.d(LOG_TAG, "titre : " + titre);
                nom = obj.getString("nom");
                Log.d(LOG_TAG, "nom : " + nom);
                prenom = obj.getString("prenom");
                Log.d(LOG_TAG, "prenom : " + prenom);
                user = prenom + " " + nom;
                Log.d(LOG_TAG, "user : " + user);
                swap = obj.getString("nb_swap");
                Log.d(LOG_TAG, "swap : " + swap);
                date = obj.getString("date");
                Log.d(LOG_TAG, "date : " + date);
                description = obj.getString("description");
                Log.d(LOG_TAG, "desc : " + description);

                lstService.add(new Service(description, user, date, swap, description));

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

