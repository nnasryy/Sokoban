/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;
import com.sokoban.game.usuarios.GestorUsuarios;
import com.sokoban.game.usuarios.Usuario;
import java.text.SimpleDateFormat;
import java.util.List;
import com.sokoban.game.GestorMusica;
public class PantallaStats extends PantallaBase {

    private Texture texFondo, texExit, texPixel;
    private BitmapFont fuenteTab;
    private BitmapFont fuenteDatos;
    private Stage stage;

    private enum Tab {
        RANKING, PARTIDAS, SOLICITUDES, COMPARAR
    }

    private Tab tabActual = Tab.PARTIDAS;
    private static final Color COLOR_FONDO_CUADRO = new Color(208f / 255f, 104f / 255f, 63f / 255f, 1f);
    private static final Color COLOR_BORDE = new Color(70f / 255f, 39f / 255f, 35f / 255f, 1f);
    private static final Color COLOR_TAB_NORMAL = new Color(117f / 255f, 58f / 255f, 50f / 255f, 1f);
    private static final Color COLOR_TAB_ACTIVO = new Color(70f / 255f, 39f / 255f, 35f / 255f, 1f);
    private static final Color COLOR_DATOS = new Color(70f / 255f, 39f / 255f, 35f / 255f, 1f);
    private static final Color COLOR_VERDE = new Color(0f / 255f, 130f / 255f, 0f / 255f, 1f);
    private static final Color COLOR_ROJO = new Color(180f / 255f, 0f / 255f, 0f / 255f, 1f);
    private static final float CX = 37.8f, CY_TOP = 127.2f, CW = 577.3f, CH = 343.2f;
    private static final float IX = 64.8f, IY_TOP = 187.6f, IW = 523.5f, IH = 271.4f;
    private static final float[] TAB_X = {62f, 170f, 340f, 464f};
    private static final float[] TAB_W = {90f, 110f, 85f, 85f};
    private static final float TAB_Y_TOP = 148.5f;
    private static final float TAB_H = 22f;
    private TextField campoBusqueda;
    private String mensajeSolicitud = "";
    private Color colorMensaje = COLOR_DATOS;
    private String amigoSeleccionado = null;
    private boolean ingles;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Texture texVolumenOn, texVolumenOff;

