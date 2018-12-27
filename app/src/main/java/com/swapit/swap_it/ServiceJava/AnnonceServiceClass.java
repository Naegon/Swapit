package com.swapit.swap_it.ServiceJava;

public class AnnonceServiceClass {
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNb_swap() {
        return nb_swap;
    }

    public void setNb_swap(String nb_swap) {
        this.nb_swap = nb_swap;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSous_categorie() {
        return sous_categorie;
    }

    public void setSous_categorie(String sous_categorie) {
        this.sous_categorie = sous_categorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String prenom;
    String nb_swap;
    String categorie;
    String sous_categorie;
    String date;
    String description;
}
