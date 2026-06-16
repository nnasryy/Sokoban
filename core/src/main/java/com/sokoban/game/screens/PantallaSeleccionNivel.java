/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;

public class PantallaSeleccionNivel extends PantallaBase {

    private Texture texFondo, texExit, texCompetitivo;
    private Texture texNivel1, texNivel2, texNivel3, texNivel4, texCandado;
    private BitmapFont fuenteTitulo;
    private Stage stage;
    private Texture texVolumen;
    private static final float[] XS = {36f, 148.4f, 271.4f, 400.1f, 523.9f};
    private static final float Y_NIVEL = 216.5f;
    private static final float ANCHO_BLOQUE = 90f;
    private static final float ALTO_BLOQUE = 100f;
    private BitmapFont fuenteNumeros;

    public PantallaSeleccionNivel(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        texFondo = new Texture("imagenes/fondos/FondoAzul.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texCompetitivo = new Texture("imagenes/botones/Competitivo.png");
        texCandado = new Texture("imagenes/botones/Candado.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");
        texNivel1 = new Texture("imagenes/botones/Nivel1Bloque.png");
        texNivel2 = new Texture("imagenes/botones/Nivel2Bloque.png");
        texNivel3 = new Texture("imagenes/botones/Nivel3Bloque.png");
        texNivel4 = new Texture("imagenes/botones/Nivel4Bloque.png");

        fuenteTitulo = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);

        fuenteNumeros = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari35.fnt"));
        fuenteNumeros.getData().setScale(1f);

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        int nivelesDesbloqueados = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getNivelesDesbloqueados() : 1;

        Texture[] texturas = {texNivel1, texNivel2, texNivel3, texNivel4, texNivel1};

        for (int i = 0; i < 5; i++) {
            final int numeroNivel = i;

            ImageButton btnNivel = new ImageButton(
                    new TextureRegionDrawable(new TextureRegion(texturas[i])));
            btnNivel.setSize(ANCHO_BLOQUE, ALTO_BLOQUE);
            btnNivel.setPosition(XS[i], 550 - Y_NIVEL - ALTO_BLOQUE);

            boolean desbloqueado = (numeroNivel + 1) <= nivelesDesbloqueados;

            if (desbloqueado) {
                btnNivel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        juego.setScreen(new PantallaJuego(juego, numeroNivel));
                    }
                });
            }

            stage.addActor(btnNivel);

            if (!desbloqueado) {
                ImageButton candado = new ImageButton(
                        new TextureRegionDrawable(new TextureRegion(texCandado)));
                candado.setSize(ANCHO_BLOQUE, ALTO_BLOQUE);
                candado.setPosition(XS[i], 550 - Y_NIVEL - ALTO_BLOQUE);
                candado.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
                stage.addActor(candado);
            }
        }

        ImageButton btnCompetitivo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texCompetitivo)));
        btnCompetitivo.setSize(224f, 77f);
        btnCompetitivo.setPosition(207.8f, 550 - 405f - 77f);
        btnCompetitivo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaCompetitivo(juego));
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(41.2f, 550 - 468.2f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        stage.addActor(btnCompetitivo);
        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(595f, 550 - 10.8f - 50f, 46f, 50f);
        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
            }
        });
        stage.addActor(btnVolumen);
        stage.addActor(btnExit);
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);

        fuenteTitulo.setColor(Color.WHITE);
        fuenteTitulo.draw(batch, obtenerTitulo(), 144.4f, 550 - 89.5f);
        fuenteNumeros.setColor(Color.WHITE);
        fuenteNumeros.draw(batch, "1", 62.8f, 550 - 328f);
        fuenteNumeros.draw(batch, "2", 178.5f, 550 - 328f);
        fuenteNumeros.draw(batch, "3", 301.5f, 550 - 328f);
        fuenteNumeros.draw(batch, "4", 429.8f, 550 - 328f);
        fuenteNumeros.draw(batch, "5", 550.7f, 550 - 328f);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private String obtenerTitulo() {
        if (juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma())) {
            return "LEVELS";
        }
        return "NIVELES";
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
        if (stage != null) {
            stage.getViewport().update(w, h, true);
        }
    }

    @Override
    public void dispose() {
        texFondo.dispose();
        texExit.dispose();
        texCompetitivo.dispose();
        texCandado.dispose();
        texNivel1.dispose();
        texNivel2.dispose();
        texNivel3.dispose();
        texNivel4.dispose();
        fuenteTitulo.dispose();
        fuenteNumeros.dispose();
        texVolumen.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
