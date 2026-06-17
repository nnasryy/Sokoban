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
import com.sokoban.game.usuarios.GestorUsuarios;
import com.sokoban.game.usuarios.ValidadorContrasena;
import com.sokoban.game.GestorMusica;
public class PantallaMisDatos extends PantallaBase {

    private Texture texFondo, texExit;
    private Texture texBtnGuardar, texBtnDesactivar;
    private Texture texOjoAbierto, texOjoCerrado;
    private Texture texPixel;
    private Texture texAvatarJugador;
    private Texture texVolumenOn, texVolumenOff;

    private BitmapFont fuenteTitulo;   // Pixellari100
    private BitmapFont fuenteLabel;    // Pixellari27
    private BitmapFont fuente18;       // Pixellari18

    private Stage stage;
    private TextField campoNombre, campoPassword;
    private boolean mostrarPassword = false;
    private ImageButton btnOjo;

    private Color colorEspecial = Color.RED;
    private Color colorNumero = Color.RED;
    private Color colorCaract = Color.RED;
    private boolean ingles;

    // Layout — columna izquierda (avatar) y derecha (campos)
    private static final float AV_X = 30f;
    private static final float AV_Y_TOP = 120f;   // desde arriba en coordenadas Canva
    private static final float AV_W = 160f;
    private static final float AV_H = 200f;

    private static final float CAMPOS_X = 230f;
    private static final float CAMPO_W = 380f;
    private static final float CAMPO_H = 42f;

    public PantallaMisDatos(SokobanGame juego) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        ingles = juego.getUsuarioActual() != null
                && "en".equals(juego.getUsuarioActual().getIdioma());

