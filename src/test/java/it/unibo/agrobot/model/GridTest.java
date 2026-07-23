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

    /**
     * verifica il recupero delle tile dalla griglia, sia in posizione valida che fuori dai limiti
     */
    @Test
    void testGetTile() {
        Grid grid = new GridImpl(3, 3);

        // recupero tile in posizione valida (1,1)
        Optional<Tile> validTile = grid.getTile(1, 1);
        assertTrue(validTile.isPresent());
        // la posizione della tile deve corrispondere a quella richiesta con tolleranza per i double
        assertEquals(1.0, validTile.get().getPosition().getX(), 0.001);
        assertEquals(1.0, validTile.get().getPosition().getY(), 0.001);

        // richiesta fuori dai limiti
        Optional<Tile> invalidTile = grid.getTile(3, 3);
        assertFalse(invalidTile.isPresent());

        // richiesta con Position fuori dai limiti
        Optional<Tile> invalidPosition = grid.getTile(new Position(-1, 0));
        assertFalse(invalidPosition.isPresent());
    }

    /**
     * verifica l'inserimento di una tile nella griglia
     */
    @Test
    void testSetTile() {
        Grid grid = new GridImpl(2, 2);
        
        // creo una tile di tipo hangar in posizione (1,1)
        Tile hangar = new TileImpl(new Position(1, 1), TileType.HANGAR);
        assertTrue(grid.setTile(1, 1, hangar));

        Optional<Tile> tileOpt = grid.getTile(1, 1);
        assertTrue(tileOpt.isPresent());
        // verifico che il tipo sia effettivamente HANGAR
        assertEquals(TileType.HANGAR, tileOpt.get().getType());

        // provo a inserire una tile fuori dai limiti
        Tile well = new TileImpl(new Position(2, 2), TileType.WELL);
        assertFalse(grid.setTile(2, 2, well));
    }
}