    public PantallaStats(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/FondoStats.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        fuenteTab = new BitmapFont(Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuenteTab.getData().setScale(1f);

        fuenteDatos = new BitmapFont(Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuenteDatos.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estilo = crearEstiloTextField();
        campoBusqueda = new TextField("", estilo);
        campoBusqueda.setMessageText(ingles ? "SEARCH USER..." : "BUSCAR USUARIO...");
        campoBusqueda.setBounds(IX + 10f, 550 - IY_TOP - 50f, IW - 130f, 36f);
        campoBusqueda.setVisible(false);
        stage.addActor(campoBusqueda);

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
        stage.addActor(btnExit);
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
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        manejarClickTabs();
        campoBusqueda.setVisible(tabActual == Tab.SOLICITUDES);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(texFondo, 0, 0, 650, 550);
        dibujarRect(CX, CY_TOP, CW, CH);
        dibujarRect(IX, IY_TOP, IW, IH);

        String[] labels = ingles
                ? new String[]{"RANKING", "REQUESTS", "MATCHES", "COMPARE"}
                : new String[]{"RANKING", "SOLICITUDES", "PARTIDAS", "COMPARAR"};
        Tab[] tabs = {Tab.RANKING, Tab.SOLICITUDES, Tab.PARTIDAS, Tab.COMPARAR};

        for (int i = 0; i < 4; i++) {
            boolean activo = tabActual == tabs[i];
            fuenteTab.setColor(activo ? COLOR_TAB_ACTIVO : COLOR_TAB_NORMAL);
            fuenteTab.draw(batch, labels[i], TAB_X[i], 550 - TAB_Y_TOP);
            if (activo) {
                fuenteTab.draw(batch, labels[i], TAB_X[i] + 0.5f, 550 - TAB_Y_TOP);
            }
        }

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

    private void dibujarPartidas() {
        Usuario u = juego.getUsuarioActual();
        if (u == null) {
            return;
        }

        float x = IX + 15f;
        float yIni = IY_TOP + 22f;
        float esp = 30f;

        fuenteDatos.setColor(COLOR_DATOS);

        String[] lineas = ingles ? new String[]{
            "MATCHES PLAYED:        " + u.getPartidasJugadas(),
            "REGISTRATION DATE:     " + sdf.format(u.getFechaRegistro()),
            "LAST SESSION:          " + sdf.format(u.getUltimaSesion()),
            "LEVELS COMPLETED:      " + u.getNivelesCompletados(),
            "TOTAL TIME PLAYED:     " + formatearTiempo(u.getTiempoTotalJugado()),
            "AVG TIME/LEVEL:        " + formatearTiempo(u.getTiempoPromedioPorNivel()),
            "ATTEMPTS/LEVEL:        " + formatearIntentos(u.getIntentosPorNivel())
        } : new String[]{
            "PARTIDAS JUGADAS:      " + u.getPartidasJugadas(),
            "FECHA DE REGISTRO:     " + sdf.format(u.getFechaRegistro()),
            "ULTIMA SESION:         " + sdf.format(u.getUltimaSesion()),
            "NIVELES COMPLETADOS:   " + u.getNivelesCompletados(),
            "TIEMPO TOTAL JUGADO:   " + formatearTiempo(u.getTiempoTotalJugado()),
            "TIEMPO PROMEDIO/NIVEL: " + formatearTiempo(u.getTiempoPromedioPorNivel()),
            "INTENTOS POR NIVEL:    " + formatearIntentos(u.getIntentosPorNivel())
        };

        for (int i = 0; i < lineas.length; i++) {
            fuenteDatos.draw(batch, lineas[i], x, 550 - (yIni + esp * i));
        }
    }

    private void dibujarRanking() {
        List<Usuario> ranking = GestorUsuarios.getRanking();

        float x = IX + 10f;
        float yIni = IY_TOP + 15f;
        float esp = 26f;

        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                ingles ? "#   USER               LEVELS    TOTAL TIME"
                        : "#   USUARIO            NIVELES   TIEMPO TOTAL",
                x, 550 - yIni);
        dibujarLinea(x, yIni + 18f, IW - 15f);

        int max = Math.min(ranking.size(), 7);
        for (int i = 0; i < max; i++) {
            Usuario u = ranking.get(i);
            boolean esYo = juego.getUsuarioActual() != null
                    && u.getUsername().equals(juego.getUsuarioActual().getUsername());

            fuenteDatos.setColor(esYo ? COLOR_VERDE : COLOR_DATOS);

            String linea = String.format("%-3d %-20s %-9d %s",
                    i + 1,
                    truncar(u.getUsername(), 18),
                    u.getNivelesCompletados(),
                    formatearTiempo(u.getTiempoTotalJugado()));

            fuenteDatos.draw(batch, linea, x, 550 - (yIni + esp * (i + 1) + 5f));
        }

        if (ranking.isEmpty()) {
            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch,
                    ingles ? "No players registered yet."
                            : "No hay jugadores registrados aun.",
                    x, 550 - (yIni + esp));
        }
    }

    private void dibujarSolicitudes() {
        Usuario u = juego.getUsuarioActual();
        if (u == null) {
            return;
        }

        float x = IX + 10f;
        float yIni = IY_TOP + 15f;
        float esp = 24f;

        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                ingles ? "SEARCH AND ADD FRIEND:" : "BUSCAR Y AGREGAR AMIGO:",
                x, 550 - yIni);

        float btnEnviarX = IX + IW - 110f;
        float btnEnviarY = IY_TOP + 42f;
        dibujarBotonTexto(ingles ? "SEND" : "ENVIAR", btnEnviarX, btnEnviarY);

        if (Gdx.input.justTouched()) {
            Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(pos);
            if (estaEnArea(pos.x, pos.y, btnEnviarX, btnEnviarY, 70f, 20f)) {
                enviarSolicitud();
            }
        }

        if (!mensajeSolicitud.isEmpty()) {
            fuenteDatos.setColor(colorMensaje);
            fuenteDatos.draw(batch, mensajeSolicitud, x, 550 - (yIni + esp * 2f + 5f));
        }

        float ySol = yIni + esp * 3.5f;
        List<String> solicitudes = u.getSolicitudesPendientes();

        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                (ingles ? "RECEIVED REQUESTS (" : "SOLICITUDES RECIBIDAS (")
                + solicitudes.size() + "):",
                x, 550 - ySol);

