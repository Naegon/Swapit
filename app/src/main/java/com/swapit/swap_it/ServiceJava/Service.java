package com.swapit.swap_it.ServiceJava;

public class Service {
    private String Titre;
    private String Nom;
    private String Date;
    private String Swap;
    private String Description;


    public Service(){
    }

    public Service(String titre, String nom, String date, String swap, String description){
        Titre = titre;
        Nom = nom;
        Date = date;
        Swap = swap;
        Description = description;
    }

    public String getTitre(){
        return Titre;
    }

    public String getNom(){
        return Nom;
    }

    public String getDate(){
        return Date;
    }

    public String getSwap(){
        return Swap;
    }

    public String getDescription(){
        return Description;
    }

    public void setTitre(String titre){
        Titre = titre;
    }

    public void setNom(String nom){
        Nom = nom;
    }

    public void setDate(String date){
        Date = date;
    }

    public void setSwap(String swap){
        Swap = swap;
    }

    public void setDescription(String description){
        Description = description;
    }
}
