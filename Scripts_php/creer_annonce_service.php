<?php
// inserer un nouveau profil utilisateur dans la BDD
function inserer ()
{
    try {
//connexion BDD, ligne obligatoire
        $bdd = new PDO('mysql:host=91.121.116.121;dbname=swapit;charset=utf8;port=3308', 'swapit', '4B2Q4fps',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
    }
    catch(PDOException $e) {

        echo'false';
        return;
    }



    try {
        $rep = $bdd->prepare('INSERT INTO annonce_service(prenom,nom,categorie,nb_swap,sous_categorie,date,description) VALUES(:prenom,:nom,:categorie,:nb_swap,:sous_categorie,:date,:description)');

        $prenom = $_GET['prenom'];
        $nom = $_GET['nom'];
        $nb_swap = $_GET['nb_swap'];
        $categorie = $_GET['categorie'];
        $sous_categorie = $_GET['sous_categorie'];
        $date = $_GET['date'];
        $description = $_GET['description'];



        $rep->execute(array(
            'prenom' => $prenom,
            'nom' => $nom,
            'nb_swap' => $nb_swap,
            'categorie' => $categorie,
            'sous_categorie' => $sous_categorie,
            'date' => $date,
            'description' => $description
        ));
        echo 'true';

        $rep->closeCursor();
    }

    catch(PDOException $e){
        echo 'false';
    }
}

inserer();
