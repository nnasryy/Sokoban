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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.game.SokobanGame;
import com.sokoban.game.usuarios.GestorUsuarios;
import com.sokoban.game.usuarios.Usuario;

public class PantallaLogin extends PantallaBase {

    private Texture texFondo, texBtnLogin, texExit, texVolumen;
    private Texture texOjoAbierto, texOjoCerrado;
    private Stage stage;
    private TextField campoUsername, campoPassword;
    private BitmapFont fuentePixel;
    private boolean mostrarPassword = false;
    private boolean musicaActiva = true;

    private String mensajeError = "";

    public PantallaLogin(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        fuentePixel = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuentePixel.getData().setScale(1f);
        texFondo = new Texture("imagenes/fondos/LogIn.png");
        texBtnLogin = new Texture("imagenes/botones/login_button.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");
        texOjoAbierto = new Texture("imagenes/botones/OpenedEye.png");
        texOjoCerrado = new Texture("imagenes/botones/ClosedEye.png");

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estilo = crearEstiloTextField();

        campoUsername = new TextField("", estilo);
        campoUsername.setMessageText("USERNAME");
        campoUsername.setBounds(148, 550 - 249 - 44, 312, 44);

        campoPassword = new TextField("", estilo);
        campoPassword.setMessageText("PASSWORD");
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        campoPassword.setBounds(148, 550 - 309 - 44, 312, 44);

        ImageButton btnOjo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texOjoCerrado)));
        btnOjo.setBounds(472, 550 - 309 - 45, 50, 45);
        btnOjo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                mostrarPassword = !mostrarPassword;
                campoPassword.setPasswordMode(!mostrarPassword);
                btnOjo.getStyle().imageUp = new TextureRegionDrawable(
                        new TextureRegion(mostrarPassword
                                ? texOjoAbierto : texOjoCerrado));
            }
        });

        ImageButton btnLogin = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnLogin)));
        btnLogin.setBounds(325, 102, 150, 44);
        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                iniciarSesion();
            }
        });

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(177, 550 - 404 - 44, 120, 44);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaInicio(juego));
            }
        });

        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(589.7f, 550 - 10.8f - 50f, 46f, 50f);
        btnVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                musicaActiva = !musicaActiva;
            }
        });

        stage.addActor(campoUsername);
        stage.addActor(campoPassword);
        stage.addActor(btnOjo);
        stage.addActor(btnLogin);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private void iniciarSesion() {
        String username = campoUsername.getText().trim();
        String password = campoPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Completa todos los campos",
                    new PantallaLogin(juego)));
            return;
        }

        Usuario u = GestorUsuarios.login(username, password);
        if (u != null) {
            juego.setUsuarioActual(u);
            juego.setScreen(new PantallaMenu(juego));
        } else {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Usuario o contrasena incorrectos",
                    new PantallaLogin(juego)));
        }
    }

    private TextField.TextFieldStyle crearEstiloTextField() {
        TextField.TextFieldStyle estilo = new TextField.TextFieldStyle();
        estilo.font = fuentePixel;
        estilo.fontColor = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);
        estilo.messageFontColor = new Color(87f / 255f, 41f / 255f, 35f / 255f, 0.7f);

        com.badlogic.gdx.graphics.Pixmap pixmap
                = new com.badlogic.gdx.graphics.Pixmap(1, 1,
                        com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(208f / 255f, 104f / 255f, 63f / 255f, 1f);
        pixmap.fill();
        estilo.background = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        com.badlogic.gdx.graphics.Pixmap cursorMap
                = new com.badlogic.gdx.graphics.Pixmap(2, 44,
                        com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        cursorMap.setColor(Color.WHITE);
        cursorMap.fill();
        estilo.cursor = new TextureRegionDrawable(new Texture(cursorMap));
        cursorMap.dispose();

        return estilo;
    }

    @Override
    public void render(float delta) {
        if (texFondo == null || fuentePixel == null) {
            return; // ← agrega esto
        }
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);
        if (!mensajeError.isEmpty()) {
            fuentePixel.setColor(Color.RED);
            fuentePixel.draw(batch, mensajeError, 148, 550 - 430);
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
        texBtnLogin.dispose();
        texExit.dispose();
        texVolumen.dispose();
        texOjoAbierto.dispose();
        texOjoCerrado.dispose();
        stage.dispose();
        fuentePixel.dispose();
    }
}
