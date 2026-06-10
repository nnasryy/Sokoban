/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.niveles;

import com.sokoban.game.logica.Tablero;

public class GestorNiveles {

    public static final int TOTAL_NIVELES = 5;

    public static Tablero cargarNivel(int numeroNivel) {
        if (numeroNivel < 0 || numeroNivel >= TOTAL_NIVELES) {
            return new Tablero(0);
        }
        return new Tablero(numeroNivel);
    }

    public static boolean esUltimoNivel(int numeroNivel) {
        return numeroNivel == TOTAL_NIVELES - 1;
    }
}
