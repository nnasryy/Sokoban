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
import com.sokoban.game.usuarios.GestorUsuarios;

public class PantallaControles extends PantallaBase {

    private Texture texFondo, texExit, texVolumenOn, texVolumenOff;
    private BitmapFont fuenteTitulo, fuenteTexto;
    private Stage stage;
    private boolean ingles;
    private float volumenActual;
    private Texture texPixel;

    public PantallaControles(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/FondoAzul.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");
        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);
        fuenteTexto = new BitmapFont(Gdx.files.internal("fuentes/Pixellari27.fnt"));
        fuenteTexto.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(8.5f, 550 - 490f - 50.3f, 120.9f, 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
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
        volumenActual = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getVolumen() : 0.8f;
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
        com.badlogic.gdx.graphics.Pixmap pmBtn = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pmBtn.setColor(new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f));
        pmBtn.fill();
        Texture texBtn = new Texture(pmBtn);
        pmBtn.dispose();

        ImageButton btnWASD = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtn)));
        btnWASD.setSize(140f, 45f);
        btnWASD.setPosition(100f, 40f);
        btnWASD.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (juego.getUsuarioActual() != null) {
                    juego.getUsuarioActual().setEsquemaControles("WASD");
                    GestorUsuarios.guardarUsuario(juego.getUsuarioActual());
                }
            }
        });

        ImageButton btnFlechas = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtn)));
        btnFlechas.setSize(140f, 45f);
        btnFlechas.setPosition(260f, 40f);
        btnFlechas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (juego.getUsuarioActual() != null) {
                    juego.getUsuarioActual().setEsquemaControles("ARROWS");
                    GestorUsuarios.guardarUsuario(juego.getUsuarioActual());
                }
            }
        });

        stage.addActor(btnWASD);
        stage.addActor(btnFlechas);
    }

@Override
public void render(float delta) {
    if (texFondo == null) return;

    float barX = 100f, barY = 80f, barW = 450f, barH = 20f;
    if (Gdx.input.isTouched()) {
        com.badlogic.gdx.math.Vector2 tp = new com.badlogic.gdx.math.Vector2(
                Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(tp);
        if (tp.x >= barX && tp.x <= barX + barW
                && tp.y >= barY && tp.y <= barY + barH) {
            volumenActual = Math.max(0f, Math.min(1f, (tp.x - barX) / barW));
            GestorMusica.setVolumen(volumenActual);
            if (juego.getUsuarioActual() != null) {
                juego.getUsuarioActual().setVolumen(volumenActual);
                com.sokoban.game.usuarios.GestorUsuarios
                        .guardarUsuario(juego.getUsuarioActual());
            }
        }
    }

    viewport.apply();
    batch.setProjectionMatrix(viewport.getCamera().combined);

    batch.begin();

        batch.draw(texFondo, 0, 0, 650, 550);

        GlyphLayout layout = new GlyphLayout();
        fuenteTitulo.setColor(Color.WHITE);
        String titulo = ingles ? "CONTROLS" : "CONTROLES";
        layout.setText(fuenteTitulo, titulo, Color.WHITE, 650, Align.center, false);
        fuenteTitulo.draw(batch, layout, 0f, 550 - 18f);

        fuenteTexto.setColor(Color.WHITE);
        float startY = 550 - 110f;
        float lineH = 38f;
        String[] lineas = ingles ? new String[]{
            "MOVE UP      :  W  /  UP ARROW",
            "MOVE DOWN    :  S  /  DOWN ARROW",
            "MOVE LEFT    :  A  /  LEFT ARROW",
            "MOVE RIGHT   :  D  /  RIGHT ARROW",
            "REVERT MOVE  :  REVERT BUTTON",
            "RESTART      :  RESTART BUTTON",
            "EXIT         :  EXIT BUTTON"
        } : new String[]{
            "MOVER ARRIBA    :  W  /  FLECHA ARRIBA",
            "MOVER ABAJO     :  S  /  FLECHA ABAJO",
            "MOVER IZQ       :  A  /  FLECHA IZQ",
            "MOVER DER       :  D  /  FLECHA DER",
            "REVERTIR MOVIM  :  BOTON REVERT",
            "REINICIAR       :  BOTON RESTART",
            "SALIR           :  BOTON EXIT"
        };
        for (int i = 0; i < lineas.length; i++) {
            fuenteTexto.draw(batch, lineas[i], 50f, startY - i * lineH);
        }

        fuenteTexto.setColor(Color.WHITE);
        fuenteTexto.draw(batch,
                (ingles ? "VOLUME: " : "VOLUMEN: ") + (int)(volumenActual * 100) + "%",
                barX, barY + 40f);
        batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        batch.draw(texPixel, barX, barY, barW, barH);
        batch.setColor(Color.WHITE);
        batch.draw(texPixel, barX, barY, barW * volumenActual, barH);
        batch.setColor(Color.WHITE);

        String esquema = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getEsquemaControles() : "WASD";
        fuenteTexto.setColor(Color.WHITE);
        fuenteTexto.draw(batch, "WASD", 130f, 72f);
        fuenteTexto.draw(batch, ingles ? "ARROWS" : "FLECHAS", 285f, 72f);

        batch.setColor(new Color(1f, 1f, 0f, 0.3f));
        if ("WASD".equals(esquema)) {
            batch.draw(texPixel, 100f, 40f, 140f, 45f);
        } else {
            batch.draw(texPixel, 260f, 40f, 140f, 45f);
        }
        batch.setColor(Color.WHITE);

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
        if (fuenteTexto != null) {
            fuenteTexto.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
