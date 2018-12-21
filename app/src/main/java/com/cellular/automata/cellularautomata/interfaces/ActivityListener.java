package com.cellular.automata.cellularautomata.interfaces;

import android.content.Context;

import com.cellular.automata.cellularautomata.core.InputCommander;

public interface ActivityListener {

    Context getContext();

    void logGesture(String text);

    void logText(String text);

    void loxTextTop(String text);

    InputCommander getInputCommander();

}
