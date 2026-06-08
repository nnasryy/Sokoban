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

public class PantallaAdvertencia extends PantallaBase {

    private Texture texFondo;
    private Texture texBtnOk;
    private BitmapFont fuente;
    private Stage stage;

    private String mensaje;          // texto que cambia según la situación
    private PantallaBase pantallaAnterior; // a dónde volver al cerrar

    public PantallaAdvertencia(SokobanGame juego,
            String mensaje,
            PantallaBase pantallaAnterior) {
        super(juego, 650, 350);
        this.mensaje = mensaje;
        this.pantallaAnterior = pantallaAnterior;
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(650, 350);

        texFondo = new Texture("imagenes/fondos/warningfondo.png");
        texBtnOk = new Texture("imagenes/botones/exit_button.png");

        fuente = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari28.fnt"));
        fuente.getData().setScale(1f);
        fuente.setColor(Color.WHITE);

        stage = new Stage(new FitViewport(650, 350));
        Gdx.input.setInputProcessor(stage);

        // Botón OK centrado
        ImageButton btnOk = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnOk)));
        btnOk.setSize(120, 40);
        btnOk.setPosition(265, 20); // centrado en 650px
        btnOk.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(pantallaAnterior);
            }
        });

        stage.addActor(btnOk);
    }

    @Override
    public void render(float delta) {
        if (fuente == null || texFondo == null) {
            return;
        }
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 350);

        // Texto dentro del cuadro
        // cuadro: x=35, y=98.5, w=580, h=216.5
        // en libGDX Y va de abajo arriba
        // centro vertical del cuadro = 350 - 98.5 - (216.5/2) = ~143
        fuente.setColor(Color.BLACK);

        // Dibuja el texto con wrap dentro del cuadro
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout
                = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
        layout.setText(fuente, mensaje.toUpperCase(), Color.BLACK,
                580,
                com.badlogic.gdx.utils.Align.center,
                true);

        fuente.draw(batch, layout,
                35f,
                175 + layout.height);
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
        texFondo.dispose();
        texBtnOk.dispose();
        fuente.dispose();
        stage.dispose();
    }
}
