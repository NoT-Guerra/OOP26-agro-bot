package it.unibo.agrobot.model;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    /**
     * verifica la creazione di una griglia e lo stato iniziale delle celle
     */
    @Test
    void testGridCreation() {
        // crea una griglia 10x5
        Grid grid = new GridImpl(10, 5);

        // dimensioni corrette
        assertEquals(10, grid.getWidth());
        assertEquals(5, grid.getHeight());

        // di default ogni tile deve essere di tipo SOIL e stato UNPLOWED
        Optional<Tile> tileOpt = grid.getTile(0, 0);
        assertTrue(tileOpt.isPresent());
        assertEquals(TileType.SOIL, tileOpt.get().getType());
        assertEquals(SoilState.UNPLOWED, tileOpt.get().getSoilState());
    }

    /**
     * verifica che dimensioni non valide (<= 0) lancino eccezione.
     */
    @Test
    void testInvalidDimensions() {
        // larghezza 0 non ammessa
        assertThrows(IllegalArgumentException.class, () -> new GridImpl(0, 5));
        // altezza negativa non ammessa
        assertThrows(IllegalArgumentException.class, () -> new GridImpl(10, -1));
    }

    /**
     * test metodo isInBounds con coordinate intere, Position e null.
     */
    @Test
    void testBoundsChecking() {
        // griglia 5x5, coordinate valide da (0,0) a (4,4)
        Grid grid = new GridImpl(5, 5);

        // limiti validi
        assertTrue(grid.isInBounds(0, 0));
        assertTrue(grid.isInBounds(4, 4));
        assertTrue(grid.isInBounds(new Position(2.0, 3.0)));

        // limiti non validi
        assertFalse(grid.isInBounds(-1, 0));    // x negativo
        assertFalse(grid.isInBounds(0, 5));     // y fuori
        assertFalse(grid.isInBounds(5, 0));     // x fuori
        assertFalse(grid.isInBounds(new Position(5.0, 5.0))); // entrambe fuori
        assertFalse(grid.isInBounds(null));     // posizione nulla
    }
}