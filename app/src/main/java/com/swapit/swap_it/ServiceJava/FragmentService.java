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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.service_fragment, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.service_recycler);
        RecyclerServiceAdapter recyclerAdapter = new RecyclerServiceAdapter(getContext(), lstService);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstService = new ArrayList<>();

        String url = "http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php";
        Log.d(LOG_TAG, url);
        new MakeNetworkCall().execute(url, "GET");
        lstService.add(new Service("Recherche hebergement post-POD", "Yves Remord", "04-12-18", "10","Help !\n Je cherche quelqu'un aui voudrais bien me prêter un petit coin de sol dans son chez lui après le pod ; on commence tôt le lendemain et j'habite loin\n\nPs : je ne ronfle pas :)"));
        lstService.add(new Service("Besoin d'un chargeur ASAP :(", "Lorie Culaire", "01-12-18", "25", "Viiiiiiiiite j'ai presque plus de batterie et j'attends un coup de téléphone important pour une opportunité de stage"));
        lstService.add(new Service("Trousse perdu en E34", "Agathe Zeblouse", "17-12-18", "18", "J'ai encore perdu ma trouse hier en cours d'anglais...\nElle est blu avec des paillettes ; y'a ma clef USB dedans avec mon nom sur l'étiquette"));
        lstService.add(new Service("Rendu d'un devoir à Mr. Lary Gollade", "Gaspard Alizan", "09-12-18", "51", "Je ne pourrais pas être à l'école le jour où on doit rendre un dossier important.\nIl suffit de le déposer dans le bon casier !"));
        lstService.add(new Service("Me cherche un thon/mayo au Franprix", "Pierre Kiroule", "18-12-18", "2", "Il fait très faim et je suis bloqué en séance de TP sur l'heure du déjeuné :(\nAidez moi à remplir mon estomac gargouillant"));
        lstService.add(new Service("Recherche covoiturage", "Harry Cover", "21-12-18", "30", "Vendredi prochain c'est encore ces maudîtes grèves et mon metro ne fonctionnera pas.\nJe recherche une âme charitable pour me permettre d'aller en cours ! J'habite 6 rue de l'église, à Ville-sur-Fleuve"));
        lstService.add(new Service("Besoin d'une calculatrice", "Baptiste Mathien", "18-12-18", "10", "Besoin d'une calculatrice"));
    }


/*

        @Override
        public void onStart() {
            super.onStart();
            lstService = new ArrayList<>();
            String url = "http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php";
            Log.d(LOG_TAG, url);
            new MakeNetworkCall().execute(url, "GET");
        }

        @Override
        public void onResume() {
            super.onResume();
            lstService = new ArrayList<>();
            String url = "http://91.121.116.121/swapit/renvoyer_info_annonce_service_max.php";
            Log.d(LOG_TAG, url);
            new MakeNetworkCall().execute(url, "GET");
        }
    */
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
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //DisplayMessage("Please Wait ...");
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

            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            remplissageService(result);
            //DisplayMessage(result);
            Log.d(LOG_TAG, "Result: " + result);
        }
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
        Log.d(LOG_TAG, "res : " + retour_BDD);

        try {
            JSONObject json = new JSONObject(retour_BDD);
            JSONArray array = new JSONArray(json.getString("annonce"));
            for (int i = 0; i < array.length() ; i++ ){
                Log.d(LOG_TAG, "Compte : " + i);

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

