<?php

function renvoyer_service()
{
    try {
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch (PDOException $e) {

        echo 'false';
        return;
    }
    $nb = 0;

    $nom = $_GET['nom'];
    $prenom = $_GET['prenom'];

    $s = '{"annonce service": [';
    try {
        $rep = $bdd->prepare('SELECT * FROM annonce_service WHERE nom = :nom AND prenom = :prenom ');

        $rep->execute(array('nom' => $nom,
            'prenom' => $prenom
        ));

        while ($donnees = $rep->fetch()) {

            if ($nb>0) {

                $s = $s . ',';
            }

            $s = $s . '{';
            $s = $s . '"nom":"' . $nom . '"';
            $s = $s . ',"prenom":"' . $prenom . '"';
            $s = $s . ',"date":"' . $donnees['date'] . '"';
            $s = $s . ',"nb_swap":"' . $donnees['nb_swap'] . '"';
            $s = $s . ',"categorie":"' . $donnees['categorie'] . '"';
            $s = $s . ',"sous_categorie":"' . $donnees['sous_categorie'] . '"';
            $s = $s . ',"description":"' . $donnees['description'] . '"';
            $s = $s . '}';
            $nb = $nb + 1;

        }

        $s = $s . "]}";
        echo $s;
    }

    catch (PDOException $e)
    {
        echo 'false';
    }

}
renvoyer_service();
