package biosphere7;

/**
 * Case du plateau.
 * 
 * VOUS NE DEVEZ PAS MODIFIER CE FICHIER.
 */
public final class Case {

    /**
     * Caractère pour indiquer une case sans plante (dans l'attribut espece de
     * Case).
     */
    public final static char CAR_VIDE = ' ';

    /**
     * Caractère pour indiquer une case avec un pommier (dans l'attribut espece
     * de Case).
     */
    public final static char CAR_POMMIER = 'P';

    /**
     * Caractère pour indiquer une plante possédée par les rouges (dans 
     * l'attribut couleur de Case).
     */
    public final static char CAR_ROUGE = 'R';

    /**
     * Caractère pour indiquer une plante possédée par les bleus (dans 
     * l'attribut couleur de Case).
     */
    public final static char CAR_BLEU = 'B';

    /**
     * Caractère indiquant la nature "Terre" de la case (dans l'attribut nature
     * de Case).
     */
    public final static char CAR_TERRE = 'T';

    /**
     * Indique l'espèce de la plante se trouvant sur cette case. 
     * L'absence de plante est indiquée par le caractère Utils.CAR_VIDE.
     */
    char espece;
    
    /**
     * Indique la couleur du propriétaire de la plante sur cette case (s'il y en
     * a une).
     * Convention : 'R' pour rouge, 'B' pour bleu.
     */
    char couleur;
    
    /**
     * Vitalité de la plante, le cas échéant.
     */
    int vitalite;
    
    /**
     * Nature d'une case.
     */
    char nature;

    /**
     * Constructeur d'une case.
     * 
     * @param uneEspece espèce de la plante sur la case
     * @param uneCouleur couleur du propriétaire de la plante sur cette case
     * @param uneVitalite vitalité de la plante sur cette case
     * @param uneNature nature de la case
     */
    public Case(char uneEspece, char uneCouleur, int uneVitalite,
            char uneNature) {
        this.espece = uneEspece;
        this.couleur = uneCouleur;
        this.vitalite = uneVitalite;
        this.nature = uneNature;
    }

    /**
     * Indique si une plante est présente sur cette case (c'est-à-dire si elle
     * n'est pas vide).
     * 
     * @return vrai ssi une plante est présente
     */
    boolean plantePresente() {
        return this.espece != CAR_VIDE;
    }
}
