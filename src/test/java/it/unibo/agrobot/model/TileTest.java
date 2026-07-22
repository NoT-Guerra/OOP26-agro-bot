package it.unibo.agrobot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void testTileCreation() {
    // creo una posizione con coordinate 1.0, 2.0
    Position pos = new Position(1.0, 2.0);
    // crea una zolla di terreno nella relativa posizione
    Tile tile = new TileImpl(pos, TileType.SOIL);

    // controlla che la posizione memorizzata nella tile sia uguale a quella creata
    assertEquals(pos, tile.getPosition());
    // verifica che il tipo di tile sia effettivamente di tipo SOIL
    assertEquals(TileType.SOIL, tile.getType());
    // verifica che lo stato iniziale del terreno sia non arato
    assertEquals(SoilState.UNPLOWED, tile.getSoilState());
    }

    @Test
    void testPlow() {
    Position pos = new Position(0, 0);
    Tile tile = new TileImpl(pos, TileType.SOIL);

    // imposto lo stato iniziale a non arato
    assertEquals(SoilState.UNPLOWED, tile.getSoilState());

    // procedo con aratura
    assertTrue(tile.plow());
    assertEquals(SoilState.PLOWED, tile.getSoilState());  // ora la zolla è arata

    // provo ad arare di nuovo la stessa zolla
    assertFalse(tile.plow()); 
    assertEquals(SoilState.PLOWED, tile.getSoilState());  // lo stato non cambia, rimane arata
    }
}
