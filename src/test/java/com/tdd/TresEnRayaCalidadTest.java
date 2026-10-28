package com.tdd;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Comportamiento del juego Tres en Raya")
class TresEnRayaCalidadTest {

    private TresEnRaya juego;

    @BeforeEach
    @DisplayName("Dado un juego nuevo")
    void setUp() {
        juego = new TresEnRaya();
    }

    @Test
    @DisplayName("inicializa todas las casillas vacías")
    void tableroNuevoDebeEstarVacio() {
        char[][] tableroEsperado = {
                {'-', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '-'}
        };

        assertArrayEquals(tableroEsperado, juego.getTablero());
        assertFalse(juego.hayGanador());
        assertFalse(juego.esEmpate());
    }

    @Test
    @DisplayName("el primer turno es X y los turnos se alternan")
    void debeAlternarLosTurnosDespuesDeCadaJugadaValida() {
        juego.jugar(0, 0);
        juego.jugar(2, 2);
        juego.jugar(1, 1);
        juego.jugar(0, 2);

        assertAll(
                () -> assertEquals('X', juego.getCasilla(0, 0)),
                () -> assertEquals('O', juego.getCasilla(2, 2)),
                () -> assertEquals('X', juego.getCasilla(1, 1)),
                () -> assertEquals('O', juego.getCasilla(0, 2))
        );
    }

    @ParameterizedTest(name = "posición [{0}, {1}]")
    @MethodSource("posicionesValidas")
    @DisplayName("acepta las posiciones de los extremos del tablero")
    void debeAceptarPosicionesValidas(int fila, int columna) {
        assertDoesNotThrow(() -> juego.jugar(fila, columna));
        assertEquals('X', juego.getCasilla(fila, columna));
    }

    @ParameterizedTest(name = "posición [{0}, {1}]")
    @MethodSource("posicionesFueraDeRango")
    @DisplayName("rechaza posiciones fuera de los límites del tablero")
    void debeRechazarPosicionesFueraDeRango(int fila, int columna) {
        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> juego.jugar(fila, columna)
        );

        assertTrue(excepcion.getMessage().contains("fuera de rango"));
    }

    @Test
    @DisplayName("rechaza una casilla ocupada sin cambiar el turno")
    void noDebePermitirJugarEnUnaCasillaOcupada() {
        juego.jugar(1, 1);

        assertThrows(IllegalArgumentException.class, () -> juego.jugar(1, 1));
        juego.jugar(0, 0);

        assertAll(
                () -> assertEquals('X', juego.getCasilla(1, 1)),
                () -> assertEquals('O', juego.getCasilla(0, 0))
        );
    }

    @Test
    @DisplayName("rechaza posiciones inválidas sin modificar el tablero")
    void movimientoInvalidoNoDebeModificarElTablero() {
        char[][] tableroInicial = juego.getTablero();

        assertThrows(IllegalArgumentException.class, () -> juego.jugar(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> juego.jugar(0, 3));

        assertArrayEquals(tableroInicial, juego.getTablero());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("secuenciasGanadorasParaX")
    @DisplayName("detecta una victoria en cada fila, columna o diagonal")
    void debeDetectarTodasLasLineasGanadoras(String descripcion, int[][] jugadas) {
        for (int[] jugada : jugadas) {
            juego.jugar(jugada[0], jugada[1]);
        }

        assertAll(
                () -> assertTrue(juego.esGanador('X')),
                () -> assertTrue(juego.hayGanador()),
                () -> assertFalse(juego.esEmpate())
        );
    }

    @Test
    @DisplayName("detecta una victoria de O")
    void debeDetectarVictoriaDelJugadorO() {
        juego.jugar(0, 0);
        juego.jugar(1, 0);
        juego.jugar(0, 1);
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.jugar(1, 2);

        assertTrue(juego.esGanador('O'));
        assertTrue(juego.hayGanador());
    }

    @Test
    @DisplayName("un tablero lleno sin línea ganadora es un empate")
    void debeDetectarEmpate() {
        int[][] jugadas = {
                {0, 0}, {0, 1}, {0, 2},
                {1, 1}, {1, 0}, {2, 0},
                {2, 1}, {1, 2}, {2, 2}
        };

        for (int[] jugada : jugadas) {
            juego.jugar(jugada[0], jugada[1]);
        }

        assertAll(
                () -> assertTrue(juego.esEmpate()),
                () -> assertFalse(juego.hayGanador()),
                () -> assertFalse(juego.esGanador('X')),
                () -> assertFalse(juego.esGanador('O'))
        );
    }

    @Test
    @DisplayName("un tablero incompleto nunca es empate")
    void tableroIncompletoNoDebeSerEmpate() {
        juego.jugar(0, 0);
        juego.jugar(1, 1);

        assertFalse(juego.esEmpate());
    }

    private static Stream<Arguments> posicionesValidas() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(0, 2),
                Arguments.of(2, 0),
                Arguments.of(2, 2)
        );
    }

    private static Stream<Arguments> posicionesFueraDeRango() {
        return Stream.of(
                Arguments.of(-1, 0),
                Arguments.of(0, -1),
                Arguments.of(3, 0),
                Arguments.of(0, 3),
                Arguments.of(-1, 3),
                Arguments.of(3, -1)
        );
    }

    private static Stream<Arguments> secuenciasGanadorasParaX() {
        return Stream.of(
                Arguments.of(
                        "fila superior",
                        new int[][] {{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}}
                ),
                Arguments.of(
                        "fila central",
                        new int[][] {{1, 0}, {0, 0}, {1, 1}, {0, 1}, {1, 2}}
                ),
                Arguments.of(
                        "fila inferior",
                        new int[][] {{2, 0}, {0, 0}, {2, 1}, {0, 1}, {2, 2}}
                ),
                Arguments.of(
                        "columna izquierda",
                        new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 0}}
                ),
                Arguments.of(
                        "columna central",
                        new int[][] {{0, 1}, {0, 0}, {1, 1}, {1, 0}, {2, 1}}
                ),
                Arguments.of(
                        "columna derecha",
                        new int[][] {{0, 2}, {0, 0}, {1, 2}, {1, 0}, {2, 2}}
                ),
                Arguments.of(
                        "diagonal principal",
                        new int[][] {{0, 0}, {0, 1}, {1, 1}, {0, 2}, {2, 2}}
                ),
                Arguments.of(
                        "diagonal secundaria",
                        new int[][] {{0, 2}, {0, 0}, {1, 1}, {1, 0}, {2, 0}}
                )
        );
    }
}
