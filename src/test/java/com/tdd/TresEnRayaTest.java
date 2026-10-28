package com.tdd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TresEnRayaTest {

    private TresEnRaya juego;

    @BeforeEach
    void setUp() {
        juego = new TresEnRaya();
    }

    @Test
    void testTableroInicialVacio() {
        char[][] tableroEsperado = {
                {'-', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '-'}
        };

        assertArrayEquals(tableroEsperado, juego.getTablero());
    }

    @Test
    void testPrimerMovimientoEsX() {
        juego.jugar(0, 0);

        assertEquals('X', juego.getTablero()[0][0]);
    }

    @Test
    void testAlternanciaDeTurnos() {
        juego.jugar(0, 0);
        juego.jugar(1, 1);

        assertEquals('O', juego.getTablero()[1][1]);
    }

    @Test
    void testCasillaOcupadaLanzaExcepcion() {
        juego.jugar(0, 0);

        assertThrows(IllegalArgumentException.class, () -> juego.jugar(0, 0));
    }

    @Test
    void testMovimientoFueraDeRangoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> juego.jugar(3, 0));
        assertThrows(IllegalArgumentException.class, () -> juego.jugar(0, -1));
    }

    @Test
    void testGanadorPorFila() {
        juego.jugar(0, 0);
        juego.jugar(1, 0);
        juego.jugar(0, 1);
        juego.jugar(1, 1);
        juego.jugar(0, 2);

        assertTrue(juego.hayGanador());
    }

    @Test
    void testGanadorPorColumna() {
        juego.jugar(0, 0);
        juego.jugar(0, 1);
        juego.jugar(1, 0);
        juego.jugar(1, 1);
        juego.jugar(2, 0);

        assertTrue(juego.hayGanador());
    }

    @Test
    void testGanadorPorDiagonal() {
        juego.jugar(0, 0);
        juego.jugar(0, 1);
        juego.jugar(1, 1);
        juego.jugar(0, 2);
        juego.jugar(2, 2);

        assertTrue(juego.hayGanador());
    }

    @Test
    void testEmpate() {
        juego.jugar(0, 0);
        juego.jugar(0, 1);
        juego.jugar(0, 2);
        juego.jugar(1, 1);
        juego.jugar(1, 0);
        juego.jugar(2, 0);
        juego.jugar(2, 1);
        juego.jugar(1, 2);
        juego.jugar(2, 2);

        assertTrue(juego.esEmpate());
    }
}
