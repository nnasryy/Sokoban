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
import com.sokoban.game.usuarios.ValidadorContrasena;

public class PantallaRegistro extends PantallaBase {

    private Texture texFondo, texExit, texBotonSignUp, texVolumen;
    private Texture texOjoAbierto, texOjoCerrado;
    private Stage stage;
    private TextField campoNombre, campoUsername, campoPassword;
    private BitmapFont fuentePixel;
    private boolean mostrarPassword = false;
    private boolean musicaActiva = true;
    private String mensajeError = "";
    private Color colorEspecial = Color.RED;
    private Color colorNumero = Color.RED;
    private Color colorCaract = Color.RED;

    public PantallaRegistro(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
        fuentePixel = new BitmapFont(
                Gdx.files.internal("fuentes/Pixellari.fnt"));
        texFondo = new Texture("imagenes/fondos/SignUp.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texBotonSignUp = new Texture("imagenes/botones/signup_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");
        texOjoAbierto = new Texture("imagenes/botones/OpenedEye.png");
        texOjoCerrado = new Texture("imagenes/botones/ClosedEye.png");

        stage = new Stage(new FitViewport(
                SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estiloField = crearEstiloTextField();

        campoNombre = new TextField("", estiloField);
        campoNombre.setMessageText("NOMBRE COMPLETO");
        campoNombre.setBounds(148, 550 - 189 - 44, 312, 44);

        campoUsername = new TextField("", estiloField);
        campoUsername.setMessageText("USERNAME");
        campoUsername.setBounds(148, 550 - 249 - 44, 312, 44);

        campoPassword = new TextField("", estiloField);
        campoPassword.setMessageText("PASSWORD");
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        campoPassword.setBounds(148, 550 - 309 - 44, 312, 44);

        campoPassword.setTextFieldListener((field, c) -> {
            String pass = field.getText();
            colorEspecial = ValidadorContrasena
                    .tieneCaracterEspecial(pass) ? Color.GREEN : Color.RED;
            colorNumero = ValidadorContrasena
                    .tieneNumero(pass) ? Color.GREEN : Color.RED;
            colorCaract = (ValidadorContrasena.tieneMinCaracteres(pass)
                    && ValidadorContrasena.passwordLongitudValida(pass))
                    ? Color.GREEN : Color.RED;
        });

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

        ImageButton btnSignUp = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBotonSignUp)));
        btnSignUp.setBounds(325, 102, 150, 44);
        btnSignUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                registrar();
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

        stage.addActor(campoNombre);
        stage.addActor(campoUsername);
        stage.addActor(campoPassword);
        stage.addActor(btnOjo);
        stage.addActor(btnSignUp);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private void registrar() {
        String nombre = campoNombre.getText().trim();
        String username = campoUsername.getText().trim();
        String password = campoPassword.getText();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Completa todos los campos",
                    new PantallaRegistro(juego)));
            return;
        }
        if (!ValidadorContrasena.nombreLongitudValida(nombre)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "El nombre debe tener entre 3 y 8 caracteres",
                    new PantallaRegistro(juego)));
            return;
        }
        if (!ValidadorContrasena.usernameLongitudValida(username)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "El username debe tener entre 3 y 8 caracteres",
                    new PantallaRegistro(juego)));
            return;
        }
        if (!ValidadorContrasena.esValida(password)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "La contrasena no cumple los requisitos",
                    new PantallaRegistro(juego)));
            return;
        }
        if (GestorUsuarios.existeUsuario(username)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Este usuario ya existe",
                    new PantallaRegistro(juego)));
            return;
        }

        Usuario nuevo = new Usuario(username, password, nombre);
        boolean guardado = GestorUsuarios.registrarUsuario(nuevo);

        if (guardado) {
            juego.setUsuarioActual(nuevo);
            juego.setScreen(new PantallaSeleccionAvatar(juego, nuevo));
        } else {
            juego.setScreen(new PantallaAdvertencia(juego,
                    "Error al guardar usuario",
                    new PantallaRegistro(juego)));
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
        estilo.background = new TextureRegionDrawable(
                new Texture(pixmap));
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
            return;
        }
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);
        batch.end();

        batch.begin();
        fuentePixel.setColor(colorEspecial);
        fuentePixel.draw(batch, "CARACTERES ESPECIALES", 148, 550 - 363);

        fuentePixel.setColor(colorNumero);
        fuentePixel.draw(batch, "NUMERO", 336, 550 - 363);

        fuentePixel.setColor(colorCaract);
        fuentePixel.draw(batch, "5 CARACTERES", 429, 550 - 363);
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
        texExit.dispose();
        texBotonSignUp.dispose();
        texVolumen.dispose();
        texOjoAbierto.dispose();
        texOjoCerrado.dispose();
        stage.dispose();
        fuentePixel.dispose();
    }
}
