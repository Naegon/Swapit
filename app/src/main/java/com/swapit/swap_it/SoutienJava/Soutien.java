package com.swapit.swap_it.SoutienJava;

public class Soutien {
    private String Titre;
    private String Nom;
    private String Date;
    private String Swap;
    private int Cat;
    private String Description;

    public Soutien(){
    }

    public Soutien(String titre, String nom, String date, String swap, int cat, String description){
        Titre = titre;
        Nom = nom;
        Date = date;
        Swap = swap;
        Cat = cat;
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

    public String getSwap() { return Swap; }

    public int getCat(){
        return Cat;
    }

    public String getDescription() { return Description; }


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

    public void setCat(int cat){
        Cat = cat;
    }

    public void setDescription(String description) { Description = description; }
}
