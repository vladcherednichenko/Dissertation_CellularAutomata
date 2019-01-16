package com.cellular.automata.cellularautomata.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.objects.Model;

public interface MainView {

    void logGesture(String text);

    void logText(String text);

    void logTextTop(String text);

    void logFps (int fps);

    void hideInterface();

    void openSaveFragment(Model model, Bitmap screenShot);

    void openLoadFragment();

    void removeFragments();

    void hideControlsBar();

    void hideLayersBar();

    void showControlsBar();

    void showLayersBar();

    void hideToolbar();

    void showToolbar();




    void showProgressBar();

    void hideProgressBar();


    //getters

    Context getContext();


}
