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
import com.sokoban.game.usuarios.ValidadorContrasena;

public class PantallaMisDatos extends PantallaBase {

    private Texture texFondo, texExit, texVolumen, texBtnGuardar;
    private Texture texOjoAbierto, texOjoCerrado;
    private Texture texPixel;
    private BitmapFont fuenteTitulo;
    private BitmapFont fuenteLabel;
    private BitmapFont fuente18;
    private Stage stage;

    private TextField campoNombre, campoUsername, campoPassword;
    private boolean mostrarPassword = false;
    private ImageButton btnOjo;

    private Color colorEspecial = Color.RED;
    private Color colorNumero   = Color.RED;
    private Color colorCaract   = Color.RED;

    private boolean ingles;

    public PantallaMisDatos(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        // Leer idioma UNA vez
        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo      = new Texture("imagenes/fondos/FondoAzul.png");
        texExit       = new Texture("imagenes/botones/exit_button.png");
        texVolumen    = new Texture("imagenes/botones/volume_button.png");
        texOjoAbierto = new Texture("imagenes/botones/OpenedEye.png");
        texOjoCerrado = new Texture("imagenes/botones/ClosedEye.png");

        // Botón guardar según idioma
        texBtnGuardar = new Texture(ingles
                ? "imagenes/botones/Save.png"
                : "imagenes/botones/Guardar.png");

        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);

        fuenteLabel = new BitmapFont(Gdx.files.internal("fuentes/Pixellari60.fnt"));
        fuenteLabel.getData().setScale(1f);

        fuente18 = new BitmapFont(Gdx.files.internal("fuentes/Pixellari18.fnt"));
        fuente18.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estilo = crearEstiloTextField();

        var usuario = juego.getUsuarioActual();

        // Campo Nombre — editable
        campoNombre = new TextField(usuario.getNombreCompleto(), estilo);
        campoNombre.setBounds(236.2f, 550 - 165.7f - 52.9f, 288.9f, 52.9f);

        // Campo Username — NO editable
        campoUsername = new TextField(usuario.getUsername(), estilo);
        campoUsername.setBounds(236.2f, 550 - 231.2f - 52.9f, 288.9f, 52.9f);
        campoUsername.setDisabled(true);

        // Campo Password
        campoPassword = new TextField("", estilo);
        campoPassword.setMessageText("********");
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        campoPassword.setBounds(276.6f, 550 - 296.5f - 50.9f, 248.5f, 50.9f);

        campoPassword.setTextFieldListener((field, c) -> {
            String pass = field.getText();
            colorEspecial = ValidadorContrasena.tieneCaracterEspecial(pass) ? Color.GREEN : Color.RED;
            colorNumero   = ValidadorContrasena.tieneNumero(pass)           ? Color.GREEN : Color.RED;
            colorCaract   = (ValidadorContrasena.tieneMinCaracteres(pass)
                          && ValidadorContrasena.passwordLongitudValida(pass)) ? Color.GREEN : Color.RED;
        });

