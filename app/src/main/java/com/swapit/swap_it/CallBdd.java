package com.swapit.swap_it;

import android.content.Context;
import android.media.MediaRouter;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class CallBdd {
    private static String LOG_TAG = "CallBdd";

    String Json; //chaine charactere de retour au format Json
    String url; //url de base sans parametre PHP
    String urlPhp; //url de base avec parametre PHP
    private ArrayList<String> parametrePhp; //Liste des parametre PHP


    /**
     * Get et set des variables
    **/
    public String getUrlPhp() {
        return urlPhp;
    }

    public void setUrlPhp(String urlPhp) {
        this.urlPhp = urlPhp;
    }

    public String getJson() {
        return Json;
    }

    public void setJson(String json) {
        Json = json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Constructeur
     * url et urlPHP sont remplis avec la l'url de base
    **/
    public CallBdd(String pUrl){
        Json = "";
        url = pUrl;
        urlPhp = pUrl;
        parametrePhp = new ArrayList<String>();
    }

    /**
     * Ajout des parametres PHP (couple clé/valeur) à la liste
     */
    public void ajoutArgumentPhpList(String cle, String valeur){
        parametrePhp.add(cle);
        parametrePhp.add(valeur);
        Log.d(LOG_TAG, "Liste param " + parametrePhp.toString());
    }

    private void ajoutArgumentPhpUrl(ArrayList<String> param){
        for (int i = 0 ; i < param.size() ; i++){
            if ((i % 2) == 0){
                urlPhp += param.get(i) + "=";
            }
            else{
                urlPhp += param.get(i);
                if (i != (param.size()-1)){
                    urlPhp += "&";
                }
            }
            Log.d(LOG_TAG, "URL PHP " + urlPhp);
        }
    }

    /**
     * Ajout des parametres à l'url
     * Envoie de la requete HTTP avec urlPhp
     * Chaine de retour stocké dans Json
     */
    public void volleyRequeteHttp(Context context){
        ajoutArgumentPhpUrl(parametrePhp);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPhp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG, "Retour BDD " + response);

                setJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setJson("");
            }
        });
        queue.add(stringRequest);
    }

    public void volleyRequeteHttpCallBack(Context context, final LoginActivity.CallBackBdd callback){
        ajoutArgumentPhpUrl(parametrePhp);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPhp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFail();
            }
        });
        queue.add(stringRequest);
    }

    public void reset(){
        Json = "";
        url = "";
        urlPhp = "";
        parametrePhp = null;
    }
}
