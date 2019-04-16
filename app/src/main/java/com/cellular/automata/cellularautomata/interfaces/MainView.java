package com.cellular.automata.cellularautomata.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.adapters.LoadScreenAdapter;
import com.cellular.automata.cellularautomata.data.Storage;
import com.cellular.automata.cellularautomata.objects.AutomataModel;

public interface MainView {

    // text view

    void logGesture(String text);

    void logText(String text);

    void logTextTop(String text);

    void logFps (int fps);



    // fragment view

    void openSaveFragment(Storage storage);

    void openLoadFragment();

    void openSettingsFragment();

    void removeFragments();


    // Fragment save
    String getSaveName();

    // Fragment load
    void attachAdapter(LoadScreenAdapter adapter);


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

    void resetInterfaceToEdit();

    void resetInterfaceToView();



    //getters

    Context getContext();




}
