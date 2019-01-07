package com.cellular.automata.cellularautomata.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.core.InputCommander;

public interface ActivityListener {

    Context getContext();

    void logGesture(String text);

    void logText(String text);

    void logTextTop(String text);

    void logFps (int fps);

    void hideInterface();

    InputCommander getInputCommander();

    Bitmap takeScreenshot();

}
