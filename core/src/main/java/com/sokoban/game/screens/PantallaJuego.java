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
import com.sokoban.game.hilos.HiloTimer;

public class PantallaJuego extends PantallaBase {

    private Texture texHUD;
    private Texture texRestart, texRevert, texExit;
    private BitmapFont fuenteNivel;
    private BitmapFont fuenteHUD;
    private Stage stage;
    private BitmapFont fuenteNivel24;
    private int intentos = 1;
    private Texture[] tiles;
    private boolean modoCompetitivo = false;
    private String nombreJ1 = "";
    private String nombreJ2 = "";
    private int turnoActual = 1;
    private int tiempoJ1 = -1;
    private Texture[] spritesIdle = new Texture[4];
    private Texture[] spritesLeftF = new Texture[4];
    private Texture[] spritesRightF = new Texture[4];
    private int direccionActual = 0;
    private boolean pieDerecho = false;
    private float tiempoFrame = 0f;
    private static final float DURACION_FRAME = 0.15f;
    private boolean seMovio = false;
    private Tablero tablero;
    private int numeroNivel;
    private HiloTimer hiloTimer;
    private boolean jugando = true;
    private static final float MAPA_X = 160.6f;
    private static final float MAPA_Y_TOP = 94.8f;
    private static final float MAPA_ANCHO = 607f;
    private static final float MAPA_ALTO = 533f;

    public PantallaJuego(SokobanGame juego, int numeroNivel) {
        super(juego, SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME);
        this.numeroNivel = numeroNivel;
    }

    public PantallaJuego(SokobanGame juego, int numeroNivel,
            String nombreJ1, String nombreJ2, int turno, int tiempoJ1) {
        super(juego, SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME);
        this.numeroNivel = numeroNivel;
        this.modoCompetitivo = true;
        this.nombreJ1 = nombreJ1;
        this.nombreJ2 = nombreJ2;
        this.turnoActual = turno;
        this.tiempoJ1 = tiempoJ1;
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME);

        tablero = GestorNiveles.cargarNivel(numeroNivel);

        String[] fondosPorNivel = {
            "FondoNivel1", "FondoNivel2", "FondoNivel3", "FondoNivel4", "FondoNivel1"
        };
        texHUD = new Texture("imagenes/fondos/" + fondosPorNivel[numeroNivel] + ".png");
        texRestart = new Texture("imagenes/botones/restart.png");
        texRevert = new Texture("imagenes/botones/revert.png");

        fuenteNivel24 = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari24.fnt"));
        fuenteNivel24.getData().setScale(1f);

        fuenteHUD = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari28.fnt"));
        fuenteHUD.getData().setScale(1f);

        cargarTiles();

        String carpetaAvatar;
        if (juego.getUsuarioActual() != null && juego.getUsuarioActual().getTipoAvatar() == 1) {
            carpetaAvatar = "imagenes/avatar/girl_sprite/";
        } else {
            carpetaAvatar = "imagenes/avatar/boy_sprite/";
        }

