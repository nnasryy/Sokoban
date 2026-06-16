/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.game.hilos;

public class HiloTimer extends Thread {

    private volatile boolean corriendo = true;
    private volatile boolean pausado = false;
    private volatile int segundos = 0;

    @Override
    public void run() {
        while (corriendo) {
            try {
                Thread.sleep(1000);
                if (!pausado) {
                    synchronized (this) {
                        segundos++;
                    }
                }
            } catch (InterruptedException e) {
                corriendo = false;
            }
        }
    }

    public synchronized int getSegundos() {
        return segundos;
    }

    public synchronized void reiniciar() {
        segundos = 0;
    }

    public void pausar() {
        pausado = true;
    }

    public void reanudar() {
        pausado = false;
    }

    public void detener() {
        corriendo = false;
        this.interrupt();
    }
}
