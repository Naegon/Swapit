<?php
// inserer un nouveau profil utilisateur dans la BDD
function login ()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch(PDOException $e) {

        echo 'false';
        return;
    }

    $adresse_mail = $_GET['adresse_mail'];
    $mdp = $_GET['mdp'];
    $test = 1;

    $rep = $bdd->prepare('SELECT * FROM compte_utilisateur WHERE adresse_mail = :adresse_mail AND mdp = :mdp');


    $rep->execute(array('adresse_mail' => $adresse_mail,
        'mdp' => $mdp
    ));

    if ($rep->fetch() == NULL) {

        $test = 0;
        echo "false";
    }

    if ($test == 1) {
        $rep->execute(array('adresse_mail' => $adresse_mail,
            'mdp' => $mdp
        ));

        while ($donnees = $rep->fetch()) {
            $prenom = $donnees['prenom'];
            $nom = $donnees['nom'];

        }

        $s = '{';
        $s = $s . '"prenom":"' . $prenom . '"';
        $s = $s . ',"nom":"' . $nom . '"';
        $s = $s . '}';
        echo $s;
    }

}

login();