        String[] dirs = {"bottom", "left", "right", "top"};
        for (int i = 0; i < 4; i++) {
            spritesIdle[i] = new Texture(carpetaAvatar + "idle_" + dirs[i] + ".png");
            spritesLeftF[i] = new Texture(carpetaAvatar + "walkingleftfoot_" + dirs[i] + ".png");
            spritesRightF[i] = new Texture(carpetaAvatar + "walkingrightfoot_" + dirs[i] + ".png");
        }
        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_GAME, SokobanGame.ALTO_GAME));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnRestart = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texRestart)));
        btnRestart.pack();
        btnRestart.setPosition(828.6f,
                628 - 12.9f - btnRestart.getHeight());
        btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                tablero.reiniciar();
                intentos++;
                jugando = true;
            }
        });

        ImageButton btnRevert = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texRevert)));
        btnRevert.pack();
        btnRevert.setPosition(828.6f,
                628 - 71.6f - btnRevert.getHeight());
        btnRevert.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                tablero.revert();
            }
        });

        texExit = new Texture("imagenes/botones/exit_button.png");
        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(864.5f, 628 - 563.2f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (modoCompetitivo) {
                    boolean ingles = juego.getUsuarioActual() != null
                            && "en".equals(juego.getUsuarioActual().getIdioma());
                    String ganador = turnoActual == 1
                            ? (ingles ? nombreJ2 + " wins! " + nombreJ1 + " retired."
                                    : nombreJ2 + " gano! " + nombreJ1 + " se retiro.")
                            : (ingles ? nombreJ1 + " wins! " + nombreJ2 + " retired."
                                    : nombreJ1 + " gano! " + nombreJ2 + " se retiro.");
                    hiloTimer.pausar();
                    juego.setScreen(new PantallaFelicidades(juego, ganador));
                } else {
                    juego.setScreen(new PantallaMenu(juego));
                }
            }
        });

        stage.addActor(btnRestart);
        stage.addActor(btnRevert);
        stage.addActor(btnExit);
        hiloTimer = new HiloTimer();
        hiloTimer.start();
    }

    private void cargarTiles() {
        String carpeta = "imagenes/tiles/nivel" + (numeroNivel + 1) + "/";
        int maxTiles = 12;
        tiles = new Texture[maxTiles];

        for (int i = 0; i < maxTiles; i++) {
            try {
                com.badlogic.gdx.files.FileHandle file
                        = Gdx.files.internal(carpeta + i + ".png");
                if (file.exists()) {
                    tiles[i] = new Texture(file);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void render(float delta) {
        if (texHUD == null) {
            return;
        }

        manejarInput(delta);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(texHUD, 0, 0, 1000, 628);

        boolean ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        fuenteNivel24.setColor(Color.BLACK);
        fuenteNivel24.draw(batch,
                (ingles ? "LEVEL: " : "NIVEL: ") + (numeroNivel + 1),
                21.3f, 628 - 7.1f);  // se queda igual

// INTENTOS — debajo de NIVEL con más separación
        fuenteNivel24.draw(batch,
                (ingles ? "TRIES: " : "INTENTOS: ") + intentos,
                21.3f, 628 - 55f);  // antes era 628-30f, ahora más abajo

// MOVIMIENTOS — se queda igual
        fuenteHUD.draw(batch,
                (ingles ? "MOVES: " : "MOVIMIENTOS: ") + tablero.getMovimientos(),
                164.7f, 628 - 38.1f);

        fuenteHUD.setColor(Color.BLACK);
        fuenteHUD.draw(batch,
                formatearTiempo(hiloTimer.getSegundos()),
                682.4f, 628 - 35f);
        if (modoCompetitivo) {
            String textoTurno = ingles ? "TURN:" : "TURNO:";
            fuenteHUD.setColor(Color.BLACK);
            fuenteHUD.draw(batch, textoTurno, 820f, 628 - 140f);
            fuenteNivel24.setColor(Color.BLACK);
            fuenteNivel24.draw(batch,
                    turnoActual == 1 ? nombreJ1 : nombreJ2,
                    820f, 628 - 170f);
        }
        dibujarMapa();

        batch.end();

        if (tablero.nivelCompleto() && jugando) {
            jugando = false;
            hiloTimer.pausar();

            if (modoCompetitivo) {
                manejarVictoriaCompetitiva();
            } else {
                guardarProgreso();
                irSiguienteNivel();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    private void manejarInput(float delta) {
        if (!jugando) {
            return;
        }

        seMovio = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (tablero.mover(0, -1)) {
                direccionActual = 3;
                seMovio = true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (tablero.mover(0, 1)) {
                direccionActual = 0;
                seMovio = true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (tablero.mover(-1, 0)) {
                direccionActual = 1;
                seMovio = true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (tablero.mover(1, 0)) {
                direccionActual = 2;
                seMovio = true;
            }
        }

        if (seMovio) {
            tiempoFrame += delta;
            if (tiempoFrame >= DURACION_FRAME) {
                tiempoFrame = 0f;
                pieDerecho = !pieDerecho;
            }
        } else {
            tiempoFrame = 0f;
        }
    }

    private void manejarVictoriaCompetitiva() {
        boolean ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        if (turnoActual == 1) {
            int tiempoFinalJ1 = hiloTimer.getSegundos();
            String msg = ingles
                    ? "Player 1 finished in " + formatearTiempo(tiempoFinalJ1) + "!\nNow it's Player 2's turn."
                    : "Jugador 1 termino en " + formatearTiempo(tiempoFinalJ1) + "!\nAhora le toca al Jugador 2.";

            juego.setScreen(new PantallaAdvertencia(juego, msg,
                    new PantallaJuego(juego, numeroNivel,
                            nombreJ1, nombreJ2, 2, tiempoFinalJ1)));

        } else {
            int tiempoFinalJ2 = hiloTimer.getSegundos();
            String ganador;
            boolean ingles2 = ingles;

            if (tiempoJ1 < tiempoFinalJ2) {
                ganador = ingles2
                        ? nombreJ1 + " wins! (" + formatearTiempo(tiempoJ1) + " vs " + formatearTiempo(tiempoFinalJ2) + ")"
                        : nombreJ1 + " gano! (" + formatearTiempo(tiempoJ1) + " vs " + formatearTiempo(tiempoFinalJ2) + ")";
            } else if (tiempoFinalJ2 < tiempoJ1) {
                ganador = ingles2
                        ? nombreJ2 + " wins! (" + formatearTiempo(tiempoFinalJ2) + " vs " + formatearTiempo(tiempoJ1) + ")"
                        : nombreJ2 + " gano! (" + formatearTiempo(tiempoFinalJ2) + " vs " + formatearTiempo(tiempoJ1) + ")";
            } else {
                ganador = ingles2 ? "It's a tie!" : "Empate!";
            }

            juego.setScreen(new PantallaFelicidades(juego, ganador));
        }
    }

    private void dibujarMapa() {
        int[][] grid = tablero.getGrid();
        int filas = tablero.getFilas();
        int columnas = tablero.getColumnas();

        float tileAncho = MAPA_ANCHO / columnas;
        float tileAlto = MAPA_ALTO / filas;
        float mapaYlibGDX = 628 - MAPA_Y_TOP - MAPA_ALTO;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int valor = grid[fila][col];
                float px = MAPA_X + col * tileAncho;
                float py = mapaYlibGDX + (filas - 1 - fila) * tileAlto;

                int tileBase = (valor == Constantes.JUGADOR) ? Constantes.VACIO : valor;

                if (tileBase != Constantes.VACIO
                        && tileBase >= 0 && tileBase < tiles.length
                        && tiles[tileBase] != null) {
                    batch.draw(tiles[tileBase], px, py, tileAncho, tileAlto);
                }
            }
        }

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                if (grid[fila][col] == Constantes.JUGADOR) {
                    float px = MAPA_X + col * tileAncho;
                    float py = mapaYlibGDX + (filas - 1 - fila) * tileAlto;

                    Texture spriteActual;
                    if (!seMovio) {
                        spriteActual = spritesIdle[direccionActual];
                    } else if (pieDerecho) {
                        spriteActual = spritesRightF[direccionActual];
                    } else {
                        spriteActual = spritesLeftF[direccionActual];
                    }

                    if (spriteActual != null) {
                        batch.draw(spriteActual, px, py, tileAncho, tileAlto);
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
                numeroNivel, hiloTimer.getSegundos(), true);
        juego.getUsuarioActual().desbloquearSiguienteNivel();
        com.sokoban.game.usuarios.GestorUsuarios
                .guardarUsuario(juego.getUsuarioActual());
    }

    private void irSiguienteNivel() {
        if (GestorNiveles.esUltimoNivel(numeroNivel)) {
            String ganador;
            ganador=juego.getUsuarioActual().getIdioma();
            juego.setScreen(new PantallaFelicidades(juego,ganador));
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
        fuenteNivel24.dispose();
        fuenteHUD.dispose();
        for (int i = 0; i < 4; i++) {
            if (spritesIdle[i] != null) {
                spritesIdle[i].dispose();
            }
            if (spritesLeftF[i] != null) {
                spritesLeftF[i].dispose();
            }
            if (spritesRightF[i] != null) {
                spritesRightF[i].dispose();
            }
        }
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
        if (hiloTimer != null) {
            hiloTimer.detener();
        }
    }
}
