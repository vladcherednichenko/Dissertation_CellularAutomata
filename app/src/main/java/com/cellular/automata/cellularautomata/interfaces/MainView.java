package com.cellular.automata.cellularautomata.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.objects.Model;

public interface MainView {

    // text view

    void logGesture(String text);

    void logText(String text);

    void logTextTop(String text);

    void logFps (int fps);



    // fragment view

    void openSaveFragment(Model model, Bitmap screenShot);

    void openLoadFragment();

    void removeFragments();



    // Animation view

    void hideInterface();

    void hideControlsBar();

    void hideEditBar();

    void showControlsBar();

    void showEditBar();

    void hideToolbar();

    void showToolbar();

    void showProgressBar();

    void hideProgressBar();



    //getters

    Context getContext();


}
