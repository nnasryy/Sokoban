/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.logica;

import static com.sokoban.game.logica.Constantes.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class Tablero {

    private int[][] grid;
    private boolean[][] esMeta;
    private int jugadorX, jugadorY;
    private int movimientos = 0;
    private int cajasEnMeta = 0;
    private int totalMetas;
    private int numeroNivel;
    private Deque<int[][]> historial = new ArrayDeque<>();
    private int[][] gridInicial;

    private static final int[][] PAREDES_POR_NIVEL = {
        {1, 2, 7},
        {1},
        {1, 2},
        {1, 2},
        {1, 2, 7}
    };

    public Tablero(int numeroNivel) {
        this.numeroNivel = numeroNivel;
        int[][] original = NivelData.NIVELES[numeroNivel];

        this.grid = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            this.grid[i] = original[i].clone();
        }

        this.totalMetas = NivelData.contarMetas(numeroNivel);
        inicializar();
    }

    private void inicializar() {
        cajasEnMeta = 0;
        esMeta = new boolean[grid.length][];
        for (int y = 0; y < grid.length; y++) {
            esMeta[y] = new boolean[grid[y].length];
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == JUGADOR) {
                    jugadorX = x;
                    jugadorY = y;
                }
                if (grid[y][x] == CAJA_EN_META) {
                    cajasEnMeta++;
                }
                if (grid[y][x] == META) {
                    esMeta[y][x] = true;
                }
            }
        }
    }

    private boolean esPared(int valor) {
        if (numeroNivel < 0 || numeroNivel >= PAREDES_POR_NIVEL.length) {
            return false;
        }
        for (int pared : PAREDES_POR_NIVEL[numeroNivel]) {
            if (valor == pared) {
                return true;
            }
        }
        return false;
    }

    private boolean esCaminable(int valor) {
        return valor == VACIO || valor == META;
    }

    public boolean mover(int dx, int dy) {
        int nx = jugadorX + dx;
        int ny = jugadorY + dy;
        if (fueraDelGrid(nx, ny)) {
            return false;
        }

        int celdaDestino = grid[ny][nx];
        if (esPared(celdaDestino)) {
            return false;
        }

        if (celdaDestino == CAJA || celdaDestino == CAJA_EN_META) {
            int nx2 = nx + dx;
            int ny2 = ny + dy;
            if (fueraDelGrid(nx2, ny2)) {
                return false;
            }
            int celdaSiguiente = grid[ny2][nx2];
            if (esPared(celdaSiguiente)) {
                return false;
            }
            if (celdaSiguiente == CAJA || celdaSiguiente == CAJA_EN_META) {
                return false;
            }
            if (!esCaminable(celdaSiguiente)) {
                return false;
            }

            historial.push(copiarGrid()); // ← guarda SOLO si el mov es válido
            empujarCaja(nx, ny, nx2, ny2);
        } else if (!esCaminable(celdaDestino)) {
            return false;
        } else {
            historial.push(copiarGrid()); // ← guarda SOLO si el mov es válido
        }

        grid[jugadorY][jugadorX] = esMeta[jugadorY][jugadorX] ? META : VACIO;
        jugadorX = nx;
        jugadorY = ny;
        grid[jugadorY][jugadorX] = JUGADOR;
        movimientos++;
        return true;
    }

    private void empujarCaja(int cx, int cy, int nx, int ny) {
        if (grid[cy][cx] == CAJA_EN_META) {
            cajasEnMeta--;
        }

        grid[cy][cx] = esMeta[cy][cx] ? META : VACIO;

        if (grid[ny][nx] == META) {
            grid[ny][nx] = CAJA_EN_META;
            cajasEnMeta++;
        } else {
            grid[ny][nx] = CAJA;
        }
    }

    private boolean fueraDelGrid(int x, int y) {
        return y < 0 || y >= grid.length
                || x < 0 || x >= grid[y].length;
    }

    public void revert() {
    if (historial.isEmpty()) return;
    
    int[][] estadoAnterior = historial.pop();
    for (int i = 0; i < grid.length; i++) {
        grid[i] = estadoAnterior[i].clone();
    }
    if (movimientos > 0) movimientos--;
    
    // Re-sincroniza toda la lógica con el grid restaurado
    cajasEnMeta = 0;
    esMeta = new boolean[grid.length][];
    for (int y = 0; y < grid.length; y++) {
        esMeta[y] = new boolean[grid[y].length];
        for (int x = 0; x < grid[y].length; x++) {
            if (grid[y][x] == JUGADOR) {
                jugadorX = x;
                jugadorY = y;
            }
            if (grid[y][x] == CAJA_EN_META) {
                cajasEnMeta++;
            }
            if (grid[y][x] == META || grid[y][x] == CAJA_EN_META) {
                esMeta[y][x] = true;
            }
        }
    }
}

    private int[][] copiarGrid() {
        int[][] copia = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copia[i] = grid[i].clone();
        }
        return copia;
    }

    public void reiniciar() {
        historial.clear();
        movimientos = 0;
        cajasEnMeta = 0;
        int[][] original = NivelData.NIVELES[numeroNivel];
        for (int i = 0; i < original.length; i++) {
            grid[i] = original[i].clone();
        }
        inicializar();
    }

    public boolean nivelCompleto() {
        return cajasEnMeta == totalMetas && totalMetas > 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getJugadorX() {
        return jugadorX;
    }

    public int getJugadorY() {
        return jugadorY;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public int getCajasEnMeta() {
        return cajasEnMeta;
    }

    public int getTotalMetas() {
        return totalMetas;
    }

    public int getFilas() {
        return grid.length;
    }

    public int getColumnas() {
        return grid[0].length;
    }
}
