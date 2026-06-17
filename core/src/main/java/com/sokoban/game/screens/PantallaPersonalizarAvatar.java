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
import com.sokoban.game.usuarios.GestorUsuarios;
import com.sokoban.game.usuarios.Usuario;
import com.sokoban.game.GestorMusica;

public class PantallaPersonalizarAvatar extends PantallaBase {

    private boolean esBoy;
    private Usuario usuario;
    private Texture texFondo, texExit, texVolumen, texListo;
    private Texture texPixel;
    private BitmapFont fuenteTitulo;
    private BitmapFont fuenteTab;
    private Stage stage;
    private Texture texDefault;
    private Texture texCapaPiel;
    private Texture texCapaCabello;
    private Texture texCapaVest;
    private int selPiel = 0;
    private int selCabello = 0;
    private int selVest = 0;

    private enum TabAvatar {
        PIEL, CABELLO, VEST
    }
    private TabAvatar tabActivo = TabAvatar.PIEL;
    private static final float AV_X = 257.1f;
    private static final float AV_Y_CANVA = 140.4f;
    private static final float AV_W = 148.9f;
    private static final float AV_H = 185.8f;
    private static final float CUAD_W = 77.4f;
    private static final float CUAD_H = 73.9f;
    private static final float CUAD_GAP = 27f;
    private static final float CUAD_Y = 421f;
    private static final float TAB_Y_CANVA = 354.4f;
    private static final float[] TABS_X = {210f, 330f, 430f};
    private Texture texVolumenOn, texVolumenOff;

    public PantallaPersonalizarAvatar(SokobanGame juego, Usuario usuario, boolean esBoy) {
        super(juego, SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);
        this.usuario = usuario;
        this.esBoy = esBoy;
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI);

        texFondo = new Texture("imagenes/fondos/FondoAzul.png");
        texExit = new Texture("imagenes/botones/exit_button.png");
        texVolumen = new Texture("imagenes/botones/volume_button.png");
        texListo = new Texture("imagenes/botones/listo_button.png");
        texVolumenOn = new Texture("imagenes/botones/volume_button.png");
        texVolumenOff = new Texture("imagenes/botones/novolume_button.png");

        String prefijo = esBoy ? "Boy" : "Girl";
        texDefault = new Texture("imagenes/avatar/" + prefijo
                + "/" + prefijo + "DefaultAvatar.png");

        fuenteTitulo = new BitmapFont(Gdx.files.internal("fuentes/Pixellari60.fnt"));
        fuenteTitulo.getData().setScale(1f);
        fuenteTab = new BitmapFont(Gdx.files.internal("fuentes/Pixellari.fnt"));
        fuenteTab.getData().setScale(1f);