        texFondo = new Texture("imagenes/fondos/FondoAzul.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texOjoAbierto = new Texture("imagenes/botones/OpenedEye.png");
        texOjoCerrado = new Texture("imagenes/botones/ClosedEye.png");
        texBtnGuardar = new Texture(ingles
                ? "imagenes/botones/Save.png"
                : "imagenes/botones/Guardar.png");
        texBtnDesactivar = new Texture(ingles
                ? "imagenes/botones/Deactivate.png"
                : "imagenes/botones/Desactivar.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        // Pixel para bordes
        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        // Cargar avatar del jugador
        cargarAvatarJugador();

        // Fuentes
        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari100.fnt"));
        fuenteTitulo.getData().setScale(1f);
        fuenteLabel = new BitmapFont(Gdx.files.internal("fuentes/Pixellari27.fnt"));
        fuenteLabel.getData().setScale(1f);
        fuente18 = new BitmapFont(Gdx.files.internal("fuentes/Pixellari18.fnt"));
        fuente18.getData().setScale(1f);

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle estilo = crearEstiloTextField();
        var usuario = juego.getUsuarioActual();

        // Campo nombre — Y: 185 desde arriba
        campoNombre = new TextField(usuario.getNombreCompleto(), estilo);
        campoNombre.setBounds(CAMPOS_X, 550 - 195f - CAMPO_H, CAMPO_W, CAMPO_H);

        // Campo username — deshabilitado, Y: 255
        TextField campoUsername = new TextField(usuario.getUsername(), estilo);
        campoUsername.setBounds(CAMPOS_X, 550 - 265f - CAMPO_H, CAMPO_W, CAMPO_H);
        campoUsername.setDisabled(true);

        // Campo password — Y: 325
        campoPassword = new TextField("", estilo);
        campoPassword.setMessageText("*****");
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        campoPassword.setBounds(CAMPOS_X, 550 - 335f - CAMPO_H, CAMPO_W - 55f, CAMPO_H);
        campoPassword.setTextFieldListener((field, c) -> {
            String pass = field.getText();
            colorEspecial = ValidadorContrasena.tieneCaracterEspecial(pass) ? Color.GREEN : Color.RED;
            colorNumero = ValidadorContrasena.tieneNumero(pass) ? Color.GREEN : Color.RED;
            colorCaract = (ValidadorContrasena.tieneMinCaracteres(pass)
                    && ValidadorContrasena.passwordLongitudValida(pass)) ? Color.GREEN : Color.RED;
        });

        // Botón ojo
        btnOjo = new ImageButton(new TextureRegionDrawable(new TextureRegion(texOjoCerrado)));
        btnOjo.setSize(46f, CAMPO_H);
        btnOjo.setPosition(CAMPOS_X + CAMPO_W - 55f + 5f, 550 - 335f - CAMPO_H);
        btnOjo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                mostrarPassword = !mostrarPassword;
                campoPassword.setPasswordMode(!mostrarPassword);
                ((TextureRegionDrawable) btnOjo.getStyle().imageUp)
                        .setRegion(new TextureRegion(mostrarPassword ? texOjoAbierto : texOjoCerrado));
            }
        });

        // Botón Guardar
        ImageButton btnGuardar = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnGuardar)));
        btnGuardar.setSize(185f, 60f);
        btnGuardar.setPosition(CAMPOS_X, 550 - 420f - 60f);
        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                guardarCambios();
            }
        });

        // Botón Desactivar
        ImageButton btnDesactivar = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texBtnDesactivar)));
        btnDesactivar.setSize(185f, 60f);
        btnDesactivar.setPosition(CAMPOS_X + 195f, 550 - 420f - 60f);
        btnDesactivar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                confirmarDesactivar();
            }
        });

        // Botón Exit
        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(8.5f, 550 - 500f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
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
        stage.addActor(campoNombre);
        stage.addActor(campoUsername);
        stage.addActor(campoPassword);
        stage.addActor(btnOjo);
        stage.addActor(btnGuardar);
        stage.addActor(btnDesactivar);
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
    }

    // Carga el avatar según tipoAvatar y configAvatar del usuario
    private void cargarAvatarJugador() {
        if (juego.getUsuarioActual() == null) {
            return;
        }
        int tipo = juego.getUsuarioActual().getTipoAvatar();
        int[] config = juego.getUsuarioActual().getConfigAvatar();

        if (tipo == 0) {
            String ruta = juego.getUsuarioActual().getRutaFotoPerfil();
            if (ruta != null && !ruta.isEmpty()) {
                try {
                    texAvatarJugador = new Texture(Gdx.files.absolute(ruta));
                } catch (Exception ex) {
                    texAvatarJugador = new Texture("imagenes/avatar/Boy/BoyDefaultAvatar.png");
                }
            } else {
                texAvatarJugador = new Texture("imagenes/avatar/Boy/BoyDefaultAvatar.png");
            }
        } else {
            String prefijo = (tipo == 1) ? "Girl" : "Boy";
            texAvatarJugador = new Texture(
                    "imagenes/avatar/" + prefijo + "/" + prefijo + "DefaultAvatar.png");
        }
    }

    private void confirmarDesactivar() {
        String msg = ingles
                ? "Are you sure you want to deactivate your account? This will erase your ranking and friends."
                : "Seguro que quieres desactivar tu cuenta? Se borrara tu ranking y amigos.";
        juego.setScreen(new PantallaAdvertencia(juego, msg,
                new PantallaConfirmarDesactivar(juego)));
    }

    private void guardarCambios() {
        var usuario = juego.getUsuarioActual();
        String nuevoNombre = campoNombre.getText().trim();
        String nuevaPass = campoPassword.getText();

        if (!ValidadorContrasena.nombreLongitudValida(nuevoNombre)) {
            juego.setScreen(new PantallaAdvertencia(juego,
                    ingles ? "Name must be between 3 and 8 characters"
                            : "El nombre debe tener entre 3 y 8 caracteres",
                    new PantallaMisDatos(juego)));
            return;
        }

        usuario.setNombreCompleto(nuevoNombre);

        if (!nuevaPass.isEmpty()) {
            if (!ValidadorContrasena.esValida(nuevaPass)) {
                juego.setScreen(new PantallaAdvertencia(juego,
                        ingles ? "Password does not meet the requirements"
                                : "La contrasena no cumple los requisitos",
                        new PantallaMisDatos(juego)));
                return;
            }
            usuario.cambiarPassword(nuevaPass);
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
                2, 34, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
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

        // Título centrado
        GlyphLayout layout = new GlyphLayout();
        fuenteTitulo.setColor(Color.WHITE);
        String titulo = ingles ? "MY INFO" : "MIS DATOS";
        layout.setText(fuenteTitulo, titulo, Color.WHITE, 650,
                com.badlogic.gdx.utils.Align.center, false);
        fuenteTitulo.draw(batch, layout, 0f, 550 - 18f);

        // Avatar izquierda
        float avY = 550 - AV_Y_TOP - AV_H;
        if (texAvatarJugador != null) {
            Color colorBorde = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);
            batch.setColor(colorBorde);
            batch.draw(texPixel, AV_X - 4, avY - 4, AV_W + 8, AV_H + 8);
            batch.setColor(Color.WHITE);
            batch.draw(texAvatarJugador, AV_X, avY, AV_W, AV_H);

            int tipo = juego.getUsuarioActual().getTipoAvatar();
            if (tipo == 1 || tipo == 2) {
                int[] config = juego.getUsuarioActual().getConfigAvatar();
                String prefijo = (tipo == 1) ? "Girl" : "Boy";
                dibujarCapaAvatar(prefijo, "Skintones", obtenerNombrePiel(prefijo, config[0]), avY);
                dibujarCapaAvatar(prefijo, "Hairs", obtenerNombreCabello(prefijo, config[1]), avY);
                dibujarCapaAvatar(prefijo, "Vests", obtenerNombreVest(prefijo, config[2]), avY);
            }
        }

        // Username debajo del avatar
        fuenteLabel.setColor(Color.WHITE);
        layout.setText(fuenteLabel, "@" + juego.getUsuarioActual().getUsername(),
                Color.WHITE, AV_W, com.badlogic.gdx.utils.Align.center, false);
        fuenteLabel.draw(batch, layout, AV_X, avY - 8f);

        // Labels columna derecha
        Color colorLabel = new Color(1f, 1f, 1f, 0.85f);
        fuenteLabel.setColor(colorLabel);
        fuenteLabel.draw(batch, ingles ? "NAME" : "NOMBRE", CAMPOS_X, 550 - 175f);
        fuenteLabel.draw(batch, ingles ? "USERNAME" : "USUARIO", CAMPOS_X, 550 - 245f);
        fuenteLabel.draw(batch, "PASSWORD", CAMPOS_X, 550 - 315f);

        // Requisitos de password
        float yReq = 550 - 395f;
        fuente18.setColor(colorEspecial);
        fuente18.draw(batch, ingles ? "SPECIAL CHAR" : "CHAR. ESPECIAL", CAMPOS_X, yReq);
        fuente18.setColor(colorNumero);
        fuente18.draw(batch, ingles ? "NUMBER" : "NUMERO", CAMPOS_X + 175f, yReq);
        fuente18.setColor(colorCaract);
        fuente18.draw(batch, "5+ CHARS", CAMPOS_X + 290f, yReq);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void dibujarCapaAvatar(String prefijo, String carpeta, String nombre, float avY) {
        if (nombre == null || nombre.isEmpty()) {
            return;
        }
        String ruta = "imagenes/avatar/" + prefijo + "/" + carpeta + "/" + prefijo + nombre + ".png";
        try {
            com.badlogic.gdx.files.FileHandle fh = Gdx.files.internal(ruta);
            if (fh.exists()) {
                Texture capa = new Texture(fh);
                batch.flush(); // fuerza el draw antes de crear nueva textura
                batch.draw(capa, AV_X, avY, AV_W, AV_H);
                batch.flush(); // fuerza el draw antes de dispose
                capa.dispose();
            }
        } catch (Exception e) {
            /* capa no existe */ }
    }

    private String obtenerNombrePiel(String p, int sel) {
        String[] n = {"", "DarkSkin", "PaleSkin"};
        return sel > 0 && sel < n.length ? n[sel] : null;
    }

    private String obtenerNombreCabello(String p, int sel) {
        String[] girl = {"", "PinkHair", "YellowHair", "GreenHair", "BlueHair"};
        String[] boy = {"", "PinkHair", "YellowHair", "", "BlueHair", "OrangeHair"}; // índice 3 vacío = default
        String[] arr = p.equals("Girl") ? girl : boy;
        return sel > 0 && sel < arr.length && !arr[sel].isEmpty() ? arr[sel] : null;
    }

    private String obtenerNombreVest(String p, int sel) {
        String[] girl = {"", "BlueVest", "PinkVest", "OrangeVest", "YellowVest"};
        String[] boy = {"", "GreenVest", "PinkVest", "OrangeVest", "YellowVest"};
        String[] arr = p.equals("Girl") ? girl : boy;
        return sel > 0 && sel < arr.length ? arr[sel] : null;
    }

    @Override
    public void resize(int w, int h
    ) {
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
        if (texBtnGuardar != null) {
            texBtnGuardar.dispose();
        }
        if (texBtnDesactivar != null) {
            texBtnDesactivar.dispose();
        }
        if (texOjoAbierto != null) {
            texOjoAbierto.dispose();
        }
        if (texOjoCerrado != null) {
            texOjoCerrado.dispose();
        }
        if (texPixel != null) {
            texPixel.dispose();
        }
        if (texAvatarJugador != null) {
            texAvatarJugador.dispose();
        }
        if (fuenteTitulo != null) {
            fuenteTitulo.dispose();
        }
        if (fuenteLabel != null) {
            fuenteLabel.dispose();
        }
        if (texVolumenOn != null) {
            texVolumenOn.dispose();
        }
        if (texVolumenOff != null) {
            texVolumenOff.dispose();
        }
        if (fuente18 != null) {
            fuente18.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

}
