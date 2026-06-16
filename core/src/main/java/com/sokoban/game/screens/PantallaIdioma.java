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
import com.sokoban.game.usuarios.GestorUsuarios;

public class PantallaIdioma extends PantallaBase {

    private Texture texFondo, texEnglish, texEspaniol;
    private Texture texExit, texVolumen;
    private BitmapFont fuenteTitulo;
    private Stage stage;
    private boolean ingles;

    public PantallaIdioma(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/FondoDefault.png");
        texEnglish = new Texture("imagenes/botones/BotonEnglish.png");
        texEspaniol = new Texture("imagenes/botones/BotonEspaniol.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");

        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnEnglish = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texEnglish)));
        btnEnglish.setBounds(189.6f, 550 - 182f - 93f, 271.2f, 93f);
        btnEnglish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                cambiarIdioma("en");
            }
        });

        ImageButton btnEspaniol = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texEspaniol)));
        btnEspaniol.setBounds(189.6f, 550 - 292f - 93f, 271.2f, 93f);
        btnEspaniol.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                cambiarIdioma("es");
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(8.5f, 550 - 410.8f - 50f, 120.9f, 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
            }
        });

        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(595f, 550 - 10.8f - 50f, 46f, 50f);

        stage.addActor(btnEnglish);
        stage.addActor(btnEspaniol);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private void cambiarIdioma(String idioma) {
        var usuario = juego.getUsuarioActual();
        if (usuario == null) {
            return;
        }

        usuario.setIdioma(idioma);
        GestorUsuarios.guardarUsuario(usuario);

        ingles = "en".equals(idioma);

        juego.setScreen(new PantallaConfiguracion(juego));
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
        fuenteTitulo.draw(batch, ingles ? "LANGUAGE" : "IDIOMA", 201f, 550 - 55f);

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
        if (texEnglish != null) {
            texEnglish.dispose();
        }
        if (texEspaniol != null) {
            texEspaniol.dispose();
        }
        if (texExit != null) {
            texExit.dispose();
        }
        if (texVolumen != null) {
            texVolumen.dispose();
        }
        if (fuenteTitulo != null) {
            fuenteTitulo.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
