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
import com.sokoban.game.usuarios.Usuario;
import java.text.SimpleDateFormat;

public class PantallaStats extends PantallaBase {

    private Texture texFondo, texExit, texVolumen;
    private BitmapFont fuente;
    private Stage stage;
    private Texture texPixel; // para dibujar bordes y rectángulos

    // Tabs
    private enum Tab {
        RANKING, PARTIDAS, SOLICITUDES, COMPARAR
    }
    private Tab tabActual = Tab.PARTIDAS;

    // Colores
    private static final Color COLOR_NORMAL
            = new Color(117f / 255f, 58f / 255f, 50f / 255f, 1f);
    private static final Color COLOR_SELECCIONADO
            = new Color(70f / 255f, 39f / 255f, 35f / 255f, 1f);

    public PantallaStats(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        texFondo = new Texture("imagenes/fondos/FondoStats.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");

        fuente = new BitmapFont(Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuente.getData().setScale(1f);

        // Pixel blanco 1x1 para dibujar rectángulos con tinte
        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        // Botón Exit
        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(14.6f, 550 - 487.4f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        // Botón Volumen
        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(595f, 550 - 10.8f - 50f, 46f, 50f);
        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                // música después
            }
        });

        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private void dibujarCuadros() {
        Color borde = new Color(70f / 255f, 39f / 255f, 35f / 255f, 1f);

        // Cuadro exterior (tabs)
        dibujarRectanguloConBorde(37.8f, 127.2f, 577.3f, 343.2f, borde);

        // Cuadro interior (contenido)
        dibujarRectanguloConBorde(64.8f, 187.6f, 523.5f, 271.4f, borde);
    }

    private void dibujarRectanguloConBorde(float x, float yCanva, float w, float h, Color borde) {
        float y = 550 - yCanva - h; // convertir a libGDX

        Color relleno = new Color(208f/255f, 104f/255f, 63f/255f, 1f);
    batch.setColor(relleno);
    batch.draw(texPixel, x + 1, y + 1, w - 2, h - 2);
        
        batch.setColor(borde);
        // Borde de 1px — dibuja 4 líneas finas
        batch.draw(texPixel, x, y, w, 1);          // abajo
        batch.draw(texPixel, x, y + h - 1, w, 1);  // arriba
        batch.draw(texPixel, x, y, 1, h);          // izquierda
        batch.draw(texPixel, x + w - 1, y, 1, h);  // derecha

        batch.setColor(Color.WHITE); // resetear color
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Manejar clicks en los tabs (texto clickeable)
        manejarClickTabs();

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);
        dibujarCuadros();

        // Dibujar los 4 tabs
        dibujarTab("RANKING", 62.1f, 148.5f, Tab.RANKING);
        dibujarTab("SOLICITUDES", 175f, 148.5f, Tab.SOLICITUDES);
        dibujarTab("PARTIDAS", 350f, 148.5f, Tab.PARTIDAS);
        dibujarTab("COMPARAR", 473.4f, 148.5f, Tab.COMPARAR);

        // Contenido según el tab activo
        switch (tabActual) {
            case PARTIDAS:
                dibujarPartidas();
                break;
            case RANKING:
                dibujarRanking();
                break;
            case SOLICITUDES:
                dibujarSolicitudes();
                break;
            case COMPARAR:
                dibujarComparar();
                break;
        }

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void dibujarTab(String texto, float x, float y, Tab tab) {
        fuente.setColor(tabActual == tab ? COLOR_SELECCIONADO : COLOR_NORMAL);
        fuente.draw(batch, texto, x, 550 - y);
    }

    private void manejarClickTabs() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        // Convertir coordenadas de pantalla a coordenadas del viewport
        com.badlogic.gdx.math.Vector2 touchPos = new com.badlogic.gdx.math.Vector2(
                Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);

        float tx = touchPos.x;
        float ty = touchPos.y;

        // Área aproximada de cada tab (ancho ~80px, alto ~20px desde su posición)
        if (estaEnArea(tx, ty, 62.1f, 148.5f, 70, 20)) {
            tabActual = Tab.RANKING;
        } else if (estaEnArea(tx, ty, 175f, 148.5f, 100, 20)) {
            tabActual = Tab.SOLICITUDES;
        } else if (estaEnArea(tx, ty, 350f, 148.5f, 80, 20)) {
            tabActual = Tab.PARTIDAS;
        } else if (estaEnArea(tx, ty, 473.4f, 148.5f, 80, 20)) {
            tabActual = Tab.COMPARAR;
        }
    }

    private boolean estaEnArea(float tx, float ty, float x, float y, float w, float h) {
        float yLib = 550 - y - h;
        return tx >= x && tx <= x + w && ty >= yLib && ty <= yLib + h;
    }

    // ============ TAB PARTIDAS ============
    private void dibujarPartidas() {
        Usuario u = juego.getUsuarioActual();
        if (u == null) {
            return;
        }

        fuente.setColor(COLOR_NORMAL);
        float x = 80f;
        float y = 200f;
        float espaciado = 30f;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        fuente.draw(batch, "PARTIDAS JUGADAS - " + u.getPartidasJugadas(),
                x, 550 - y);
        fuente.draw(batch, "FECHA DE REGISTRO - " + sdf.format(u.getFechaRegistro()),
                x, 550 - (y + espaciado));
        fuente.draw(batch, "ULTIMA SESION - " + sdf.format(u.getUltimaSesion()),
                x, 550 - (y + espaciado * 2));
        fuente.draw(batch, "NIVELES COMPLETADOS - " + u.getNivelesCompletados(),
                x, 550 - (y + espaciado * 3));
        fuente.draw(batch, "TIEMPO TOTAL JUGADO - " + formatearTiempo(u.getTiempoTotalJugado()),
                x, 550 - (y + espaciado * 4));
        fuente.draw(batch, "TIEMPO PROMEDIO/NIVEL - " + formatearTiempo(u.getTiempoPromedioPorNivel()),
                x, 550 - (y + espaciado * 5));

        // Intentos por nivel
        StringBuilder intentos = new StringBuilder("INTENTOS POR NIVEL - ");
        int[] arr = u.getIntentosPorNivel();
        for (int i = 1; i < arr.length; i++) {
            intentos.append("N").append(i).append(":").append(arr[i]);
            if (i < arr.length - 1) {
                intentos.append(" ");
            }
        }
        fuente.draw(batch, intentos.toString(), x, 550 - (y + espaciado * 6));
    }

    // ============ TAB RANKING ============
    private void dibujarRanking() {
        fuente.setColor(COLOR_NORMAL);
        fuente.draw(batch, "RANKING - PROXIMAMENTE", 80f, 550 - 200f);
    }

    // ============ TAB SOLICITUDES ============
    private void dibujarSolicitudes() {
        fuente.setColor(COLOR_NORMAL);
        fuente.draw(batch, "SOLICITUDES - PROXIMAMENTE", 80f, 550 - 200f);
    }

    // ============ TAB COMPARAR ============
    private void dibujarComparar() {
        fuente.setColor(COLOR_NORMAL);
        fuente.draw(batch, "COMPARAR - PROXIMAMENTE", 80f, 550 - 200f);
    }

    private String formatearTiempo(long segundos) {
        long min = segundos / 60;
        long seg = segundos % 60;
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
        texFondo.dispose();
        texExit.dispose();
        texVolumen.dispose();
        texPixel.dispose();
        fuente.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
