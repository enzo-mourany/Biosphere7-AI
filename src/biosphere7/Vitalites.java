package biosphere7;

/**
 * Total des vitalités, pour les plantes de chaque joueur.
 */
public class Vitalites {
    
    /**
     * Somme des vitalités des plantes du joueur rouge.
     */
    int vitalitesRouge;
    
    /**
     * Somme des vitalités des plantes du joueur bleu.
     */
    int vitalitesBleu;
    
    /**
     * Constructeur vide.
     */
    Vitalites() {
        vitalitesRouge = 0;
        vitalitesBleu = 0;
    }
    
    /**
     * Constructeur à partir de valeurs connues.
     * 
     * @param vitaRouge somme des vitalités du joueur rouge
     * @param vitaBleu somme des vitalités du joueur bleu
     */
    Vitalites(int vitaRouge, int vitaBleu) {
        vitalitesRouge = vitaRouge;
        vitalitesBleu = vitaBleu;
    }

    /**
     * Constructeur par copie : permet d'obtenir une copie de cet objet.
     * 
     * @param vitalites l'objet à copier
     */
    Vitalites(Vitalites vitalites) {
        this.vitalitesRouge = vitalites.vitalitesRouge;
        this.vitalitesBleu = vitalites.vitalitesBleu;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.vitalitesRouge;
        hash = 89 * hash + this.vitalitesBleu;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vitalites other = (Vitalites) obj;
        if (this.vitalitesRouge != other.vitalitesRouge) {
            return false;
        }
        if (this.vitalitesBleu != other.vitalitesBleu) {
            return false;
        }
        return true;
    }
}
