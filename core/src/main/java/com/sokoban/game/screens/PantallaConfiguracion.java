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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;
import com.sokoban.game.GestorMusica;
public class PantallaConfiguracion extends PantallaBase {

    private Texture texFondo, texMisDatos, texIdioma, texCambiarAvatar;
    private Texture texExit;
    private Stage stage;
    private BitmapFont fuenteTitulo;
    private boolean ingles;
    private Texture texVolumenOn, texVolumenOff;

    public PantallaConfiguracion(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/FondoDefault.png");

        texMisDatos = new Texture("imagenes/botones/" + (ingles ? "MyDetails.png" : "MisDatos.png"));
        texIdioma = new Texture("imagenes/botones/" + (ingles ? "Language.png" : "Idioma.png"));
        texCambiarAvatar = new Texture("imagenes/botones/" + (ingles ? "ChangeAvatar.png" : "CambiarAvatar.png"));
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnMisDatos = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texMisDatos)));
        btnMisDatos.setBounds(189.5f, 550 - 159.4f - 93f, 271f, 93f);
        btnMisDatos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMisDatos(juego));
            }
        });

        Texture texControles = new Texture(
                ingles ? "imagenes/botones/Controls.png" : "imagenes/botones/Controles.png");
        ImageButton btnControles = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texControles)));
        btnControles.setBounds(189.5f, 550 - 456f - 93f, 271f, 93f);
        btnControles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaControles(juego));
            }
        });
        stage.addActor(btnControles);

        ImageButton btnIdioma = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texIdioma)));
        btnIdioma.setBounds(189.5f, 550 - 258.3f - 93f, 271f, 93f);
        btnIdioma.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaIdioma(juego));
            }
        });

        ImageButton btnCambiarAvatar = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texCambiarAvatar)));
        btnCambiarAvatar.setBounds(189.5f, 550 - 357.1f - 93f, 271f, 93f);
        btnCambiarAvatar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaSeleccionAvatar(juego, juego.getUsuarioActual()));
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(8.5f, 550 - 410.8f - 50f, 120.9f, 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
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
        stage.addActor(btnVolumen);
        stage.addActor(btnMisDatos);
        stage.addActor(btnIdioma);
        stage.addActor(btnCambiarAvatar);
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

        String titulo = ingles ? "SETTINGS" : "AJUSTES";
        fuenteTitulo.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(fuenteTitulo, titulo, Color.WHITE, 650, Align.center, false);
        fuenteTitulo.draw(batch, layout, 0f, 550 - 55f);

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
        if (texMisDatos != null) {
            texMisDatos.dispose();
        }
        if (texIdioma != null) {
            texIdioma.dispose();
        }
        if (texCambiarAvatar != null) {
            texCambiarAvatar.dispose();
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
        if (fuenteTitulo != null) {
            fuenteTitulo.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
