<?php

//renvoyer toutes les annonces : la premiere est celle avec la date la plus proche

function renvoyer()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    } catch (PDOException $e) {

        echo 'false';
        return;
    }

    $nb = 0;

    $s = '{"annonce": [';
    try {
        $rep = $bdd->query('SELECT * FROM annonce_service ORDER BY date');

        while ($donnees = $rep->fetch()) {

            if ($nb>0) {

                $s = $s . ',';
            }


            $s = $s . '{';
            $s = $s . '"nom":"' . $donnees['nom'] . '"';
            $s = $s . ',"prenom":"' . $donnees['prenom'] . '"';
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
renvoyer();
