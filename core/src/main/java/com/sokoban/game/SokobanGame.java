/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game;

import com.sokoban.game.usuarios.Usuario;
import com.badlogic.gdx.Game;
import com.sokoban.game.screens.PantallaInicio;

public class SokobanGame extends Game {

    public static final int ANCHO_UI = 650;
    public static final int ALTO_UI = 550;
    public static final int ANCHO_GAME = 1000;
    public static final int ALTO_GAME = 628;
    private Usuario usuarioActual;

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario u) {
        this.usuarioActual = u;
    }

   @Override
public void create() {
    setScreen(new PantallaInicio(this));
    GestorMusica.iniciar(0.8f); 
}

@Override
public void dispose() {
    GestorMusica.dispose();
}
}
