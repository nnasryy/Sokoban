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
import com.sokoban.game.GestorMusica;

public class PantallaInicio extends PantallaBase {

    private Texture texFondo;
    private Texture texNube1, texNube2;
    private Texture texSignUp, texLogin, texExit;
    private Stage stage;
    private float tiempo = 0f;
    private boolean musicaActiva = true;
    private Texture texVolumenOn, texVolumenOff;

    public PantallaInicio(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        // ← setWindowedMode va al FINAL, no al inicio

        texFondo = new Texture("imagenes/fondos/Menuinicio.png");
        texSignUp = new Texture("imagenes/botones/signup_button.png");
        texLogin = new Texture("imagenes/botones/login_button.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnSignUp = crearBoton(texSignUp, 126, 210);
        ImageButton btnLogin = crearBoton(texLogin, 339, 210);
        ImageButton btnExit = crearBoton(texExit, 265, 135);

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

        stage.addActor(btnSignUp);
        stage.addActor(btnLogin);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);

        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI); // ← al final
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return; // ← guard por si render llega antes que show termine
        }
        tiempo += delta;

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);
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
        texSignUp.dispose();
        texLogin.dispose();
        texExit.dispose();
        if (texVolumenOn != null) {
            texVolumenOn.dispose();
        }
        if (texVolumenOff != null) {
            texVolumenOff.dispose();
        }
        stage.dispose();
    }

    private ImageButton crearBoton(Texture tex, float x, float y) {
        ImageButton btn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(tex))
        );
        btn.setPosition(x, y);
        return btn;
    }
}
