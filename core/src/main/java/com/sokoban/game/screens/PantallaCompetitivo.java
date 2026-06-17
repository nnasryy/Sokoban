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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;
import com.sokoban.game.GestorMusica;

public class PantallaCompetitivo extends PantallaBase {

    private Texture texFondo, texExit, texPlayCompetitivo, texCandado;
    private Texture texNivel1, texNivel2, texNivel3, texNivel4;
    private Texture texPixel;
    private BitmapFont fuenteTitulo;
    private BitmapFont fuente18;
    private Stage stage;
    private TextField campoAmigo;
    private Texture texVolumenOn, texVolumenOff;

    private int nivelSeleccionado = -1;

    private static final float[] XS = {78.2f, 170.3f, 271f, 376.4f, 477.9f};
    private static final float Y_NIVEL = 318.3f;
    private static final float ANCHO_BLOQUE = 90f;
    private static final float ALTO_BLOQUE = 100f;

    public PantallaCompetitivo(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        texFondo = new Texture("imagenes/fondos/FondoAzul.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        boolean ingles = juego.getUsuarioActual() != null && "en".equals(juego.getUsuarioActual().getIdioma());
        texPlayCompetitivo = new Texture(ingles ? "imagenes/botones/PlayCompetitivo.png" : "imagenes/botones/JugarCompetitivo.png");
        texCandado = new Texture("imagenes/botones/Candado.png");

        texNivel1 = new Texture("imagenes/botones/Nivel1Bloque.png");
        texNivel2 = new Texture("imagenes/botones/Nivel2Bloque.png");
        texNivel3 = new Texture("imagenes/botones/Nivel3Bloque.png");
        texNivel4 = new Texture("imagenes/botones/Nivel4Bloque.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        fuenteTitulo = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);

        fuente18 = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari27.fnt"));
        fuente18.getData().setScale(1f);

        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estilo = crearEstiloTextField();
        campoAmigo = new TextField("", estilo);
        campoAmigo.setMessageText(ingles ? "FRIEND'S USERNAME" : "USER DE AMIGO A COMPETIR");
        campoAmigo.setBounds(64.8f, 550 - 220f - 40f, 520f, 40f);

        int nivelesDesbloqueados = juego.getUsuarioActual() != null
                ? juego.getUsuarioActual().getNivelesDesbloqueados() : 1;

        Texture[] texturas = {texNivel1, texNivel2, texNivel3, texNivel4, texNivel1};

        for (int i = 0; i < 5; i++) {
            final int numeroNivel = i;

            ImageButton btnNivel = new ImageButton(
                    new TextureRegionDrawable(new TextureRegion(texturas[i])));
            btnNivel.setSize(ANCHO_BLOQUE, ALTO_BLOQUE);
            btnNivel.setPosition(XS[i], 550 - Y_NIVEL - ALTO_BLOQUE);

            boolean desbloqueado = (numeroNivel + 1) <= nivelesDesbloqueados;

            if (desbloqueado) {
                btnNivel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        nivelSeleccionado = numeroNivel;
                    }
                });
            }

            stage.addActor(btnNivel);

            if (!desbloqueado) {
                ImageButton candado = new ImageButton(
                        new TextureRegionDrawable(new TextureRegion(texCandado)));
                candado.setSize(ANCHO_BLOQUE, ALTO_BLOQUE);
                candado.setPosition(XS[i], 550 - Y_NIVEL - ALTO_BLOQUE);
                candado.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
                stage.addActor(candado);
            }
        }

