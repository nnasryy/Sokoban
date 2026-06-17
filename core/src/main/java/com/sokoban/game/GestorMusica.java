/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GestorMusica {

    private static Music musica;
    private static boolean activa = true;

    public static void iniciar(float volumen) {
        if (musica == null) {
            musica = Gdx.audio.newMusic(
                    Gdx.files.internal("musica/musica_fondo.ogg"));
            musica.setLooping(true);
        }
        musica.setVolume(volumen);
        if (activa) {
            musica.play();
        }
    }

    public static void toggleMusica(ImageButton btnVolumen,
            com.badlogic.gdx.graphics.Texture texOn,
            com.badlogic.gdx.graphics.Texture texOff) {
        activa = !activa;
        if (activa) {
            musica.play();
        } else {
            musica.pause();
        }
        ((com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable) btnVolumen.getStyle().imageUp)
                .setRegion(new com.badlogic.gdx.graphics.g2d.TextureRegion(
                        activa ? texOn : texOff));
    }

    public static boolean isActiva() {
        return activa;
    }

    public static void setVolumen(float v) {
        if (musica != null) {
            musica.setVolume(v);
        }
    }

    public static void dispose() {
        if (musica != null) {
            musica.dispose();
            musica = null;
        }
    }
}
