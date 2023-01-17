package biosphere7;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tetst unitaires de la classe Vitalites.
 */
public class VitalitesTest {
    
    @Test
    public void testVitalites() {
        // constructeur par défaut
        Vitalites vita0 = new Vitalites();
        assertEquals(0, vita0.vitalitesRouge);
        assertEquals(0, vita0.vitalitesBleu);
        // constructeur paramétré
        Vitalites vita1 = new Vitalites(17, 48);
        assertEquals(17, vita1.vitalitesRouge);
        assertEquals(48, vita1.vitalitesBleu);
        // constructeur par copie
        Vitalites nbPv2 = new Vitalites(vita1);
        assertEquals(17, nbPv2.vitalitesRouge);
        assertEquals(48, nbPv2.vitalitesBleu);
    }
}