        if (solicitudes.isEmpty()) {
            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch,
                    ingles ? "  No pending requests." : "  Sin solicitudes pendientes.",
                    x, 550 - (ySol + esp));
        } else {
            int maxSol = Math.min(solicitudes.size(), 2);
            for (int i = 0; i < maxSol; i++) {
                fuenteDatos.setColor(COLOR_DATOS);
                fuenteDatos.draw(batch, "  @" + solicitudes.get(i),
                        x, 550 - (ySol + esp * (i + 1)));

                float yBtnSol = ySol + esp * (i + 1);
                dibujarBotonTexto(ingles ? "ACCEPT" : "ACEPTAR", x + 180f, yBtnSol);
                dibujarBotonTexto(ingles ? "DECLINE" : "RECHAZAR", x + 290f, yBtnSol);

                final String de = solicitudes.get(i);
                if (Gdx.input.justTouched()) {
                    Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                    viewport.unproject(pos);
                    if (estaEnArea(pos.x, pos.y, x + 180f, yBtnSol, 70f, 20f)) {
                        GestorUsuarios.aceptarSolicitud(u, de);
                        mensajeSolicitud = ingles
                                ? "@" + de + " is now your friend!"
                                : "@" + de + " ahora es tu amigo!";
                        colorMensaje = COLOR_VERDE;
                    } else if (estaEnArea(pos.x, pos.y, x + 290f, yBtnSol, 80f, 20f)) {
                        GestorUsuarios.rechazarSolicitud(u, de);
                        mensajeSolicitud = ingles
                                ? "Request from @" + de + " declined."
                                : "Solicitud de @" + de + " rechazada.";
                        colorMensaje = COLOR_ROJO;
                    }
                }
            }
        }

        float yAm = ySol + esp * 4f;
        List<String> amigos = u.getAmigos();

        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                (ingles ? "FRIENDS (" : "AMIGOS (") + amigos.size() + "):",
                x, 550 - yAm);

        if (amigos.isEmpty()) {
            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch,
                    ingles ? "  No friends yet." : "  Sin amigos aun.",
                    x, 550 - (yAm + esp));
        } else {
            fuenteDatos.setColor(COLOR_DATOS);
            int maxAm = Math.min(amigos.size(), 3);
            for (int i = 0; i < maxAm; i++) {
                fuenteDatos.draw(batch, "  @" + amigos.get(i),
                        x, 550 - (yAm + esp * (i + 1)));
            }
            if (amigos.size() > 3) {
                fuenteDatos.draw(batch,
                        "  ... " + (ingles ? "and " : "y ") + (amigos.size() - 3)
                        + (ingles ? " more." : " mas."),
                        x, 550 - (yAm + esp * 4));
            }
        }
    }

    private void dibujarComparar() {
        Usuario yo = juego.getUsuarioActual();
        if (yo == null) {
            return;
        }

        float x = IX + 10f;
        float yIni = IY_TOP + 15f;
        float esp = 26f;

        List<String> amigos = yo.getAmigos();

        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                ingles ? "SELECT A FRIEND:" : "SELECCIONA UN AMIGO:",
                x, 550 - yIni);

        if (amigos.isEmpty()) {
            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch,
                    ingles ? "No friends yet. Add from REQUESTS tab."
                            : "No tienes amigos. Agrega desde SOLICITUDES.",
                    x, 550 - (yIni + esp));
            return;
        }

        float xBtn = x;
        float yBtn = yIni + esp;
        int maxAmigos = Math.min(amigos.size(), 4);

        for (int i = 0; i < maxAmigos; i++) {
            String amigo = amigos.get(i);
            boolean seleccionado = amigo.equals(amigoSeleccionado);

            if (seleccionado) {
                batch.setColor(COLOR_BORDE);
                batch.draw(texPixel, xBtn - 3f, 550 - yBtn - 3f, 90f, 22f);
                batch.setColor(Color.WHITE);
                fuenteDatos.setColor(Color.WHITE);
            } else {
                fuenteDatos.setColor(COLOR_DATOS);
            }

            fuenteDatos.draw(batch, "@" + truncar(amigo, 8), xBtn, 550 - yBtn);

            final String nombreAmigo = amigo;
            if (Gdx.input.justTouched()) {
                Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                viewport.unproject(pos);
                if (estaEnArea(pos.x, pos.y, xBtn, yBtn, 90f, 20f)) {
                    amigoSeleccionado = nombreAmigo;
                }
            }

            xBtn += 100f;
        }

        if (amigoSeleccionado == null) {
            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch,
                    ingles ? "Press a friend to compare."
                            : "Presiona un amigo para comparar.",
                    x, 550 - (yIni + esp * 2.5f));
            return;
        }

        Usuario ellos = GestorUsuarios.cargarUsuario(amigoSeleccionado);
        if (ellos == null) {
            fuenteDatos.setColor(COLOR_ROJO);
            fuenteDatos.draw(batch,
                    (ingles ? "Could not load data for @" : "No se pudo cargar datos de @")
                    + amigoSeleccionado,
                    x, 550 - (yIni + esp * 2.5f));
            return;
        }

        float yTabla = yIni + esp * 2.5f;
        fuenteDatos.setColor(COLOR_BORDE);
        fuenteDatos.draw(batch,
                String.format("%-22s %-14s %s",
                        ingles ? "STAT" : "ESTADISTICA",
                        ingles ? "YOU" : "TU",
                        "@" + truncar(amigoSeleccionado, 10)),
                x, 550 - yTabla);
        dibujarLinea(x, yTabla + 18f, IW - 15f);

        String[][] filas = ingles ? new String[][]{
            {"MATCHES PLAYED", String.valueOf(yo.getPartidasJugadas()), String.valueOf(ellos.getPartidasJugadas())},
            {"LEVELS COMPLET.", String.valueOf(yo.getNivelesCompletados()), String.valueOf(ellos.getNivelesCompletados())},
            {"TOTAL TIME", formatearTiempo(yo.getTiempoTotalJugado()), formatearTiempo(ellos.getTiempoTotalJugado())},
            {"AVG TIME/LEVEL", formatearTiempo(yo.getTiempoPromedioPorNivel()), formatearTiempo(ellos.getTiempoPromedioPorNivel())}
        } : new String[][]{
            {"PARTIDAS JUGADAS", String.valueOf(yo.getPartidasJugadas()), String.valueOf(ellos.getPartidasJugadas())},
            {"NIVELES COMPLET.", String.valueOf(yo.getNivelesCompletados()), String.valueOf(ellos.getNivelesCompletados())},
            {"TIEMPO TOTAL", formatearTiempo(yo.getTiempoTotalJugado()), formatearTiempo(ellos.getTiempoTotalJugado())},
            {"TIEMPO PROM/NIV", formatearTiempo(yo.getTiempoPromedioPorNivel()), formatearTiempo(ellos.getTiempoPromedioPorNivel())}
        };

        int[] yoNums = {yo.getPartidasJugadas(), yo.getNivelesCompletados()};
        int[] ellosNums = {ellos.getPartidasJugadas(), ellos.getNivelesCompletados()};

        for (int i = 0; i < filas.length; i++) {
            boolean yoGana = i < 2
                    ? yoNums[i] >= ellosNums[i]
                    : yo.getTiempoTotalJugado() <= ellos.getTiempoTotalJugado();

            float yFila = yTabla + esp * (i + 1) + 5f;

            fuenteDatos.setColor(COLOR_DATOS);
            fuenteDatos.draw(batch, String.format("%-22s", filas[i][0]), x, 550 - yFila);

            fuenteDatos.setColor(yoGana ? COLOR_VERDE : COLOR_ROJO);
            fuenteDatos.draw(batch, String.format("%-14s", filas[i][1]), x + 200f, 550 - yFila);

            fuenteDatos.setColor(yoGana ? COLOR_ROJO : COLOR_VERDE);
            fuenteDatos.draw(batch, filas[i][2], x + 320f, 550 - yFila);
        }

        float yVeredicto = yTabla + esp * 5.5f;
        boolean yoMejor = yo.getNivelesCompletados() >= ellos.getNivelesCompletados();
        fuenteDatos.setColor(yoMejor ? COLOR_VERDE : COLOR_DATOS);
        fuenteDatos.draw(batch,
                yoMejor
                        ? (ingles ? "You're ahead of @" : "Vas ganando a @")
                        + truncar(amigoSeleccionado, 10)
                        + (ingles ? "! Keep it up." : "! Sigue asi.")
                        : (ingles ? "@" : "@") + truncar(amigoSeleccionado, 10)
                        + (ingles ? " has the lead. Play more!" : " te lleva ventaja. A jugar mas!"),
                x, 550 - yVeredicto);
    }

    private void enviarSolicitud() {
        String destino = campoBusqueda.getText().trim();
        if (destino.isEmpty()) {
            mensajeSolicitud = ingles ? "Enter a username first." : "Escribe un username primero.";
            colorMensaje = COLOR_ROJO;
            return;
        }
        String miUser = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getUsername() : "";
        if (destino.equals(miUser)) {
            mensajeSolicitud = ingles ? "You can't add yourself." : "No puedes agregarte a ti mismo.";
            colorMensaje = COLOR_ROJO;
            return;
        }
        boolean ok = GestorUsuarios.enviarSolicitud(miUser, destino);
        if (ok) {
            mensajeSolicitud = (ingles ? "Request sent to @" : "Solicitud enviada a @") + destino + "!";
            colorMensaje = COLOR_VERDE;
            campoBusqueda.setText("");
        } else {
            mensajeSolicitud = ingles ? "User not found or already a friend." : "Usuario no encontrado o ya es amigo.";
            colorMensaje = COLOR_ROJO;
        }
    }

    private void dibujarBotonTexto(String texto, float x, float yTop) {
        float y = 550 - yTop;
        batch.setColor(COLOR_BORDE);
        batch.draw(texPixel, x - 2f, y - 16f, getTextWidth(texto) + 6f, 20f);
        batch.setColor(Color.WHITE);
        fuenteDatos.setColor(Color.WHITE);
        fuenteDatos.draw(batch, texto, x, y);
    }

    private float getTextWidth(String texto) {
        return texto.length() * 8f;
    }

    private void dibujarRect(float x, float yTop, float w, float h) {
        float y = 550 - yTop - h;
        batch.setColor(COLOR_FONDO_CUADRO);
        batch.draw(texPixel, x + 2, y + 2, w - 4, h - 4);
        batch.setColor(COLOR_BORDE);
        batch.draw(texPixel, x, y, w, 2);
        batch.draw(texPixel, x, y + h - 2, w, 2);
        batch.draw(texPixel, x, y, 2, h);
        batch.draw(texPixel, x + w - 2, y, 2, h);
        batch.setColor(Color.WHITE);
    }

    private void dibujarLinea(float x, float yTop, float ancho) {
        batch.setColor(COLOR_BORDE);
        batch.draw(texPixel, x, 550 - yTop, ancho, 1.5f);
        batch.setColor(Color.WHITE);
    }

    private void manejarClickTabs() {
        if (!Gdx.input.justTouched()) {
            return;
        }
        Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(pos);
        Tab[] tabs = {Tab.RANKING, Tab.SOLICITUDES, Tab.PARTIDAS, Tab.COMPARAR};
        for (int i = 0; i < 4; i++) {
            if (estaEnArea(pos.x, pos.y, TAB_X[i], TAB_Y_TOP, TAB_W[i], TAB_H)) {
                tabActual = tabs[i];
                mensajeSolicitud = "";
                amigoSeleccionado = null;
                break;
            }
        }
    }

    private boolean estaEnArea(float tx, float ty, float x, float yTop, float w, float h) {
        float yLib = 550 - yTop - h;
        return tx >= x && tx <= x + w && ty >= yLib && ty <= yLib + h;
    }

    private String formatearTiempo(long segundos) {
        long min = segundos / 60;
        long seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }

   private String formatearIntentos(int[] arr) {
    if (arr == null) return ingles ? "N/A" : "N/D";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Math.min(arr.length, 5); i++) {
        sb.append(ingles ? "L" : "N").append(i + 1)
          .append(":").append(arr[i]);
        if (i < 4) sb.append("  ");
    }
    return sb.toString();
}

    private String truncar(String s, int max) {
        return s != null && s.length() > max ? s.substring(0, max) : s;
    }

    private TextField.TextFieldStyle crearEstiloTextField() {
        TextField.TextFieldStyle estilo = new TextField.TextFieldStyle();
        estilo.font = fuenteDatos;
        estilo.fontColor = COLOR_BORDE;
        estilo.messageFontColor = new Color(COLOR_BORDE.r, COLOR_BORDE.g, COLOR_BORDE.b, 0.6f);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(COLOR_FONDO_CUADRO);
        pixmap.fill();
        estilo.background = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Pixmap cursor = new Pixmap(2, 30, Pixmap.Format.RGBA8888);
        cursor.setColor(COLOR_BORDE);
        cursor.fill();
        estilo.cursor = new TextureRegionDrawable(new Texture(cursor));
        cursor.dispose();

        return estilo;
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
        if (texPixel != null) {
            texPixel.dispose();
        }
        if (fuenteTab != null) {
            fuenteTab.dispose();
        }
        if (fuenteDatos != null) {
            fuenteDatos.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