        com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(
                1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        texPixel = new Texture(pm);
        pm.dispose();

        cargarCapas();

        stage = new Stage(new FitViewport(SokobanGame.ANCHO_UI, SokobanGame.ALTO_UI));
        Gdx.input.setInputProcessor(stage);

        ImageButton btnExit = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texExit)));
        btnExit.setSize(120.9f, 50.3f);
        btnExit.setPosition(10.7f, 550 - 281.5f - 50.3f);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                juego.setScreen(new PantallaSeleccionAvatar(juego, usuario));
            }
        });

        ImageButton btnListo = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(texListo)));
        btnListo.setSize(182f, 62f);
        btnListo.setPosition(468f, 550 - 275f - 62f);
        btnListo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                guardarAvatar();
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
        stage.addActor(btnExit);
        stage.addActor(btnVolumen);
        stage.addActor(btnListo);
    }

    private void cargarCapas() {
        String prefijo = esBoy ? "Boy" : "Girl";

        String[] pielNombres = {"", "DarkSkin", "PaleSkin"};
        if (texCapaPiel != null) {
            texCapaPiel.dispose();
            texCapaPiel = null;
        }
        if (selPiel > 0) {
            texCapaPiel = new Texture("imagenes/avatar/" + prefijo
                    + "/Skintones/" + prefijo + pielNombres[selPiel] + ".png");
        }

        String[] cabellosGirl = {"", "PinkHair", "YellowHair", "GreenHair", "BlueHair"};
        String[] cabellosBoy = {"", "PinkHair", "YellowHair", "", "BlueHair", "OrangeHair"};
        String[] cabellos = esBoy ? cabellosBoy : cabellosGirl;
        if (texCapaCabello != null) {
            texCapaCabello.dispose();
            texCapaCabello = null;
        }
        if (selCabello > 0 && !cabellos[selCabello].isEmpty()) {
            texCapaCabello = new Texture("imagenes/avatar/" + prefijo
                    + "/Hairs/" + prefijo + cabellos[selCabello] + ".png");
        }
        String[] vestsGirl = {"", "BlueVest", "PinkVest", "OrangeVest", "YellowVest"};
        String[] vestsBoy = {"", "GreenVest", "PinkVest", "OrangeVest", "YellowVest"};
        String[] vests = esBoy ? vestsBoy : vestsGirl;
        if (texCapaVest != null) {
            texCapaVest.dispose();
            texCapaVest = null;
        }
        if (selVest > 0) {
            texCapaVest = new Texture("imagenes/avatar/" + prefijo
                    + "/Vests/" + prefijo + vests[selVest] + ".png");
        }
    }

    private void guardarAvatar() {
        int[] config = {selPiel, selCabello, selVest};
        usuario.setConfigAvatar(config);
        usuario.setTipoAvatar(esBoy ? 2 : 1);
        GestorUsuarios.guardarUsuario(usuario);
        juego.setUsuarioActual(usuario);
        juego.setScreen(new PantallaMenu(juego));
    }

    @Override
    public void render(float delta) {
        if (texFondo == null) {
            return;
        }

        manejarClicksPanel();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(texFondo, 0, 0, 650, 550);

        boolean ingles = "en".equals(juego.getUsuarioActual().getIdioma());
        String titulo = ingles ? "CUSTOMIZE YOUR AVATAR" : "PERSONALIZA TU AVATAR";
        fuenteTitulo.setColor(Color.WHITE);
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout
                = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
        layout.setText(fuenteTitulo, titulo, Color.WHITE, 650,
                com.badlogic.gdx.utils.Align.center, false);
        fuenteTitulo.draw(batch, layout, 0f, 550 - 75.9f);

        float avY = 550 - AV_Y_CANVA - AV_H;
        batch.draw(texDefault, AV_X, avY, AV_W, AV_H);
        if (texCapaPiel != null) {
            batch.draw(texCapaPiel, AV_X, avY, AV_W, AV_H);
        }
        if (texCapaCabello != null) {
            batch.draw(texCapaCabello, AV_X, avY, AV_W, AV_H);
        }
        if (texCapaVest != null) {
            batch.draw(texCapaVest, AV_X, avY, AV_W, AV_H);
        }

        dibujarPanel(ingles);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void dibujarPanel(boolean ingles) {
        Color colorFondo = new Color(208f / 255f, 104f / 255f, 63f / 255f, 1f);
        Color colorBorde = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);
        Color colorNormal = new Color(117f / 255f, 58f / 255f, 50f / 255f, 1f);

        float panelY = 550 - 344.9f - 205.1f;

        batch.setColor(colorFondo);
        batch.draw(texPixel, 0, panelY, 650, 205.1f);
        batch.setColor(colorBorde);
        batch.draw(texPixel, 0, panelY + 205.1f - 4, 650, 4);
        batch.setColor(Color.WHITE);

        float tabY = 550 - TAB_Y_CANVA;
        String[] tabsEs = {"TONO DE PIEL", "CABELLO", "UNIFORME"};
        String[] tabsEn = {"SKIN TONE", "HAIR", "UNIFORM"};
        String[] tabs = ingles ? tabsEn : tabsEs;
        TabAvatar[] tabVals = {TabAvatar.PIEL, TabAvatar.CABELLO, TabAvatar.VEST};

        for (int i = 0; i < 3; i++) {
            fuenteTab.setColor(tabActivo == tabVals[i] ? colorBorde : colorNormal);
            fuenteTab.draw(batch, tabs[i], TABS_X[i], tabY);
        }

        switch (tabActivo) {
            case PIEL:
                dibujarOpcionColor(201.5f, CUAD_Y, CUAD_W, CUAD_H, CUAD_GAP,
                        new Color[]{
                            new Color(255f / 255f, 144f / 255f, 100f / 255f, 1f),
                            new Color(160f / 255f, 109f / 255f, 75f / 255f, 1f),
                            new Color(255f / 255f, 194f / 255f, 169f / 255f, 1f)
                        }, selPiel);
                break;
            case CABELLO:
                Color[] coloresCab = esBoy
                        ? new Color[]{
                            new Color(1f, 0f, 184f / 255f, 1f), // 1 - Pink
                            new Color(1f, 245f / 255f, 0f, 1f), // 2 - Yellow
                            new Color(0f, 150f / 255f, 0f, 0f), // 3 - Green (invisible/default)
                            new Color(0f, 72f / 255f, 1f, 1f), // 4 - Blue
                            new Color(1f, 142f / 255f, 0f, 1f) // 5 - Orange
                        }
                        : new Color[]{
                            new Color(1f, 142f / 255f, 0f, 1f),
                            new Color(1f, 0f, 184f / 255f, 1f),
                            new Color(1f, 245f / 255f, 0f, 1f),
                            new Color(0f, 219f / 255f, 78f / 255f, 1f),
                            new Color(0f, 72f / 255f, 1f, 1f)
                        };
                dibujarOpcionColor(98.2f, CUAD_Y, CUAD_W, CUAD_H, CUAD_GAP,
                        coloresCab, selCabello);
                break;
            case VEST:
                Color[] coloresVest = esBoy
                        ? new Color[]{
                            new Color(0f, 72f / 255f, 1f, 1f),
                            new Color(0f, 219f / 255f, 78f / 255f, 1f),
                            new Color(1f, 0f, 184f / 255f, 1f),
                            new Color(1f, 142f / 255f, 0f, 1f),
                            new Color(1f, 245f / 255f, 0f, 1f)
                        }
                        : new Color[]{
                            new Color(0f, 177f / 255f, 139f / 255f, 1f),
                            new Color(0f, 72f / 255f, 1f, 1f),
                            new Color(1f, 0f, 184f / 255f, 1f),
                            new Color(1f, 142f / 255f, 0f, 1f),
                            new Color(1f, 245f / 255f, 0f, 1f)
                        };
                dibujarOpcionColor(98.2f, CUAD_Y, CUAD_W, CUAD_H, CUAD_GAP,
                        coloresVest, selVest);
                break;
        }
    }

    private void dibujarOpcionColor(float startX, float yCanva, float w, float h,
            float gap, Color[] colores, int seleccionado) {
        Color colorBorde = new Color(87f / 255f, 41f / 255f, 35f / 255f, 1f);

        for (int i = 0; i < colores.length; i++) {
            float x = startX + i * (w + gap);
            float yBase = 550 - yCanva - h;

            batch.setColor(colores[i]);
            batch.draw(texPixel, x + 4, yBase + 4, w - 8, h - 8);

            int grosor = (i == seleccionado) ? 4 : 2;
            batch.setColor(colorBorde);
            batch.draw(texPixel, x, yBase, w, grosor);
            batch.draw(texPixel, x, yBase + h - grosor, w, grosor);
            batch.draw(texPixel, x, yBase, grosor, h);
            batch.draw(texPixel, x + w - grosor, yBase, grosor, h);

            batch.setColor(Color.WHITE);
        }
    }

    private void manejarClicksPanel() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        com.badlogic.gdx.math.Vector2 tp = new com.badlogic.gdx.math.Vector2(
                Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(tp);
        float tx = tp.x, ty = tp.y;

        float tabY = 550 - TAB_Y_CANVA;
        TabAvatar[] tabVals = {TabAvatar.PIEL, TabAvatar.CABELLO, TabAvatar.VEST};

        for (int i = 0; i < 3; i++) {
            if (tx >= TABS_X[i] && tx <= TABS_X[i] + 100f
                    && ty >= tabY - 20f && ty <= tabY + 5f) {
                tabActivo = tabVals[i];
                return;
            }
        }

        int maxOpciones = 0;
        float startX = 98.2f;

        switch (tabActivo) {
            case PIEL:
                maxOpciones = 3;
                startX = 201.5f;
                break;
            case CABELLO:
                maxOpciones = esBoy ? 6 : 5;
                break;
            case VEST:
                maxOpciones = 5;
                break;
        }

        float yBase = 550 - CUAD_Y - CUAD_H;

        for (int i = 0; i < maxOpciones; i++) {
            float cx = startX + i * (CUAD_W + CUAD_GAP);
            if (tx >= cx && tx <= cx + CUAD_W
                    && ty >= yBase && ty <= yBase + CUAD_H) {
                switch (tabActivo) {
                    case PIEL:
                        selPiel = i;
                        break;
                    case CABELLO:
                        selCabello = i;
                        break;
                    case VEST:
                        selVest = i;
                        break;
                }
                cargarCapas();
                return;
            }
        }
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
        if (texVolumenOn != null) {
            texVolumenOn.dispose();
        }
        if (texVolumenOff != null) {
            texVolumenOff.dispose();
        }
        texListo.dispose();
        texDefault.dispose();
        texPixel.dispose();
        fuenteTitulo.dispose();
        fuenteTab.dispose();
        if (texCapaPiel != null) {
            texCapaPiel.dispose();
        }
        if (texCapaCabello != null) {
            texCapaCabello.dispose();
        }
        if (texCapaVest != null) {
            texCapaVest.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}
