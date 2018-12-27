package com.swapit.swap_it.SoutienJava;

public class AnnonceSoutienClass {

    public String getNom_createur() {
        return nom_createur;
    }

    public void setNom_createur(String nom_createur) {
        this.nom_createur = nom_createur;
    }

    public String getPrenom_createur() {
        return prenom_createur;
    }

    public void setPrenom_createur(String prenom_createur) {
        this.prenom_createur = prenom_createur;
    }

    public String getNb_swap() {
        return nb_swap;
    }

    public void setNb_swap(String nb_swap) {
        this.nb_swap = nb_swap;
    }

    public String getDate_limite() {
        return date_limite;
    }

    public void setDate_limite(String date_limite) {
        this.date_limite = date_limite;
    }


    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getUe_majeur() {
        return ue_majeur;
    }

    public void setUe_majeur(String ue_majeur) {
        this.ue_majeur = ue_majeur;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    String prenom_createur;
    String nom_createur;
    String nb_swap;
    String date_limite;
    String semestre;
    String ue_majeur;
    String matiere;
    String descripion;


}
