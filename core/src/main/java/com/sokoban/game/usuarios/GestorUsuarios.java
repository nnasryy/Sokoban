/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.usuarios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private static final String CARPETA_BASE = "datos/usuarios/";

    public static boolean registrarUsuario(Usuario u) {
        if (existeUsuario(u.getUsername())) {
            return false;
        }

        File carpeta = new File(CARPETA_BASE + u.getUsername());
        carpeta.mkdirs();

        guardarUsuario(u);
        return true;
    }

    public static void guardarUsuario(Usuario u) {
        String ruta = CARPETA_BASE + u.getUsername() + "/perfil.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ruta))) {
            oos.writeObject(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Usuario cargarUsuario(String username) {
        String ruta = CARPETA_BASE + username + "/perfil.dat";
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ruta))) {
            return (Usuario) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static Usuario login(String username, String password) {
        Usuario u = cargarUsuario(username);
        if (u != null && u.verificarPassword(password)) {
            u.actualizarUltimaSesion();
            guardarUsuario(u);
            return u;
        }
        return null;
    }

    public static boolean existeUsuario(String username) {
        return new File(CARPETA_BASE + username).exists();
    }

    public static List<Usuario> cargarTodos() {
        List<Usuario> lista = new ArrayList<>();
        File carpeta = new File(CARPETA_BASE);
        if (carpeta.exists() && carpeta.isDirectory()) {
            for (File sub : carpeta.listFiles()) {
                if (sub.isDirectory()) {
                    Usuario u = cargarUsuario(sub.getName());
                    if (u != null) {
                        lista.add(u);
                    }
                }
            }
        }
        return lista;
    }

    public static List<Usuario> getRanking() {
        List<Usuario> todos = cargarTodos();
        todos.sort((a, b) -> b.getPuntajeTotal() - a.getPuntajeTotal());
        return todos;
    }
    // Envía solicitud de A hacia B
public static boolean enviarSolicitud(String deUsername, String aUsername) {
    if (!existeUsuario(aUsername)) return false;
    if (deUsername.equals(aUsername)) return false;

    Usuario destino = cargarUsuario(aUsername);
    if (destino == null) return false;

    // Verifica que no sean ya amigos
    if (destino.getAmigos().contains(deUsername)) return false;

    destino.recibirSolicitud(deUsername);
    guardarUsuario(destino);
    return true;
}

public static void aceptarSolicitud(Usuario usuarioActual, String deUsername) {
    usuarioActual.aceptarSolicitud(deUsername);
    guardarUsuario(usuarioActual);

    Usuario otro = cargarUsuario(deUsername);
    if (otro != null) {
        otro.agregarAmigo(usuarioActual.getUsername());
        guardarUsuario(otro);
    }
}

public static void rechazarSolicitud(Usuario usuarioActual, String deUsername) {
    usuarioActual.rechazarSolicitud(deUsername);
    guardarUsuario(usuarioActual);
}
}
