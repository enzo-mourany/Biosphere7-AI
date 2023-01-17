package biosphere7;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.lang.Math;

/**
 * Joueur implémentant les actions possibles à partir d'un plateau, pour un
 * niveau donné.
 */
public class JoueurBiosphere7 implements IJoueurBiosphere7 {

    Plante pommier = new Plante(Categorie.ARBRE, "Pommier");
    Plante sureau = new Plante(Categorie.ARBRE, "Sureau");
    Plante framboisier = new Plante(Categorie.ARBUSTE, "Boisier");
    Plante pommeDeTerre = new Plante(Categorie.LEGUME, "DeTerre");
    Plante tomate = new Plante(Categorie.LEGUME, "Tomate");
    Plante haricot = new Plante(Categorie.LEGUME, "Haricot");

    Plante[] plantes = {pommier, sureau, framboisier, pommeDeTerre, tomate, haricot};

    /**
     * Cette méthode renvoie, pour un plateau donné et un joueur donné, toutes
     * les actions possibles pour ce joueur.
     *
     * @param plateau le plateau considéré
     * @param couleurJoueur couleur du joueur
     * @param niveau le niveau de la partie à jouer
     * @return l'ensemble des actions possibles
     */
    @Override
    public String[] actionsPossibles(Case[][] plateau, char couleurJoueur, int niveau) {
        // afficher l'heure de lancement
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        System.out.println("actionsPossibles : lancement le " + format.format(new Date()));
        // se préparer à stocker les actions possibles
        ActionsPossibles actions = new ActionsPossibles();
        // calculer les vitalités sur le plateau initial
        Vitalites vitalites = vitalitesPlateau(plateau);
        // ajout des actions "planter pommier"
        for (int lig = 0; lig < Coordonnees.NB_LIGNES; lig++) {
            for (int col = 0; col < Coordonnees.NB_COLONNES; col++) {
                Coordonnees coord = new Coordonnees(lig, col);
                // peut planter une plante s'il n'y a aucune plante deja planté et s'il n'est pas entre 4 plantes
                // peut couper une plante
                // peut fertiliser une plante
                if (!plateau[lig][col].plantePresente() && niveau != 3) {
                    if (nombreVoisines(coord, plateau) < 4) {
                        ajoutActionPlante(coord, actions, vitalites, couleurJoueur, plateau);
                    }
                } else if (plateau[lig][col].plantePresente() && niveau >= 3 && niveau != 5 && niveau != 4) {
                    ajoutActionCouperPlante(coord, actions, vitalites, plateau);
                    ajoutActionFertiliserPlante(coord, actions, vitalites, plateau);
                    ajoutActionDisseminerPlante(coord, actions, vitalites, couleurJoueur, plateau);
                }
            }
        }
        ajoutActionOmbre(actions, vitalites, plateau);
        System.out.println("actionsPossibles : fin");
        return actions.nettoyer();
    }

    /**
     * Renvoie les coordonnées de la case suivante, en suivant une direction
     * donnée.
     *
     * @param d la direction à suivre
     * @return les coordonnées de la case suivante
     */
    static Coordonnees suivante(Coordonnees c, Direction d) {
        return new Coordonnees(c.ligne + Direction.mvtVertic(d),
                c.colonne + Direction.mvtHoriz(d));
    }

    /**
     * Somme des vitalités des plantes de chaque joueur sur le plateau.
     *
     * @param plateau le plateau
     * @return la somme des vitalités des plantes de chaque joueur
     */
    static Vitalites vitalitesPlateau(Case[][] plateau) {
        int vitaliteRouge = 0;
        int vitaliteBleu = 0;
        for (int lig = 0; lig < Coordonnees.NB_LIGNES; lig++) {
            for (int col = 0; col < Coordonnees.NB_COLONNES; col++) {
                if (plateau[lig][col].plantePresente() && plateau[lig][col].couleur == 'B') {
                    vitaliteBleu += plateau[lig][col].vitalite;
                } else if (plateau[lig][col].plantePresente() && plateau[lig][col].couleur == 'R') {
                    vitaliteRouge += plateau[lig][col].vitalite;
                }
            }
        }
        return new Vitalites(vitaliteRouge, vitaliteBleu);
    }

