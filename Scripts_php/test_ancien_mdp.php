<?php

// tester sil y a un compte avec ce nom,prenom,adresse mail + mdp


function testermdp()
{

    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    } catch (PDOException $e) {

        echo 'false';
        return;
    }

    $prenom = $_GET['prenom'];
    $nom = $_GET['nom'];
    $adresse_mail = $_GET['adresse_mail'];
    $mdp = $_GET['mdp'];
    $test = 0;

    try {

        $rep = $bdd->prepare('SELECT mdp FROM compte_utilisateur WHERE nom = :nom AND prenom = :prenom AND adresse_mail = :adresse_mail');

        $rep->execute(array('adresse_mail' => $adresse_mail,
            'nom' => $nom,
            'prenom' => $prenom
        ));

        while ($donnees = $rep->fetch()) {
            if ($donnees['mdp'] == $mdp) {

                $test = 1;

            }
        }
        $rep->closeCursor();
    } catch (PDOException $e) {
        echo 'false';
    }

    if ($test == 1)
    {
        echo 'true';
    }

    else
    {
        echo 'false';
    }
}

testermdp();
