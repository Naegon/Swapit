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

    try {
        $rep = $bdd->prepare('UPDATE annonce_service SET variable = :variable WHERE nom = :nom AND prenom = :prenom');

        $nom = $_GET['nom'];
        $prenom = $_GET['prenom'];
        $variable = $_GET['variable'];

        $rep->execute(array(
            'variable' => $variable,
            'nom' => $nom,
            'prenom' => $prenom
        ));

        echo 'true';

        $rep->closeCursor();

    }
    catch(PDOException $e){

        echo 'false';

    }

}

update();