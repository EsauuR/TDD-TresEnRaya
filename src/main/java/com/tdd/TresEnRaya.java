package com.tdd;

public class TresEnRaya {

    private static final int TAMANO_TABLERO = 3;
    private static final char CASILLA_VACIA = '-';

    private final char[][] tablero;
    private char turno;

    public TresEnRaya() {
        tablero = new char[TAMANO_TABLERO][TAMANO_TABLERO];
        turno = 'X';

        for (int fila = 0; fila < TAMANO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMANO_TABLERO; columna++) {
                tablero[fila][columna] = CASILLA_VACIA;
            }
        }
    }

    public void jugar(int fila, int columna) {
        validarPosicion(fila, columna);

        if (tablero[fila][columna] != CASILLA_VACIA) {
            throw new IllegalArgumentException("La casilla ya está ocupada");
        }

        tablero[fila][columna] = turno;
        turno = turno == 'X' ? 'O' : 'X';
    }

    public char getCasilla(int fila, int columna) {
        validarPosicion(fila, columna);
        return tablero[fila][columna];
    }

    public char[][] getTablero() {
        char[][] copia = new char[TAMANO_TABLERO][TAMANO_TABLERO];
        for (int fila = 0; fila < TAMANO_TABLERO; fila++) {
            System.arraycopy(tablero[fila], 0, copia[fila], 0, TAMANO_TABLERO);
        }
        return copia;
    }

    public boolean esGanador(char jugador) {
        for (int indice = 0; indice < TAMANO_TABLERO; indice++) {
            if (tablero[indice][0] == jugador
                    && tablero[indice][1] == jugador
                    && tablero[indice][2] == jugador) {
                return true;
            }

            if (tablero[0][indice] == jugador
                    && tablero[1][indice] == jugador
                    && tablero[2][indice] == jugador) {
                return true;
            }
        }

        return (tablero[0][0] == jugador
                && tablero[1][1] == jugador
                && tablero[2][2] == jugador)
                || (tablero[0][2] == jugador
                && tablero[1][1] == jugador
                && tablero[2][0] == jugador);
    }

    public boolean hayGanador() {
        return esGanador('X') || esGanador('O');
    }

    public boolean esEmpate() {
        if (hayGanador()) {
            return false;
        }

        for (int fila = 0; fila < TAMANO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMANO_TABLERO; columna++) {
                if (tablero[fila][columna] == CASILLA_VACIA) {
                    return false;
                }
            }
        }

        return true;
    }

    private void validarPosicion(int fila, int columna) {
        if (fila < 0 || fila >= TAMANO_TABLERO
                || columna < 0 || columna >= TAMANO_TABLERO) {
            throw new IllegalArgumentException("La posición está fuera de rango");
        }
    }
}
