package com.cellular.automata.cellularautomata.utils;

public class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;
    int oldFrames =0;

    public int frames() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
            oldFrames = frames;
            frames = 0;
            startTime = System.nanoTime();
        }

        return oldFrames;
    }
}

