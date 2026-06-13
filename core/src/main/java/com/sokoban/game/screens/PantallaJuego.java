/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.sokoban.game.logica.Constantes;
import com.sokoban.game.logica.Tablero;
import com.sokoban.game.niveles.GestorNiveles;

public class PantallaJuego extends PantallaBase {

    // HUD
    private Texture texHUD;
    private Texture texRestart, texRevert, texExit;
    private BitmapFont fuenteNivel;    // Pixellari 14
    private BitmapFont fuenteHUD;      // Pixellari 28
    private Stage stage;

    // Tiles — se cargan según el nivel
    private Texture[] tiles;

    // Sprite jugador
    private Texture texJugador;

    // Lógica
    private Tablero tablero;
    private int numeroNivel;

    // Timer
    private float tiempoSegundos = 0f;
    private boolean jugando = true;

    // Tamaño del mapa en pantalla
    private static final float MAPA_X = 160.6f;
    private static final float MAPA_Y_TOP = 94.8f;   // desde arriba en Canva
    private static final float MAPA_ANCHO = 607f;
    private static final float MAPA_ALTO = 533f;

    public PantallaJuego(SokobanGame juego, int numeroNivel) {
        super(juego, SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME);
        this.numeroNivel = numeroNivel;
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME);

        // Cargar tablero
        tablero = GestorNiveles.cargarNivel(numeroNivel);

        // HUD
        texHUD = new Texture("imagenes/fondos/fondonivel.png");
        texRestart = new Texture("imagenes/botones/restart.png");
        texRevert = new Texture("imagenes/botones/revert.png");

        // Fuentes
        fuenteNivel = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuenteNivel.getData().setScale(1f);

        fuenteHUD = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari28.fnt"));
        fuenteHUD.getData().setScale(1f);

        // Cargar tiles del nivel actual
        cargarTiles();

        // Sprite jugador
        texJugador = new Texture(
                "imagenes/avatar/idle_bottom.png");

        // Stage
        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME));
        Gdx.input.setInputProcessor(stage);

        // Botón Restart
        ImageButton btnRestart = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texRestart)));
        btnRestart.pack();
        btnRestart.setPosition(828.6f,
                628 - 12.9f - btnRestart.getHeight());
        btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                tablero.reiniciar();
                tiempoSegundos = 0f;
                jugando = true;
            }
        });

        // Botón Revert
        ImageButton btnRevert = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texRevert)));
        btnRevert.pack();
        btnRevert.setPosition(828.6f,
                628 - 71.6f - btnRevert.getHeight());
        btnRevert.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                // lógica de deshacer después
            }
        });

        // Revert conectado
        btnRevert.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                tablero.revert();
            }
        });

// Botón Exit
        texExit = new Texture("imagenes/botones/exit_button.png");
        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(864.5f, 628 - 563.2f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        stage.addActor(btnRestart);
        stage.addActor(btnRevert);
        stage.addActor(btnExit);
    }

    private void cargarTiles() {
        // Busca las imágenes del nivel actual
        // Cada nivel tiene su carpeta: tiles/nivel1/, tiles/nivel2/, etc.
        String carpeta = "imagenes/tiles/nivel" + (numeroNivel + 1) + "/";
        int maxTiles = 12; // máximo número de tiles posibles
        tiles = new Texture[maxTiles];

        for (int i = 0; i < maxTiles; i++) {
            try {
                com.badlogic.gdx.files.FileHandle file
                        = Gdx.files.internal(carpeta + i + ".png");
                if (file.exists()) {
                    tiles[i] = new Texture(file);
                }
            } catch (Exception e) {
                // tile no existe para este nivel
            }
        }
    }

    @Override
    public void render(float delta) {
        if (texHUD == null) {
            return;
        }

        // Actualizar timer
        if (jugando) {
            tiempoSegundos += delta;
        }

        // Input teclado
        manejarInput();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        // Dibujar HUD de fondo
        batch.draw(texHUD, 0, 0, 1000, 628);

        // Texto número de nivel
        // Texto número de nivel — negro
        fuenteNivel.setColor(Color.BLACK);
        fuenteNivel.draw(batch,
                String.valueOf(numeroNivel + 1),
                137f, 628 - 12.9f);

// Texto movimientos — negro
        fuenteHUD.setColor(Color.BLACK);
        fuenteHUD.draw(batch,
                String.valueOf(tablero.getMovimientos()),
                393.5f, 628 - 46.6f);

// Texto tiempo — negro
        fuenteHUD.setColor(Color.BLACK);
        fuenteHUD.draw(batch,
                formatearTiempo((int) tiempoSegundos),
                577.1f, 628 - 47.9f);

        // Dibujar mapa
        dibujarMapa();

        batch.end();

        // Verificar victoria
        if (tablero.nivelCompleto() && jugando) {
            jugando = false;
            guardarProgreso();
            irSiguienteNivel();
        }

        stage.act(delta);
        stage.draw();
    }

    private void manejarInput() {
        if (!jugando) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            tablero.mover(0, -1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            tablero.mover(0, 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            tablero.mover(-1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            tablero.mover(1, 0);
        }
    }

    private void dibujarMapa() {
        int[][] grid = tablero.getGrid();
        int filas = tablero.getFilas();
        int columnas = tablero.getColumnas();

        float tileAncho = MAPA_ANCHO / columnas;
        float tileAlto = MAPA_ALTO / filas;
        float mapaYlibGDX = 628 - MAPA_Y_TOP - MAPA_ALTO;

        // Primera pasada — todos los tiles de fondo
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int valor = grid[fila][col];
                float px = MAPA_X + col * tileAncho;
                float py = mapaYlibGDX + (filas - 1 - fila) * tileAlto;

                // Si es jugador dibuja el tile de debajo (0)
                int tileBase = (valor == Constantes.JUGADOR) ? 0 : valor;

                if (tileBase >= 0 && tileBase < tiles.length
                        && tiles[tileBase] != null) {
                    batch.draw(tiles[tileBase], px, py, tileAncho, tileAlto);
                }
            }
        }

        // Segunda pasada — jugador encima de todo
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                if (grid[fila][col] == Constantes.JUGADOR) {
                    float px = MAPA_X + col * tileAncho;
                    float py = mapaYlibGDX + (filas - 1 - fila) * tileAlto;
                    if (texJugador != null) {
                        batch.draw(texJugador, px, py, tileAncho, tileAlto);
                    }
                }
            }
        }
    }

    private void guardarProgreso() {
        if (juego.getUsuarioActual() == null) {
            return;
        }
        juego.getUsuarioActual().registrarPartida(
                numeroNivel, (long) tiempoSegundos, true);
        juego.getUsuarioActual().setNivelActual(numeroNivel + 1);
        com.sokoban.game.usuarios.GestorUsuarios
                .guardarUsuario(juego.getUsuarioActual());
    }

    private void irSiguienteNivel() {
        if (GestorNiveles.esUltimoNivel(numeroNivel)) {
            juego.setScreen(new PantallaFelicidades(juego));
        } else {
            juego.setScreen(new PantallaJuego(juego, numeroNivel + 1));
        }
    }

    private String formatearTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
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
        texHUD.dispose();
        texRestart.dispose();
        texRevert.dispose();
        fuenteNivel.dispose();
        fuenteHUD.dispose();
        texJugador.dispose();
        if (tiles != null) {
            for (Texture t : tiles) {
                if (t != null) {
                    t.dispose();
                }
            }
        }
        if (texExit != null) {
            texExit.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
