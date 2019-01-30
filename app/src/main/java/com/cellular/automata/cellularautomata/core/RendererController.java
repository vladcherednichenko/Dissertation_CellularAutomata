package com.cellular.automata.cellularautomata.core;

public class RendererController {

    //holds all the commands for Automata
    // save actions user made on screen

    public int currentCommand = -1;

    public static final int START = 0;
    public static final int PAUSE = 1;
    public static final int RESET = 2;
    public static final int NEXT = 3;

    public static final int VIEW_MODE = 4;
    public static final int EDIT_MODE = 5;
    public static final int COLOR_SELECTED = 6;

    public static final int STRETCH = 7;
    public static final int SQUEEZE = 8;

    public static final int SCREEN_TOUCHED = 9;
    public static final int FIGURE_TOUCHED = 10;

    public static final int SAVE = 11;
    public static final int LOAD = 12;

    public static final int LAYER_UP = 13;
    public static final int LAYER_DOWN = 14;

    public static final int SHOW_GRID = 15;
    public static final int HIDE_GRID = 16;

    public static final int ADD_CUBE = 20;
    public static final int REMOVE_CUBE = 21;
    public static final int PAINT_CUBE = 22;


    public static final int NULL = -1;

    public int currentColor = 0;

    public int readCommand(){

        int ret = currentCommand;
        currentCommand = -1;
        return ret;

    }

    public void startPressed(){

        currentCommand = START;

    }

    public void pausePressed(){

        currentCommand = PAUSE;

    }

    public void resetPressed(){

        currentCommand = RESET;

    }

    public void nextStepPressed(){

        currentCommand = NEXT;

    }

    public void stretchPressed(){

        currentCommand = STRETCH;

    }

    public void squeezePressed(){

        currentCommand = SQUEEZE;

    }

    public void vewModePressed(){

        currentCommand = VIEW_MODE;

    }

    public void editModePressed(){

        currentCommand = EDIT_MODE;

    }

    public void savePressed(){

        currentCommand = SAVE;

    }

    public void loadPressed(){

        currentCommand = LOAD;

    }

    public void colorSelected(int color){

        this.currentColor = color;
        currentCommand = COLOR_SELECTED;

    }

    public void screenTouched(){

        currentCommand = SCREEN_TOUCHED;

    }

    public void cubeTouched(){

        currentCommand = FIGURE_TOUCHED;

    }

    public void layerUp(){

        currentCommand = LAYER_UP;

    }

    public void layerDown(){

        currentCommand = LAYER_DOWN;

    }

    public void showGrid(){

        currentCommand = SHOW_GRID;

    }

    public void hideGrid(){

        currentCommand = HIDE_GRID;

    }

    public void addCubePressed(){

        currentCommand = ADD_CUBE;

    }

    public void removeCsubePressed(){

        currentCommand = REMOVE_CUBE;

    }

    public void paintCubePressed(){

        currentCommand = PAINT_CUBE;

    }





}
