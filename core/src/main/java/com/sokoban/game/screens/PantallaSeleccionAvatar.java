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
import com.sokoban.game.usuarios.Usuario;

public class PantallaSeleccionAvatar extends PantallaBase {

    private Texture texFondo, texBtnFoto, texBtnListo, texVolumen;
    private Stage stage;
    private BitmapFont fuente;
    private Usuario usuario;
    private String rutaFotoElegida = null;

    public PantallaSeleccionAvatar(SokobanGame juego, Usuario u) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
        this.usuario = u;
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        texFondo = new Texture("imagenes/fondos/SeleccionFondo.png");
        texBtnFoto = new Texture("imagenes/botones/pfp_button.png");
        texBtnListo = new Texture("imagenes/botones/listo_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");

        fuente = new BitmapFont(Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuente.getData().setScale(1f);

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnFoto = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnFoto)));
        btnFoto.setBounds(62.5f, 550 - 232.7f - 181f, 157f, 181f);
        btnFoto.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                abrirSelectorFoto();
            }
        });

        ImageButton btnListo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnListo)));
        btnListo.setBounds(252.2f, 550 - 463.9f - 62f, 180f, 62f);
        btnListo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                confirmar();
            }
        });

        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(589.7f, 550 - 10.8f - 50f, 46f, 50f);
        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
            }
        });

        stage.addActor(btnFoto);
        stage.addActor(btnListo);
        stage.addActor(btnVolumen);
    }

    private void abrirSelectorFoto() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Selecciona tu foto de perfil");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imagenes", "png", "jpg", "jpeg"));

            int resultado = chooser.showOpenDialog(null);
            if (resultado == javax.swing.JFileChooser.APPROVE_OPTION) {
                rutaFotoElegida = chooser.getSelectedFile().getAbsolutePath();
            }
        });
    }

    private void confirmar() {
        if (rutaFotoElegida == null) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Debes seleccionar una foto de perfil",
                    new PantallaSeleccionAvatar(juego, usuario)));
            return;
        }

        usuario.setRutaFotoPerfil(rutaFotoElegida);
        usuario.setTipoAvatar(0); // 0 = foto propia
        GestorUsuarios.guardarUsuario(usuario);
        juego.setUsuarioActual(usuario);
        juego.setScreen(new PantallaMenu(juego));
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

        if (rutaFotoElegida != null) {
            fuente.setColor(Color.GREEN);
            fuente.draw(batch, "FOTO SELECCIONADA!",
                    62.5f, 550 - 232.7f - 181f - 10f);
        }
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
        texBtnFoto.dispose();
        texBtnListo.dispose();
        texVolumen.dispose();
        fuente.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
