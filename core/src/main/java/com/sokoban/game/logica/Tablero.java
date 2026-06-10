/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.logica;

import static com.sokoban.game.logica.Constantes.*;

public class Tablero {

    private int[][] grid;
    private int jugadorX, jugadorY;
    private int movimientos = 0;
    private int cajasEnMeta = 0;
    private int totalMetas;
    private int numeroNivel;

    public Tablero(int numeroNivel) {
        this.numeroNivel = numeroNivel;
        int[][] original = NivelData.NIVELES[numeroNivel];

        // Copia profunda para no modificar el original
        this.grid = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            this.grid[i] = original[i].clone();
        }

        this.totalMetas = NivelData.contarMetas(numeroNivel);
        inicializar();
    }

    private void inicializar() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == JUGADOR) {
                    jugadorX = x;
                    jugadorY = y;
                }
            }
        }
    }

    // dx, dy — dirección del movimiento
    public boolean mover(int dx, int dy) {
        int nx = jugadorX + dx;
        int ny = jugadorY + dy;

        // Verifica límites
        if (ny < 0 || ny >= grid.length ||
            nx < 0 || nx >= grid[ny].length) return false;

        // No puede moverse a pared o vacio
        if (grid[ny][nx] == PARED || grid[ny][nx] == VACIO) return false;

        // Si hay caja intenta empujarla
        if (grid[ny][nx] == CAJA || grid[ny][nx] == CAJA_EN_META) {
            int nx2 = nx + dx;
            int ny2 = ny + dy;

            if (ny2 < 0 || ny2 >= grid.length ||
                nx2 < 0 || nx2 >= grid[ny2].length) return false;

            if (grid[ny2][nx2] == PARED || grid[ny2][nx2] == VACIO ||
                grid[ny2][nx2] == CAJA  || grid[ny2][nx2] == CAJA_EN_META)
                return false;

            empujarCaja(nx, ny, nx2, ny2);
        }

        // Mover jugador
        grid[jugadorY][jugadorX] = 
            (grid[jugadorY][jugadorX] == JUGADOR) ? SUELO : META;
        jugadorX = nx;
        jugadorY = ny;
        grid[jugadorY][jugadorX] = JUGADOR;
        movimientos++;
        return true;
    }

    private void empujarCaja(int cx, int cy, int nx, int ny) {
        // Si la caja estaba en meta, resta
        if (grid[cy][cx] == CAJA_EN_META) cajasEnMeta--;

        // La celda donde estaba la caja vuelve a suelo o meta
        grid[cy][cx] = SUELO;

        // La nueva posición
        if (grid[ny][nx] == META) {
            grid[ny][nx] = CAJA_EN_META;
            cajasEnMeta++;
        } else {
            grid[ny][nx] = CAJA;
        }
    }

    public boolean nivelCompleto() {
        return cajasEnMeta == totalMetas && totalMetas > 0;
    }

    public void reiniciar() {
        int[][] original = NivelData.NIVELES[numeroNivel];
        for (int i = 0; i < original.length; i++) {
            this.grid[i] = original[i].clone();
        }
        movimientos = 0;
        cajasEnMeta = 0;
        inicializar();
    }

    // Getters
    public int[][] getGrid()        { return grid; }
    public int getJugadorX()        { return jugadorX; }
    public int getJugadorY()        { return jugadorY; }
    public int getMovimientos()     { return movimientos; }
    public int getCajasEnMeta()     { return cajasEnMeta; }
    public int getTotalMetas()      { return totalMetas; }
    public int getFilas()           { return grid.length; }
    public int getColumnas()        { return grid[0].length; }
}
