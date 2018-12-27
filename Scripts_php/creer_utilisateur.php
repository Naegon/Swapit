<?php
// inserer un nouveau profil utilisateur dans la BDD
function inserer ()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch(PDOException $e) {

        echo 'false';
        return;
    }

    $nom = $_GET['nom'];
    $mdp = $_GET['mdp'];
    $adresse_mail = $_GET['adresse_mail'];
    $prenom = $_GET['prenom'];
    $annee = $_GET['annee'];
    $nbpoints = $_GET['nbpoints'];
    $numerotel = $_GET['numerotel'];
    $filiere = $_GET['filiere'];
    $biographie = $_GET['biographie'];
    $section = $_GET['section'];

    $test = 0;

    try {

        $rep = $bdd->prepare('SELECT adresse_mail FROM compte_utilisateur WHERE adresse_mail = :adresse_mail');

        $rep->execute(array('adresse_mail' => $adresse_mail
        ));

        while ($donnees = $rep->fetch())
        {
            if ($donnees['adresse_mail'] == $adresse_mail){

                $test = 1;

            }
        }
        $rep->closeCursor();
    }

    catch(PDOException $e){
        echo 'false';
    }

    if ($test == 0) {

        try {
            $rep = $bdd->prepare('INSERT INTO compte_utilisateur(nom,mdp,adresse_mail,prenom,annee,nbpoints,numerotel,filiere,biographie,section) VALUES(:nom,:mdp,:adresse_mail,:prenom,:annee,:nbpoints,:numerotel,:filiere,:biographie,:section)');


            $rep->execute(array(
                'nom' => $nom,
                'mdp' => $mdp,
                'adresse_mail' => $adresse_mail,
                'prenom' => $prenom,
                'annee' => $annee,
                'nbpoints' => $nbpoints,
                'numerotel' => $numerotel,
                'filiere' => $filiere,
                'biographie' => $biographie,
                'section' => $section
            ));
            echo 'true';

            $rep->closeCursor();

        }

        catch(PDOException $e)
        {
            echo 'false';
        }
    }

    else{
        echo 'false';
        }

}

inserer();

