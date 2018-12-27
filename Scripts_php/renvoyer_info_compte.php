<?php

function renvoyer()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }

    catch (PDOException $e) {

        echo 'false';
        return;
    }

    $nom = $_GET['nom'];
    $adresse_mail = $_GET['adresse_mail'];
    $prenom = $_GET['prenom'];

    try {
        $rep = $bdd->prepare('SELECT * FROM compte_utilisateur WHERE nom = :nom AND prenom = :prenom AND adresse_mail = :adresse_mail');


        $rep->execute(array('nom' => $nom,
            'prenom' => $prenom,
            'adresse_mail' => $adresse_mail
        ));

        while ($donnees = $rep->fetch()) {
            $mdp = $donnees['mdp'];
            $annee = $donnees['annee'];
            $nbpoints = $donnees['nbpoints'];
            $numerotel = $donnees['numerotel'];
            $filiere = $donnees['filiere'];
            $biographie = $donnees['biographie'];
            $section = $donnees['section'];

        }

        //echo "nom : " . "$nom" . ", mdp : " . "$mdp" . ", adresse_mail : " . "$adresse_mail" . ", prenom : " . "$prenom" . ", annee : " . "$annee" . ", nbpoints : " . "$nbpoints" . ", numerotel : " . "$numerotel" . ", filiere : " . "$filiere" . ", biographie : " . "$biographie" . ", section : " . "$section";
        $s = '{';
        $s = $s . '"nom":"' . $nom . '"';
        $s = $s . ',"mdp":"' . $mdp . '"';
        $s = $s . ',"adresse_mail":"' . $adresse_mail . '"';
        $s = $s . ',"prenom":"' . $prenom . '"';
        $s = $s . ',"annee":"' . $annee . '"';
        $s = $s . ',"nbpoints":"' . $nbpoints . '"';
        $s = $s . ',"numerotel":"' . $numerotel . '"';
        $s = $s . ',"filiere":"' . $filiere . '"';
        $s = $s . ',"biographie":"' . $biographie . '"';
        $s = $s . ',"section":"' . $section . '"';
        $s = $s . '}';
        echo $s;
        $rep->closeCursor();
    }

    catch(PDOException $e){

        echo "false";

    }


}

renvoyer();