<?php


// modifier le mdp pour la personne avec ce nom prenom + adresse mail

// renvoyer oui/non et pas true/false

function update()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    } catch (PDOException $e) {

        echo 'false';
        return;
    }

    $nom = $_GET['nom'];
    $prenom = $_GET['prenom'];
    $mdp = $_GET['mdp'];
    $adresse_mail = $_GET['adresse_mail'];

    try
    {
        $rep = $bdd->prepare('UPDATE compte_utilisateur SET mdp = :mdp WHERE nom = :nom AND prenom = :prenom AND adresse_mail = :adresse_mail');

        $rep->execute(array(
            'mdp' => $mdp,
            'nom' => $nom,
            'prenom' => $prenom,
            'adresse_mail' => $adresse_mail
        ));

        echo'oui';
        $rep->closeCursor();
    }

    catch (PDOException $e)
    {
        echo 'non';
    }
}

update();