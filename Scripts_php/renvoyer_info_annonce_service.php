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
    $date_limite = $_GET['date_limite'];
    $prenom = $_GET['prenom'];

    $rep = $bdd ->prepare('SELECT * FROM annonce_service WHERE nom = :nom AND prenom =:prenom AND date =:date_limite');


    try {
        $rep->execute(array('nom' => $nom,
            'prenom' => $prenom,
            'date_limite' => $date_limite
        ));

        while ($donnees = $rep->fetch()) {
            $nb_swap = $donnees['nb_swap'];
            $categorie = $donnees['categorie'];
            $sous_categorie = $donnees['sous_categorie'];
            $description = $donnees['description'];

        }

        $s = '{';
        $s = $s . '"nom":"' . $nom . '"';
        $s = $s . ',"prenom":"' . $prenom . '"';
        $s = $s . ',"date_limite":"' . $date_limite . '"';
        $s = $s . ',"nb_swap":"' . $nb_swap . '"';
        $s = $s . ',"categorie":"' . $categorie . '"';
        $s = $s . ',"sous_categorie":"' . $sous_categorie . '"';
        $s = $s . ',"description":"' . $description . '"';
        $s = $s . '}';
        echo $s;

        $rep->closeCursor();
    }

    catch (PDOException $e)
    {
        echo 'false';
    }
}

renvoyer();