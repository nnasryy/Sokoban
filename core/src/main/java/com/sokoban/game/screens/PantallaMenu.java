/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;
import com.sokoban.game.GestorMusica;

public class PantallaMenu extends PantallaBase {

    private Texture texFondo, texPlay, texStats, texSettings;
    private Texture texExit;
    private Stage stage;
    private boolean ingles;
    private Texture texVolumenOn, texVolumenOff;

    public PantallaMenu(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/MenuPrincipal.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");
        texPlay = new Texture(ingles ? "imagenes/botones/play_button.png" : "imagenes/botones/Jugar.png");
        texStats = new Texture("imagenes/botones/stats_button.png");
        texSettings = new Texture(ingles ? "imagenes/botones/settings_button.png" : "imagenes/botones/Ajustes.png");

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnPlay = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texPlay)));
        btnPlay.setBounds(189.5f, 550 - 159.4f - 93f, 271f, 93f);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaSeleccionNivel(juego));
            }
        });

        ImageButton btnStats = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texStats)));
        btnStats.setBounds(189.5f, 550 - 258.3f - 93f, 271f, 93f);
        btnStats.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaStats(juego));
            }
        });

        ImageButton btnSettings = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texSettings)));
        btnSettings.setBounds(189.5f, 550 - 357.1f - 93f, 271f, 93f);
        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(8.5f, 550 - 410.8f - 50f, 120.9f, 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setUsuarioActual(null);
                juego.setScreen(new PantallaInicio(juego));
            }
        });

        Texture texActual = GestorMusica.isActiva() ? texVolumenOn : texVolumenOff;
        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texActual)));
        btnVolumen.setBounds(595f, 550 - 10.8f - 50f, 46f, 50f);
        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                GestorMusica.toggleMusica(btnVolumen, texVolumenOn, texVolumenOff);
            }
        });
        stage.addActor(btnPlay);
        stage.addActor(btnStats);
        stage.addActor(btnSettings);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
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
        batch.end();

        stage.act(delta);
        stage.draw();
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
        if (texFondo != null) {
            texFondo.dispose();
        }
        if (texPlay != null) {
            texPlay.dispose();
        }
        if (texStats != null) {
            texStats.dispose();
        }
        if (texSettings != null) {
            texSettings.dispose();
        }
        if (texExit != null) {
            texExit.dispose();
        }
        if (texVolumenOn != null) {
            texVolumenOn.dispose();
        }
        if (texVolumenOff != null) {
            texVolumenOff.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
