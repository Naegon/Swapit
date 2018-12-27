<?php
// fonction pour supprimer une ligne
function delete()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch(PDOException $e) {

        echo 'false';
        return;
    }

    $nom = $_GET['nom'];
    $prenom = $_GET['prenom'];
    $adresse_mail = $_GET['adresse_mail'];

    //delete annonce soutien
    try{
        $rep = $bdd->prepare('DELETE FROM annonce_soutien WHERE nom = :nom AND prenom = :prenom');

        $rep->execute(array(
            'nom' => $nom,
            'prenom' => $prenom
        ));

        $rep->closeCursor();
    }
    catch(PDOException $e)
    {
        echo 'false';
        return;
    }

    //delete annonce service
    try{
        $rep = $bdd->prepare('DELETE FROM annonce_service WHERE nom = :nom AND prenom = :prenom');

        $rep->execute(array(
            'nom' => $nom,
            'prenom' => $prenom
        ));


        $rep->closeCursor();
    }
    catch(PDOException $e)
    {
        echo 'false';
        return;
    }

    //delete le compte
    try {
        $rep = $bdd->prepare('DELETE FROM compte_utilisateur WHERE nom = :nom AND prenom = :prenom AND adresse_mail = :adresse_mail');

        $rep->execute(array(
            'nom' => $nom,
            'prenom' => $prenom,
            'adresse_mail' => $adresse_mail
        ));

        echo 'true';
        $rep->closeCursor();

    }
    catch (PDOException $e)
    {
        echo 'false';
    }
}

delete();