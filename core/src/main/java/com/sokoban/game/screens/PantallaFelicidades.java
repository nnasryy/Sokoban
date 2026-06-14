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

public class PantallaFelicidades extends PantallaBase {

    private Texture texFondo;
    private Texture texBtnSeleccionar, texBtnMenu;
    private BitmapFont fuenteTitulo;   // Pixellari60
    private BitmapFont fuenteSubtitulo; // Pixellari35
    private Stage stage;

    public PantallaFelicidades(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        // Texturas
        texFondo = new Texture("imagenes/fondos/Fondodefault.png");
        texBtnSeleccionar = new Texture("imagenes/botones/SeleccionarNivel.png");
        texBtnMenu = new Texture("imagenes/botones/RegresarAMenu.png");

        // Fuentes
        fuenteTitulo = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari60.fnt"));
        fuenteTitulo.getData().setScale(1f);

        fuenteSubtitulo = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari35.fnt"));
        fuenteSubtitulo.getData().setScale(1f);

        // Stage
        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        // Botón Seleccionar Nivel
        ImageButton btnSeleccionar = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnSeleccionar)));
        btnSeleccionar.setSize(215.7f, 73.9f);
        btnSeleccionar.setPosition(75.2f, 550 - 367.8f - 73.9f);
        btnSeleccionar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaSeleccionNivel(juego));
            }
        });

        // Botón Regresar al Menú
        ImageButton btnMenu = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnMenu)));
        btnMenu.setSize(215.7f, 73.9f);
        btnMenu.setPosition(364.9f, 550 - 367.8f - 73.9f);
        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        stage.addActor(btnSeleccionar);
        stage.addActor(btnMenu);
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        // Fondo
        batch.draw(texFondo, 0, 0, 650, 550);

        // Título — idioma dinámico
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout
                = new com.badlogic.gdx.graphics.g2d.GlyphLayout();

// Título centrado horizontalmente en 650px
        String titulo = obtenerTitulo();
        fuenteTitulo.setColor(Color.WHITE);
        layout.setText(fuenteTitulo, titulo, Color.WHITE, 650,
                com.badlogic.gdx.utils.Align.center, false);
        fuenteTitulo.draw(batch, layout, 0f, 550 - 96.4f);

// Subtítulo centrado
        String username = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getUsername().toUpperCase()
                : "JUGADOR";
        String subtitulo = obtenerSubtitulo(username);
        fuenteSubtitulo.setColor(Color.WHITE);
        layout.setText(fuenteSubtitulo, subtitulo, Color.WHITE, 650,
                com.badlogic.gdx.utils.Align.center, true);
        fuenteSubtitulo.draw(batch, layout, 0f, 550 - 179.2f);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private String obtenerTitulo() {
        if (juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma())) {
            return "!CONGRATULATIONS!";
        }
        return "!FELICIDADES!";
    }

    private String obtenerSubtitulo(String username) {
        if (juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma())) {
            return "@" + username + " HAS COMPLETED ALL LEVELS";
        }
        return "@" + username + " HAS COMPLETADO TODOS LOS NIVELES";
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
        texBtnSeleccionar.dispose();
        texBtnMenu.dispose();
        fuenteTitulo.dispose();
        fuenteSubtitulo.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