    /**
     * Ajout d'une action de plantation d'une plante dans l'ensemble des actions
     * possibles.
     *
     * @param coord coordonnées de la case où planter la plante
     * @param actions l'ensemble des actions possibles (en construction)
     * @param vitalites la somme des vitalités sur le plateau avant de jouer l'action
     * @param couleur la couleur du pommier à ajouterPR1
     */
    void ajoutActionPlante(Coordonnees coord, ActionsPossibles actions,
                           Vitalites vitalites, char couleur, Case[][] plateau) {
        int vitRouge = vitalites.vitalitesRouge;
        int vitBleu = vitalites.vitalitesBleu;
        if (nombreVoisinesMemeCouleur(coord, plateau, couleur, false) < 4) {
            if (couleur == Case.CAR_BLEU) {
                vitBleu += 1 + nombreVoisinesMemeCouleur(coord, plateau, couleur, false);
            } else {
                vitRouge += 1 + nombreVoisinesMemeCouleur(coord, plateau, couleur, false);
            }
        }
        // Vérifie si la plante plantée ettouffe une autre plante
        for (Coordonnees c : plantesVoisines(coord, plateau)) {
            if (planteEttoufe(c, plateau)) {
                if (plateau[c.ligne][c.colonne].couleur == Case.CAR_BLEU) {
                    vitBleu -= plateau[c.ligne][c.colonne].vitalite;
                } else {
                    vitRouge -= plateau[c.ligne][c.colonne].vitalite;
                }
            }
        }
        for (Plante plante : plantes) {
            String action = String.valueOf(plante.initiale()) + coord.carLigne() + coord.carColonne() + ","
                    + (vitRouge) + ","
                    + (vitBleu);
            actions.ajouterAction(action);
        }
    }

    /**
     * Vérifie si une plante se fait ettoufer
     *
     * @param coord coordonnées de la case où se trouve la plante
     * @param plateau le plateau de jeu
     * @return vrai si la plante se fait ettoufer
     */
    boolean planteEttoufe(Coordonnees coord, Case[][] plateau) {
        boolean estEttouffe = false;
        if (nombreVoisines(coord, plateau) >= 3) {
            estEttouffe = true;
        }
        return estEttouffe;
    }

    /**
     * Renvoie la liste des coordonnées des plantes voisines d'une case donnée.
     *
     * @param coord coordonnées de la case dont on veut les voisines
     * @param plateau le plateau de jeu
     * @return la liste des coordonnées des plantes voisines
     */
    Coordonnees[] plantesVoisines(Coordonnees coord, Case[][] plateau) {
        Coordonnees[] voisines = new Coordonnees[4];
        int nbVoisines = 0;
        for (Direction d : Direction.values()) {
            Coordonnees coordVoisin = suivante(coord, d);
            if (coordVoisin.estDansPlateau() && plateau[coordVoisin.ligne][coordVoisin.colonne].plantePresente()) {
                voisines[nbVoisines] = coordVoisin;
                nbVoisines++;
            }
        }
        return Arrays.copyOf(voisines, nbVoisines);
    }

    /**
     * Renvoie le nombre de plantes voisines d'une case donnée
     *
     * @param coord coordonnées de la case considérée
     * @param plateau le plateau
     * @return le nombre de plantes voisines de la case
     */
    int nombreVoisines(Coordonnees coord, Case[][] plateau) {
        return plantesVoisines(coord, plateau).length;
    }

    /**
     * Renvoie le nombre de voisines de memes couleurs d'une case donnée
     *
     * @param coord coordonnées de la case considérée
     * @param plateau le plateau
     * @param couleur la couleur des voisines à compter
     * @param actionCouper vrai si la fonction est appelé pour l'action couper
     * @return le nombre de voisines de même couleur
     */
    int nombreVoisinesMemeCouleur(Coordonnees coord, Case[][] plateau, char couleur, boolean actionCouper) {
        int nbVoisines = 0;
        for (Direction d : Direction.values()) {
            Coordonnees c = suivante(coord, d);
            if (c.estDansPlateau() && plateau[c.ligne][c.colonne].plantePresente()) {
                if (plateau[c.ligne][c.colonne].couleur == couleur
                        && !c.equals(coord)
                        && (actionCouper ? plateau[c.ligne][c.colonne].vitalite < 9 : plateau[c.ligne][c.colonne].vitalite != 0)) {
                    nbVoisines++;
                }
            }
        }
        return nbVoisines;
    }

