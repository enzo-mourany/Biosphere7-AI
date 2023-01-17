package biosphere7;

public class Plante {

    Categorie categorie;
    String nom;

    Plante(Categorie _catagorie, String _nom) {
        this.categorie = _catagorie;
        this.nom = _nom;
    }

    public char initiale() {
        return this.nom.charAt(0);
    }
}