        // Botón ojo — FIX: usar TextureRegionDrawable actualizable correctamente
        btnOjo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texOjoCerrado)));
        btnOjo.setSize(50, 45);
        btnOjo.setPosition(276.6f + 248.5f + 5f, 550 - 296.5f - 50.9f);
        btnOjo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                mostrarPassword = !mostrarPassword;
                campoPassword.setPasswordMode(!mostrarPassword);
                // FIX: actualizar ícono correctamente
                ((TextureRegionDrawable) btnOjo.getStyle().imageUp)
                        .setRegion(new TextureRegion(
                                mostrarPassword ? texOjoAbierto : texOjoCerrado));
            }
        });

        // Botón Guardar
        ImageButton btnGuardar = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnGuardar)));
        btnGuardar.setSize(223.7f, 76.7f);
        btnGuardar.setPosition(213.1f, 550 - 418.3f - 76.7f);
        btnGuardar.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                guardarCambios();
            }
        });

        // Exit
        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setBounds(8.5f, 550 - 410.8f - 50f, 120.9f, 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
            }
        });

        // Volumen
        ImageButton btnVolumen = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texVolumen)));
        btnVolumen.setBounds(595f, 550 - 10.8f - 50f, 46f, 50f);

        stage.addActor(campoNombre);
        stage.addActor(campoUsername);
        stage.addActor(campoPassword);
        stage.addActor(btnOjo);
        stage.addActor(btnGuardar);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    private void guardarCambios() {
        var usuario = juego.getUsuarioActual();
        String nuevoNombre   = campoNombre.getText().trim();
        String nuevaPassword = campoPassword.getText();

        if (!ValidadorContrasena.nombreLongitudValida(nuevoNombre)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    ingles ? "Name must be between 3 and 8 characters"
                           : "El nombre debe tener entre 3 y 8 caracteres",
                    new PantallaMisDatos(juego)));
            return;
        }

        usuario.setNombreCompleto(nuevoNombre);

        if (!nuevaPassword.isEmpty()) {
            if (!ValidadorContrasena.esValida(nuevaPassword)) {
                juego.setScreen(new PantallaAdvertencia(juego,
                        ingles ? "Password does not meet the requirements"
                               : "La nueva contrasena no cumple los requisitos",
                        new PantallaMisDatos(juego)));
                return;
            }
            usuario.cambiarPassword(nuevaPassword);
        }

        GestorUsuarios.guardarUsuario(usuario);
        juego.setScreen(new PantallaAdvertencia(juego,
                ingles ? "Data saved successfully"
                       : "Datos guardados correctamente",
                new PantallaConfiguracion(juego)));
    }

    private TextField.TextFieldStyle crearEstiloTextField() {
        TextField.TextFieldStyle estilo = new TextField.TextFieldStyle();
        estilo.font = fuente18;
        Color colorTexto = new Color(87f/255f, 41f/255f, 35f/255f, 1f);
        estilo.fontColor        = colorTexto;
        estilo.messageFontColor = colorTexto;

        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(208f/255f, 104f/255f, 63f/255f, 1f);
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
        if (texFondo == null) return;

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(texFondo, 0, 0, 650, 550);

        // Título
        fuenteTitulo.setColor(Color.WHITE);
        fuenteTitulo.draw(batch,
                ingles ? "MY INFO" : "MIS DATOS",
                48.2f, 550 - 55f);

        // Labels
        fuenteLabel.setColor(Color.WHITE);
        fuenteLabel.draw(batch, ingles ? "NAME:"     : "NOMBRE:",   55f,  550 - 170.6f);
        fuenteLabel.draw(batch, ingles ? "USERNAME:" : "USUARIO:",  54.2f, 550 - 242.1f);
        fuenteLabel.draw(batch, "PASSWORD:",                         54.2f, 550 - 302.5f);

        // Requisitos contraseña
        float yReq = 550 - 371.5f;

        fuente18.setColor(colorEspecial);
        fuente18.draw(batch,
                ingles ? "SPECIAL CHARACTERS" : "CARACTERES ESPECIALES",
                100f, yReq);

        fuente18.setColor(colorNumero);
        fuente18.draw(batch,
                ingles ? "NUMBER" : "NUMERO",
                320f, yReq);

        fuente18.setColor(colorCaract);
        fuente18.draw(batch,
                "5 " + (ingles ? "CHARACTERS" : "CARACTERES"),
                420f, yReq);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
        if (stage != null) stage.getViewport().update(w, h, true);
    }

    @Override
    public void dispose() {
        if (texFondo       != null) texFondo.dispose();
        if (texExit        != null) texExit.dispose();
        if (texVolumen     != null) texVolumen.dispose();
        if (texBtnGuardar  != null) texBtnGuardar.dispose();
        if (texOjoAbierto  != null) texOjoAbierto.dispose();
        if (texOjoCerrado  != null) texOjoCerrado.dispose();
        if (texPixel       != null) texPixel.dispose();
        if (fuenteTitulo   != null) fuenteTitulo.dispose();
        if (fuenteLabel    != null) fuenteLabel.dispose();
        if (fuente18       != null) fuente18.dispose();
        if (stage          != null) stage.dispose();
    }
}