    /**
     * Calcul le nombre de voisines de la même espèce et même couleur que la plante donnée
     *
     * @param coord coordonnées de la case considérée
     * @param plateau le plateau
     * @return le nombre de voisines de même espèce et même couleur
     */
    int nombreVoisinesMemeEspece(Coordonnees coord, Case[][] plateau) {
        int nbVoisines = 0;
        for (Direction d : Direction.values()) {
            Coordonnees c = suivante(coord, d);
            if (c.estDansPlateau() && plateau[c.ligne][c.colonne].plantePresente()
                    && plateau[c.ligne][c.colonne].espece == plateau[coord.ligne][coord.colonne].espece) {
                nbVoisines++;
            }
        }
        return nbVoisines;
    }
    
    /**
     * Ajout d'une action de coupe de plante dans l'ensemble des actions
     * possibles.
     *
     * @param coord coordonnées de la case où couper la plante
     * @param actions l'ensemble des actions possibles (en construction)
     * @param vitalites la somme des vitalités sur le plateau avant de jouer l'action
     * @param plateau le plateau de jeu
     */
    void ajoutActionCouperPlante(Coordonnees coord, ActionsPossibles actions,
                                 Vitalites vitalites, Case[][] plateau) {
        int vitRouge = vitalites.vitalitesRouge;
        int vitBleu = vitalites.vitalitesBleu;
        if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
            vitBleu -= plateau[coord.ligne][coord.colonne].vitalite;
        } else {
            vitRouge -= plateau[coord.ligne][coord.colonne].vitalite;
        }
        vitBleu += nombreVoisinesMemeCouleur(coord, plateau, 'B', true);
        vitRouge += nombreVoisinesMemeCouleur(coord, plateau, 'R', true);
        String action = "C" + coord.carLigne() + coord.carColonne() + ","
                + (vitRouge) + ","
                + (vitBleu);
        actions.ajouterAction(action);
    }

    /**
     * Ajout d'une action de fertilisation d'une plante dans l'ensemble des actions
     * possibles.
     *
     * @param coord coordonnées de la case où fertiliser la plante
     * @param actions l'ensemble des actions possibles (en construction)
     * @param vitalites la somme des vitalités sur le plateau avant de jouer l'action
     * @param plateau le plateau de jeu
     */
    void ajoutActionFertiliserPlante(Coordonnees coord, ActionsPossibles actions,
                                 Vitalites vitalites, Case[][] plateau) {
        int vitRouge = vitalites.vitalitesRouge;
        int vitBleu = vitalites.vitalitesBleu;
        switch (plateau[coord.ligne][coord.colonne].espece) {
            case 'P':
            case 'S':
                if (plateau[coord.ligne][coord.colonne].vitalite < 9) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu++;
                    } else {
                        vitRouge++;
                    }
                }
                break;
            case 'B':
                if (plateau[coord.ligne][coord.colonne].vitalite < 8) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu += 2;
                    } else {
                        vitRouge += 2;
                    }
                } else if (plateau[coord.ligne][coord.colonne].vitalite == 8) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu += 1;
                    } else {
                        vitRouge += 1;
                    }
                }
                break;
            case 'D':
            case 'T':
            case 'H':
                if (plateau[coord.ligne][coord.colonne].vitalite < 7) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu += 3;
                    } else {
                        vitRouge += 3;
                    }
                } else if (plateau[coord.ligne][coord.colonne].vitalite == 7) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu += 2;
                    } else {
                        vitRouge += 2;
                    }
                } else if (plateau[coord.ligne][coord.colonne].vitalite == 8) {
                    if (plateau[coord.ligne][coord.colonne].couleur == Case.CAR_BLEU) {
                        vitBleu += 1;
                    } else {
                        vitRouge += 1;
                    }
                }
                break;
        }
        String action = "F" + coord.carLigne() + coord.carColonne() + ","
                + (vitRouge) + ","
                + (vitBleu);
        actions.ajouterAction(action);
    }

    /**
     * Vérifie si une plante est autostérile, c'est-à-dire si elle ne peut pas
     * se reproduire seule
     * C'est le cas pour les Tomates et les Haricots
     *
     * @param coord coordonnées de la plante
     * @param plateau le plateau de jeu
     * @return true si la plante est autostérile
     */
    boolean estAutosterile(Coordonnees coord, Case[][] plateau) {
        return plateau[coord.ligne][coord.colonne].espece == 'T' 
                || plateau[coord.ligne][coord.colonne].espece == 'H';
    }

    /**
     * Vérifie si deux plantes sont de même espèce
     *
     * @param coord1 coordonnées de la première plante
     * @param coord2 coordonnées de la deuxième plante
     * @param plateau le plateau de jeu
     * @return vrai si les deux plantes sont de même espèce
     */
    boolean estDeMemeEspece(Coordonnees coord1, Coordonnees coord2, Case[][] plateau) {
        return plateau[coord1.ligne][coord1.colonne].espece == plateau[coord2.ligne][coord2.colonne].espece;
    }

    /**
     * Renvoie le nombre cases voisines vides d'une case
     *
     * @param coord coordonnées de la case
     * @param plateau le plateau de jeu
     * @return la liste des coordonnées des cases voisines vides
     */
    int nombreCasesVoisinesVides(Coordonnees coord, Case[][] plateau) {
        int nbCasesVoisinesVides = 0;
        for (Direction d : Direction.values()) {
            Coordonnees c = suivante(coord, d);
            if (c.estDansPlateau() && !plateau[c.ligne][c.colonne].plantePresente()) {
                nbCasesVoisinesVides++;
            }
        }
        return nbCasesVoisinesVides;
    }

    /**
     * Ajout d'une action de dissemination d'une plante dans l'ensemble des actions
     * possibles.
     *
     * @param coord coordonnées de la case
     * @param actions l'ensemble des actions possibles (en construction)
     * @param vitalites la somme des vitalités sur le plateau avant de jouer l'action
     * @param couleur la couleur de la plante
     * @param plateau le plateau de jeu
     */
    void ajoutActionDisseminerPlante(Coordonnees coord, ActionsPossibles actions,
                                 Vitalites vitalites, char couleur, Case[][] plateau) {
        int vitRouge = vitalites.vitalitesRouge;
        int vitBleu = vitalites.vitalitesBleu;
        if (estAutosterile(coord, plateau) && nombreVoisinesMemeEspece(coord, plateau) != 0) {
            // pour les plantes autostériles
            int plusPetiteVatalite = plateau[coord.ligne][coord.colonne].vitalite;
            for (Coordonnees c : plantesVoisines(coord, plateau)) {
                if (estDeMemeEspece(coord, c, plateau) 
                        && plateau[c.ligne][c.colonne].vitalite < plusPetiteVatalite) {
                    plusPetiteVatalite = plateau[c.ligne][c.colonne].vitalite;
                }
            }
            if (couleur == Case.CAR_BLEU) {
                vitBleu += nombreCasesVoisinesVides(coord, plateau) * plusPetiteVatalite;
            } else {
                vitRouge += nombreCasesVoisinesVides(coord, plateau) * plusPetiteVatalite;
            }
            String action = "I" + coord.carLigne() + coord.carColonne() + ","
                    + (vitRouge) + ","
                    + (vitBleu);
            actions.ajouterAction(action);
        } else if (!estAutosterile(coord, plateau)){
            // pour les plantes autofécondes
            if (couleur == Case.CAR_BLEU) {
                vitBleu += nombreCasesVoisinesVides(coord, plateau);
            } else {
                vitRouge += nombreCasesVoisinesVides(coord, plateau);
            }
            String action = "I" + coord.carLigne() + coord.carColonne() + ","
                    + (vitRouge) + ","
                    + (vitBleu);
            actions.ajouterAction(action);
        }
    }
    
    /**
     * Renvoie les coordonnées des plantes présentes au dessus d'une plante donnée
     *
     * @param coord coordonnées de la plante donnée
     * @param plateau le plateau de jeu
     * @return un tableau de Coordonnées
     */
    Coordonnees[] coordonneesPlantesAuDessusDuneCase(Coordonnees coord, Case[][] plateau) {
        Coordonnees[] casesAuDessus = new Coordonnees[coord.ligne];
        int nbPlantesAuDessusDuneCase = 0;
        if (coord.ligne > 0) {
            for (int lig = coord.ligne - 1; lig > 0; lig--) {
                Coordonnees c = new Coordonnees(lig, coord.colonne);
                if (plateau[c.ligne][c.colonne].plantePresente()) {
                    casesAuDessus[nbPlantesAuDessusDuneCase] = c;
                    nbPlantesAuDessusDuneCase++;
                }
            }
        }
        return Arrays.copyOf(casesAuDessus, nbPlantesAuDessusDuneCase);
    }
    
    /**
     * Calcule la distance entre deux plantes
     *
     * @param coord1 coordonnées de la première plante
     * @param coord2 coordonnées de la deuxième plante
     * @return la valeur absolue de la soustrations des deux coordonées (ligne) 
     * des deux plantes
     */
    int distanceEntreDeuxPlantes(Coordonnees coord1, Coordonnees coord2) {
        return Math.abs(coord1.ligne - coord2.ligne);
    }
    
    
    /**
     * Ajout d'une action d'Ombre dans l'ensemble des actions possibles
     *
     * @param actions l'ensemble des actions possibles (en construction)
     * @param vitalites la somme des vitalités sur le plateau avant de jouer l'action
     * @param plateau le plateau de jeu
     */
    void ajoutActionOmbre(ActionsPossibles actions, Vitalites vitalites, Case[][] plateau) {
        int vitRouge = vitalites.vitalitesRouge;
        int vitBleu = vitalites.vitalitesBleu;
        for (int lig = 0; lig < Coordonnees.NB_LIGNES; lig++) {
            for (int col = 0; col < Coordonnees.NB_COLONNES; col++) {
                Coordonnees coord = new Coordonnees(lig, col);
                if (faitDeLOmbre(coord, plateau) && coord.ligne >= 0) {
                    for (Coordonnees c : coordonneesPlantesAuDessusDuneCase(coord, plateau)) {
                        if (((plateau[coord.ligne][coord.colonne].vitalite - distanceEntreDeuxPlantes(coord, c)) / 2) >= 1) {
                            if (plateau[c.ligne][c.colonne].vitalite - ((plateau[coord.ligne][coord.colonne].vitalite
                                    - distanceEntreDeuxPlantes(coord, c)) / 2) > 0
                                    && ((plateau[coord.ligne][coord.colonne].vitalite - distanceEntreDeuxPlantes(coord, c)) / 2) > 0) {
                                if (plateau[c.ligne][c.colonne].couleur == Case.CAR_BLEU) {
                                    vitBleu -= ((plateau[coord.ligne][coord.colonne].vitalite
                                            - distanceEntreDeuxPlantes(coord, c)) / 2);
                                } else {
                                    vitRouge -= ((plateau[coord.ligne][coord.colonne].vitalite
                                            - distanceEntreDeuxPlantes(coord, c)) / 2);
                                }
                            } else if (((plateau[coord.ligne][coord.colonne].vitalite - distanceEntreDeuxPlantes(coord, c)) / 2) > 0){
                                if (plateau[c.ligne][c.colonne].couleur == Case.CAR_BLEU) {
                                    vitBleu -= plateau[c.ligne][c.colonne].vitalite;
                                } else {
                                    vitRouge -= plateau[c.ligne][c.colonne].vitalite;
                                }
                            }
                        }

                    }
                }
            }
        }
        if (vitRouge != vitalites.vitalitesRouge || vitBleu != vitalites.vitalitesBleu) {
            String action = "O" + "," + (vitRouge) + "," + (vitBleu);
            actions.ajouterAction(action); 
        }
    }
}
