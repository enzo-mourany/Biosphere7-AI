package biosphere7;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests sur la classe Utils.
 */
public class UtilsTest {

    /**
     * Test de la fonction caseDepuisCodage.
     */
    @Test
    public void testCaseDepuisCodage() {
        Case laCase;
        // case vide
        laCase = Utils.caseDepuisCodage("---", "   ");
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_VIDE, laCase.espece);
        // un pommier possédé par les rouges
        laCase = Utils.caseDepuisCodage("---", "PR4");
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_POMMIER, laCase.espece);
        assertEquals(Case.CAR_ROUGE, laCase.couleur);
        assertEquals(4, laCase.vitalite);
        // un pommier possédé par les bleus
        laCase = Utils.caseDepuisCodage("---", "PB9");
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_POMMIER, laCase.espece);
        assertEquals(Case.CAR_BLEU, laCase.couleur);
        assertEquals(9, laCase.vitalite);
    }

    /**
     * Test de la fonction plateauDepuisTexte().
     */
    @Test
    public void testPlateauDepuisTexte() {
        Case[][] plateau = Utils.plateauDepuisTexte(PLATEAU1);
        Case laCase;
        // une case avec un pommier bleu
        laCase = plateau[0][0];
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_POMMIER, laCase.espece);
        assertEquals(Case.CAR_BLEU, laCase.couleur);
        assertEquals(4, laCase.vitalite);
        // une case avec un pommier rouge
        laCase = plateau[13][13];
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_POMMIER, laCase.espece);
        assertEquals(Case.CAR_ROUGE, laCase.couleur);
        assertEquals(1, laCase.vitalite);
        // une case vide
        laCase = plateau[0][1];
        assertEquals(Case.CAR_TERRE, laCase.nature);
        assertEquals(Case.CAR_VIDE, laCase.espece);
    }

    final String PLATEAU1
            = "   A   B   C   D   E   F   G   H   I   J   K   L   M   N \n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "a|PB4|   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "b|PR9|   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "c|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "d|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "e|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "f|   |PR3|   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "g|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "h|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "i|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "j|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "k|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "l|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "m|   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n"
            + "n|   |   |   |   |   |   |   |   |   |   |   |   |   |PR1|\n"
            + " +---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";
}
