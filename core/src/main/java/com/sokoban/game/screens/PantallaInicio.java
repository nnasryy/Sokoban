package com.sokoban.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;

public class PantallaInicio extends PantallaBase {

    // Texturas
    private Texture texFondo;
    private Texture texNube1, texNube2;
    private Texture texSignUp, texLogin, texExit, texVolumen;

    // Stage para los botones
    private Stage stage;

    // Animación nubes
    private float tiempo = 0f;

    // Estado volumen
    private boolean musicaActiva = true;

    public PantallaInicio(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        // Ajustar tamaño ventana
       Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        // Cargar imágenes
        texFondo    = new Texture("imagenes/fondos/Menuinicio.png");
        texSignUp   = new Texture("imagenes/botones/signup_button.png");
        texLogin    = new Texture("imagenes/botones/login_button.png");
        texExit     = new Texture("imagenes/botones/exit_button.png");
        texVolumen  = new Texture("imagenes/botones/volume_button.png");

        // Stage
        stage = new Stage(new FitViewport(
            SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        // Crear botones con sus posiciones
        ImageButton btnSignUp  = crearBoton(texSignUp,  126, 210);
        ImageButton btnLogin   = crearBoton(texLogin,   339, 210);
        ImageButton btnExit    = crearBoton(texExit,    265, 135);
        ImageButton btnVolumen = crearBoton(texVolumen, 589, 490);

        // Acciones
        btnSignUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaRegistro(juego));
            }
        });

        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaLogin(juego));
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        });

        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                musicaActiva = !musicaActiva;
                // Aquí después controlarás la música
            }
        });

        stage.addActor(btnSignUp);
        stage.addActor(btnLogin);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private ImageButton crearBoton(Texture tex, float x, float y) {
        ImageButton btn = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(tex))
        );
        btn.setPosition(x, y);
        return btn;
    }

    @Override
    public void render(float delta) {
        tiempo += delta;

        // Animación nubes — movimiento suave
        float offsetNube1 = (float) Math.sin(tiempo * 1.2f) * 6f;
        float offsetNube2 = (float) Math.sin(tiempo * 1.2f + 1.5f) * 6f;

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
            // Fondo completo
            batch.draw(texFondo, 0, 0, 650, 550);
        batch.end();

        // Botones encima
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        texFondo.dispose();
        texSignUp.dispose();
        texLogin.dispose();
        texExit.dispose();
        texVolumen.dispose();
        stage.dispose();
    }
}