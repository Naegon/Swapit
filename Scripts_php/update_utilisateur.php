<?php

//fonction pour mettre a jour
function update()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch(PDOException $e) {

        echo 'false';
        return;
    }

    $champ = $_GET['champ'];

    if ($champ == "adresse_mail") {
        try {
            $rep = $bdd->prepare('UPDATE compte_utilisateur SET adresse_mail = :adresse_mail WHERE nom = :nom AND prenom = :prenom AND mdp = :mdp');

            $prenom = $_GET['prenom'];
            $nom = $_GET['nom'];
            $mdp = $_GET['mdp'];
            $adresse_mail = $_GET['texte'];

            $rep->execute(array(
                'mdp' => $mdp,
                'nom' => $nom,
                'prenom' => $prenom,
                'adresse_mail' => $adresse_mail
            ));

            echo 'true';

            $rep->closeCursor();

        }
        catch (PDOException $e) {
            echo 'false';

        }
    }

    else if ($champ == "biographie" )
    {
        try {
            $rep = $bdd->prepare('UPDATE compte_utilisateur SET biographie = :biographie WHERE nom = :nom AND prenom = :prenom AND mdp = :mdp');

            $prenom = $_GET['prenom'];
            $nom = $_GET['nom'];
            $mdp = $_GET['mdp'];
            $biographie = $_GET['texte'];

            $rep->execute(array(
                'mdp' => $mdp,
                'nom' => $nom,
                'prenom' => $prenom,
                'biographie' => $biographie
            ));

            echo 'true';

            $rep->closeCursor();

        }
        catch (PDOException $e) {
            echo 'false';

        }
    }
    else if ($champ == "promo")
    {
        try {
            $rep = $bdd->prepare('UPDATE compte_utilisateur SET annee = :annee WHERE nom = :nom AND prenom = :prenom AND mdp = :mdp');

            $prenom = $_GET['prenom'];
            $nom = $_GET['nom'];
            $mdp = $_GET['mdp'];
            $annee = $_GET['texte'];

            $rep->execute(array(
                'mdp' => $mdp,
                'nom' => $nom,
                'prenom' => $prenom,
                'annee' => $annee
            ));

            echo 'true';

            $rep->closeCursor();

        }
        catch (PDOException $e) {
            echo 'false';

        }
    }
    else if ($champ == "numerotel")
    {
        try {
            $rep = $bdd->prepare('UPDATE compte_utilisateur SET numerotel = :numerotel WHERE nom = :nom AND prenom = :prenom AND mdp = :mdp');

            $prenom = $_GET['prenom'];
            $nom = $_GET['nom'];
            $mdp = $_GET['mdp'];
            $numerotel = $_GET['texte'];

            $rep->execute(array(
                'mdp' => $mdp,
                'nom' => $nom,
                'prenom' => $prenom,
                'numerotel' => $numerotel
            ));

            echo 'true';

            $rep->closeCursor();

        }
        catch (PDOException $e) {
            echo 'false';

        }
    }
    else if ($champ == "section")
    {
        try {
            $rep = $bdd->prepare('UPDATE compte_utilisateur SET section = :section WHERE nom = :nom AND prenom = :prenom AND mdp = :mdp');

            $prenom = $_GET['prenom'];
            $nom = $_GET['nom'];
            $mdp = $_GET['mdp'];
            $section = $_GET['texte'];

            $rep->execute(array(
                'mdp' => $mdp,
                'nom' => $nom,
                'prenom' => $prenom,
                'section' => $section
            ));

            echo 'true';

            $rep->closeCursor();

        }
        catch (PDOException $e) {
            echo 'false';

        }
    }
}

update();