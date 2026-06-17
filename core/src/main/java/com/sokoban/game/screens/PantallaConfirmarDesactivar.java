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
import com.sokoban.game.usuarios.GestorUsuarios;

public class PantallaConfirmarDesactivar extends PantallaBase {

    private Texture texFondo, texBtnSi, texBtnNo;
    private BitmapFont fuente;
    private Stage stage;
    private boolean ingles;
    private boolean pendienteDesactivar = false;

    public PantallaConfirmarDesactivar(SokobanGame juego) {
        super(juego, 650, 350);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(650, 350);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/warningfondo.png");
        texBtnSi = new Texture(ingles
                ? "imagenes/botones/Deactivate.png"
                : "imagenes/botones/Desactivar.png");
        texBtnNo = new Texture("imagenes/botones/exit_button.png");

        fuente = new BitmapFont(Gdx.files.internal("fuentes/Pixellari24.fnt"));
        fuente.getData().setScale(1f);

        stage = new Stage(new FitViewport(650, 350));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnSi = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnSi)));
        btnSi.setSize(185f, 55f);
        btnSi.setPosition(350f, 30f);
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                pendienteDesactivar = true;
            }
        });

        ImageButton btnNo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnNo)));
        btnNo.setSize(120f, 55f);
        btnNo.setPosition(130f, 30f);
        btnNo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMisDatos(juego));
            }
        });

        stage.addActor(btnSi);
        stage.addActor(btnNo);
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        if (pendienteDesactivar) {
            ejecutarDesactivacion();
            return;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 350);

        fuente.setColor(Color.BLACK);
        GlyphLayout layout = new GlyphLayout();
        String msg = ingles
                ? "THIS WILL HIDE YOUR RANKING,\nFRIENDS AND STATS.\nARE YOU SURE?"
                : "ESTO OCULTARA TU RANKING,\nAMIGOS Y ESTADISTICAS.\nESTAS SEGURO?";
        layout.setText(fuente, msg, Color.BLACK, 580,
                com.badlogic.gdx.utils.Align.center, true);
        float textY = 350f - (350f - layout.height - 100f) / 2f - 10f;
        fuente.draw(batch, layout, 35f, textY);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void ejecutarDesactivacion() {
        if (juego.getUsuarioActual() == null) {
            return;
        }
        juego.getUsuarioActual().desactivarCuenta();
        GestorUsuarios.guardarUsuario(juego.getUsuarioActual());
        juego.setUsuarioActual(null);
        juego.setScreen(new PantallaInicio(juego));
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
        if (texBtnSi != null) {
            texBtnSi.dispose();
        }
        if (texBtnNo != null) {
            texBtnNo.dispose();
        }
        if (fuente != null) {
            fuente.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
