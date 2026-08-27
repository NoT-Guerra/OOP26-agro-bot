package it.unibo.agrobot.model;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CropTest {

    /**
     * testo la crescita del grano e verifico che diventi maturo dopo il tempo di crescita prestabilito
     */
    @Test
    void testWheatGrowth() {
        Crop wheat = new Wheat(); // 40.0s per la crescita, 50.0s per l'irrigazione
        
        assertEquals(CropState.SEED, wheat.getState()); // inizialmente è uno stato di seme
        
        // avanzo il tempo di 21 secondi e verifico che la pianta sia ancora in crescita
        wheat.update(21.0);
        assertEquals(CropState.GROWING, wheat.getState());
        
        // annaffio la pianta in modo che non muoia
        wheat.water();
        
        // avanzo il tempo di altri 20 secondi → totale 41s > 40s di crescita
        wheat.update(20.0);
        assertEquals(CropState.MATURE, wheat.getState());
        assertTrue(wheat.isReadyToHarvest());
    }
    
    /**
     * testo la crescita del mais e verifico che muoia se non viene annaffiato entro il tempo prestabilito
     */
    @Test
    void testCornDehydration() {
        Crop corn = new Corn(); // 70.0s per la crescita, 80.0s per l'irrigazione
        
        assertEquals(CropState.SEED, corn.getState());
        
        // avanzo il tempo di 81 secondi per verificare che la pianta sia morta
        corn.update(81.0);
        
        // il mais dovrebbe essere morto per mancanza d'acqua
        assertEquals(CropState.DEAD, corn.getState());
        assertTrue(corn.isDead());
        
        // una pianta morta non cresce più anche se il tempo passa
        corn.update(100.0);
        assertEquals(CropState.DEAD, corn.getState());
    }

    /**
     * testo la semina e il raccolto di una coltura
     */
    @Test
    void testPlantAndHarvest() {
        Tile tile = new TileImpl(new Position(0, 0), TileType.SOIL);
        
        Crop wheat = new Wheat();
        
        // provo a piantare il grano su una zolla non arata, deve fallire
        assertFalse(tile.plant(wheat));
        
        // aro il terreno
        tile.plow();
        
        // semina ora ha successo
        assertTrue(tile.plant(wheat));
        assertTrue(tile.getCrop().isPresent());
        
        // avanzo il tempo di 20 secondi per far maturare la pianta
        wheat.update(20.0);
        tile.irrigate(); // annaffio la pianta
        wheat.update(21.0);
        
        assertTrue(wheat.isReadyToHarvest());
        
        // racclto
        Optional<Crop> harvested = tile.harvest();
        assertTrue(harvested.isPresent());
        assertEquals(wheat, harvested.get());
        
        // la zolla dovrebbe essere vuota e UNPLOWED dopo il raccolto
        assertTrue(tile.getCrop().isEmpty());
        assertEquals(SoilState.UNPLOWED, tile.getSoilState());
    }
}
