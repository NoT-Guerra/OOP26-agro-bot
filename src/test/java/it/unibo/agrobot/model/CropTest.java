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
        Crop wheat = new Wheat(); // 20.0s per la crescita, 15.0s per l'irrigazione
        
        assertEquals(CropState.SEED, wheat.getState()); // inizialmente è uno stato di seme
        
        // avanzo il tempo di 11 secondi e verifico che la pianta sia ancora in crescita
        wheat.update(11.0);
        assertEquals(CropState.GROWING, wheat.getState());
        
        // annaffio la pianta in modo che non muoia
        wheat.water();
        
        // avanzo il tempo di altri 10 secondi → totale 21s > 20s di crescita
        wheat.update(10.0);
        assertEquals(CropState.MATURE, wheat.getState());
        assertTrue(wheat.isReadyToHarvest());
    }
    
    /**
     * testo la crescita del mais e verifico che muoia se non viene annaffiato entro il tempo prestabilito
     */
    @Test
    void testCornDehydration() {
        Crop corn = new Corn(); // 35.0s per la crescita, 10.0s per l'irrigazione
        
        assertEquals(CropState.SEED, corn.getState());
        
        // avanzo il tempo di 41 secondi per verificare che la pianta sia morta
        corn.update(41.0);
        
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
        
        // avanzo il tempo di 10 secondi per far maturare la pianta
        wheat.update(10.0);
        tile.irrigate(); // annaffio la pianta
        wheat.update(11.0);
        
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
