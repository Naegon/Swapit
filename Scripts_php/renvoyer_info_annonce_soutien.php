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

    $rep = $bdd ->prepare('SELECT * FROM annonce_soutien WHERE nom = :nom AND prenom =:prenom AND date_limite =:date_limite');


    $rep->execute(array('nom' => $nom,
        'prenom' => $prenom,
        'date_limite' =>$date_limite
    ));

    while ($donnees = $rep->fetch())
    {
        $nbswap = $donnees['nbswap'];
        $semestre = $donnees['semestre'];
        $ue_majeur = $donnees['ue_majeur'];
        $matiere = $donnees['matiere'];
        $description = $donnees['description'];

    }

    $s = '{';
    $s = $s . '"nom":"' . $nom . '"';
    $s = $s . ',"prenom":"' . $prenom . '"';
    $s = $s . ',"date_limite":"' . $date_limite . '"';
    $s = $s . ',"nbswap":"' . $nbswap . '"';
    $s = $s . ',"semestre":"' . $semestre . '"';
    $s = $s . ',"ue_majeur":"' . $ue_majeur . '"';
    $s = $s . ',"matiere":"' . $matiere . '"';
    $s = $s . ',"description":"' . $description . '"';
    $s = $s . '}';
    echo $s;
    $rep->closeCursor();
}

renvoyer();