/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.usuarios;

public class ValidadorContrasena {

    public static boolean tieneMinCaracteres(String password) {
        return password.length() >= 5;
    }

    public static boolean tieneNumero(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }
        return false;
    }

    public static boolean tieneCaracterEspecial(String password) {
        String especiales = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        for (char c : password.toCharArray()) {
            if (especiales.indexOf(c) >= 0) return true;
        }
        return false;
    }

    public static boolean esValida(String password) {
        return tieneMinCaracteres(password) &&
               tieneNumero(password) &&
               tieneCaracterEspecial(password);
    }
    
   public static boolean usernameLongitudValida(String username) {
    return username.length() >= 3 && username.length() <= 5;
}

public static boolean nombreLongitudValida(String nombre) {
    return nombre.length() >= 3 && nombre.length() <= 5;
}

public static boolean passwordLongitudValida(String password) {
    return password.length() <= 5;
}
    
    
    
    
}