        ImageButton btnPlay = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texPlayCompetitivo)));
        btnPlay.setSize(223.7f, 76.7f);
        btnPlay.setPosition(198.7f, 550 - 429.8f - 76.7f);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                iniciarCompetencia();
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(41.2f, 550 - 468.2f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaSeleccionNivel(juego));
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
        stage.addActor(campoAmigo);
        stage.addActor(btnPlay);
        stage.addActor(btnExit);
    }

    private void iniciarCompetencia() {
        String amigoUsername = campoAmigo.getText().trim();

        if (amigoUsername.isEmpty() || nivelSeleccionado == -1) {
            boolean ingles2 = juego.getUsuarioActual() != null
                    && "en".equals(juego.getUsuarioActual().getIdioma());
            String msgAdv = ingles2
                    ? "Select a friend and a level"
                    : "Selecciona un amigo y un nivel";
            juego.setScreen(new PantallaAdvertencia(juego, msgAdv,
                    new PantallaCompetitivo(juego)));
            return;
        }

        boolean esAmigo = juego.getUsuarioActual() != null
                && juego.getUsuarioActual().getAmigos().contains(amigoUsername);

        if (!esAmigo) {
            boolean ingles3 = juego.getUsuarioActual() != null
                    && "en".equals(juego.getUsuarioActual().getIdioma());
            String msgAmigo = ingles3
                    ? "That user is not in your friends list"
                    : "Ese usuario no esta en tu lista de amigos";
            juego.setScreen(new PantallaAdvertencia(juego, msgAmigo,
                    new PantallaCompetitivo(juego)));
            return;
        }

        String nombreJ1 = juego.getUsuarioActual().getUsername();
        String nombreJ2 = amigoUsername;

        boolean ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());
        String msg = ingles
                ? "Player 1: " + nombreJ1 + "\nYour turn! Good luck."
                : "Jugador 1: " + nombreJ1 + "\nEs tu turno! Buena suerte.";

        juego.setScreen(new PantallaAdvertencia(juego, msg,
                new PantallaJuego(juego, nivelSeleccionado,
                        nombreJ1, nombreJ2, 1, -1)));
    }

    private TextField.TextFieldStyle crearEstiloTextField() {
        TextField.TextFieldStyle estilo = new TextField.TextFieldStyle();
        estilo.font = fuente18;
        Color colorTexto = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);
        estilo.fontColor = colorTexto;
        estilo.messageFontColor = colorTexto;

        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(208f / 255f, 104f / 255f, 63f / 255f, 1f);
        pixmap.fill();
        estilo.background = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        com.badlogic.gdx.graphics.Pixmap cursorMap = new com.badlogic.gdx.graphics.Pixmap(
                2, 40, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        cursorMap.setColor(colorTexto);
        cursorMap.fill();
        estilo.cursor = new TextureRegionDrawable(new Texture(cursorMap));
        cursorMap.dispose();

        return estilo;
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

        fuenteTitulo.setColor(Color.WHITE);
        String titulo = obtenerTitulo();
        GlyphLayout layoutTitulo = new GlyphLayout(fuenteTitulo, titulo);
        float xTitulo = (650f - layoutTitulo.width) / 2f;
        fuenteTitulo.draw(batch, titulo, xTitulo, 550 - 84.9f);

        Color colorBorde = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);
        batch.setColor(colorBorde);
        float fx = 64.8f, fy = 550 - 220f - 40f, fw = 520f, fh = 40f;
        batch.draw(texPixel, fx - 4, fy - 4, fw + 8, 4);
        batch.draw(texPixel, fx - 4, fy + fh, fw + 8, 4);
        batch.draw(texPixel, fx - 4, fy - 4, 4, fh + 8);
        batch.draw(texPixel, fx + fw, fy - 4, 4, fh + 8);
        batch.setColor(Color.WHITE);

        fuente18.setColor(colorBorde);
        String textoSel = obtenerTextoSelecciona();
        GlyphLayout layoutSel = new GlyphLayout(fuente18, textoSel);
        float xSel = (650f - layoutSel.width) / 2f;
        fuente18.draw(batch, textoSel, xSel, 550 - 290f);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private String obtenerTitulo() {
        if (juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma())) {
            return "COMPETITIVE";
        }
        return "COMPETITIVO";
    }

    private String obtenerTextoSelecciona() {
        if (juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma())) {
            return "SELECT THE AVAILABLE LEVEL:";
        }
        return "SELECCIONA EL NIVEL DISPONIBLE:";
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
        texPlayCompetitivo.dispose();
        texCandado.dispose();
        texNivel1.dispose();
        texNivel2.dispose();
        texNivel3.dispose();
        texNivel4.dispose();
        texPixel.dispose();
        if (texVolumenOn != null) {
            texVolumenOn.dispose();
        }
        if (texVolumenOff != null) {
            texVolumenOff.dispose();
        }
        fuenteTitulo.dispose();
        fuente18.dispose();
        if (stage != null) {
            stage.dispose();
        }
    }
}
