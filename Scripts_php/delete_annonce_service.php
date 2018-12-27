<?php
// fonction pour supprimer une ligne
function delete()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    } catch (PDOException $e) {

        echo 'false';
        return;
    }

    try {
        $rep = $bdd->prepare('DELETE FROM annonce_service WHERE nom = :nom AND prenom = :prenom');

        $nom = $_GET['nom'];
        $prenom = $_GET['prenom'];
        $rep->execute(array(
            'nom' => $nom,
            'prenom' => $prenom
        ));

        echo 'true';

        $rep->closeCursor();

    } catch (PDOException $e) {

        echo 'false';
    }
}
delete();
