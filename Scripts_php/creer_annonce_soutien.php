<?php
// inserer un nouveau profil utilisateur dans la BDD
function inserer ()
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
        $rep = $bdd->prepare('INSERT INTO annonce_soutien(prenom,nom,nbswap,semestre,ue_majeur,matiere,date_limite,description) VALUES(:prenom,:nom,:nbswap,:semestre,:ue_majeur,:matiere,:date_limite,:description)');

        $prenom = $_GET['prenom'];
        $nom = $_GET['nom'];
        $nbswap = $_GET['nbswap'];
        $semestre = $_GET['semestre'];
        $ue_majeur = $_GET['ue_majeur'];
        $matiere = $_GET['matiere'];
        $date_limite = $_GET['date_limite'];
        $description = $_GET['description'];



        $rep->execute(array(
            'prenom' => $prenom,
            'nom' => $nom,
            'nbswap' => $nbswap,
            'semestre' => $semestre,
            'ue_majeur' => $ue_majeur,
            'matiere' => $matiere,
            'date_limite' => $date_limite,
            'description' => $description
        ));
        echo 'true';

        $rep->closeCursor();

    }

    catch(PDOException $e){
        echo'false';
    }
}

inserer();
