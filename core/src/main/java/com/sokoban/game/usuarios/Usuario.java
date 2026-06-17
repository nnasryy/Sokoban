/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.usuarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    private int nivelesDesbloqueados = 1;
    private String username;
    private String passwordHash;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int nivelActual;
    private int[] intentosPorNivel;
    private long tiempoTotalJugado;
    private List<Long> tiempoPorNivel;
    private String rutaFotoPerfil;
    private int tipoAvatar;
    private int[] configAvatar;
    private float volumen;
    private String idioma;
    private int partidasJugadas;
    private int nivelesCompletados;
    private List<String> historialPartidas;
    private int puntajeTotal;
    private List<String> amigos;
    private List<String> solicitudesPendientes;
    private boolean cuentaActiva = true;
    private String esquemaControles = "WASD";
    private int partidasCompetitivasJugadas = 0;
    private int partidasCompetitivasGanadas = 0;

    public Usuario(String username, String password, String nombreCompleto) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = new Date();
        this.ultimaSesion = new Date();
        this.nivelActual = 1;
        this.intentosPorNivel = new int[6];
        this.tiempoTotalJugado = 0;
        this.tiempoPorNivel = new ArrayList<>();
        this.partidasJugadas = 0;
        this.nivelesCompletados = 0;
        this.historialPartidas = new ArrayList<>();
        this.configAvatar = new int[]{0, 0, 0, 0};
        this.volumen = 0.8f;
        this.idioma = "es";
        this.puntajeTotal = 0;
        this.amigos = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
    }

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public String getEsquemaControles() {
        return esquemaControles;
    }

    public void registrarPartidaCompetitiva(boolean gano) {
        partidasCompetitivasJugadas++;
        if (gano) {
            partidasCompetitivasGanadas++;
        }
    }

    public int getPartidasCompetitivasJugadas() {
        return partidasCompetitivasJugadas;
    }

    public int getPartidasCompetitivasGanadas() {
        return partidasCompetitivasGanadas;
    }

    public void setEsquemaControles(String e) {
        this.esquemaControles = e;
    }

    public boolean verificarPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    public void actualizarUltimaSesion() {
        this.ultimaSesion = new Date();
    }

    public void agregarAmigo(String usernameAmigo) {
        if (!amigos.contains(usernameAmigo)) {
            amigos.add(usernameAmigo);
        }
    }

    public List<String> getSolicitudesPendientes() {
        return solicitudesPendientes;
    }

    public void recibirSolicitud(String deUsername) {
        if (!solicitudesPendientes.contains(deUsername)
                && !amigos.contains(deUsername)) {
            solicitudesPendientes.add(deUsername);
        }
    }

    public void aceptarSolicitud(String username) {
        if (solicitudesPendientes.remove(username)) {
            agregarAmigo(username);
        }
    }

    public void rechazarSolicitud(String username) {
        solicitudesPendientes.remove(username);
    }

    public void registrarPartida(int nivel, long tiempo, boolean completado) {
        partidasJugadas++;
        tiempoTotalJugado += tiempo;
        tiempoPorNivel.add(tiempo);
        if (completado) {
            nivelesCompletados++;
        }
        intentosPorNivel[nivel]++;
        historialPartidas.add("Nivel " + nivel
                + " | Tiempo: " + tiempo + "s"
                + " | " + (completado ? "Completado" : "Fallido")
                + " | " + new Date());
    }

    public String getUsername() {
        return username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public Date getUltimaSesion() {
        return ultimaSesion;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int n) {
        this.nivelActual = n;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public int getNivelesCompletados() {
        return nivelesCompletados;
    }

    public long getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }

    public int[] getIntentosPorNivel() {
        return intentosPorNivel;
    }

    public List<Long> getTiempoPorNivel() {
        return tiempoPorNivel;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int p) {
        this.puntajeTotal = p;
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float v) {
        this.volumen = v;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String i) {
        this.idioma = i;
    }

    public int[] getConfigAvatar() {
        return configAvatar;
    }

    public void setConfigAvatar(int[] c) {
        this.configAvatar = c;
    }

    public int getTipoAvatar() {
        return tipoAvatar;
    }

    public void setTipoAvatar(int t) {
        this.tipoAvatar = t;
    }

    public String getRutaFotoPerfil() {
        return rutaFotoPerfil;
    }

    public void setRutaFotoPerfil(String r) {
        this.rutaFotoPerfil = r;
    }

    public List<String> getHistorial() {
        return historialPartidas;
    }

    public long getTiempoPromedioPorNivel() {
        if (tiempoPorNivel.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (long t : tiempoPorNivel) {
            total += t;
        }
        return total / tiempoPorNivel.size();
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "username='" + username + '\''
                + ", nombreCompleto='" + nombreCompleto + '\''
                + ", fechaRegistro=" + fechaRegistro
                + ", nivelActual=" + nivelActual
                + ", partidasJugadas=" + partidasJugadas
                + '}';
    }

    public int getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    public void setNombreCompleto(String nombre) {
        this.nombreCompleto = nombre;
    }

    public void cambiarPassword(String nuevaPassword) {
        this.passwordHash = Integer.toHexString(nuevaPassword.hashCode());
    }

    public void desbloquearSiguienteNivel() {
        if (nivelesDesbloqueados < 5) {
            nivelesDesbloqueados++;
        }

    }

    public boolean isCuentaActiva() {
        return cuentaActiva;
    }

    public void desactivarCuenta() {
        this.cuentaActiva = false;
    }

    public void reactivarCuenta() {
        this.cuentaActiva = true;
    }

}
