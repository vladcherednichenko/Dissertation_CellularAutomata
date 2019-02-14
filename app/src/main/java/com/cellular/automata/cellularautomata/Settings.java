package com.cellular.automata.cellularautomata;

public class Settings {

    // GLOBAL SETTINGS
    public static boolean log_fps_counter = true;
    public static boolean log_top = true;
    public static boolean log_down = true;
    public static boolean log_alive_number = true;
    public static boolean debugTextViewEnabled = false;
    public static boolean validateShaderProgram = true;
    public static boolean antialiasing = false;
    public static String mainDataBaseName = "automata.sqlite";
    public static String imagePrefix = "screen_";
    public static boolean clearImageCache = true;

    // Automata
    public static int defaultAutomataRadius = 10;
    public static int minimumAutomataRadius = 5;
    public static float sliceHeight = 5f;

    // Cube
    public static String defaultCubeColor = "#ffffff";
    public static boolean generateCellsDataAfterCreation = true;
    public static float renderCubeSize = 1.0f;

    // Grid
    public static String gridColor = "#828282";
    public static String gridBGColor = "#b2a4f4";
    public static boolean highLightedCentralLines = false;
    public static String highlightedGridColor = "#a142f4";
    public static float gridHeight = -0.5f;
    public static float gridOffsetX = 0.0f;
    public static float gridOffsetZ = 0.0f;

    // Figure
    public static float minimumFigureScale = 0.1f;
    public static float maximumFigureScale = 6.0f;

    // Environment
    public static float lightDistance = 100f;
    public static String backgroundColor = "#b2b2b2";



    // Test Models
    public static float[] testAutomataCoords = new float[]{0, 0, 0,
            0, 0, 1, 0, 0, 2, 0, 0, 3, 0, 0, 4, 0, 0, -1, 0, 0, -2, 0, 0, -3, 0, 0, -4,
            0, 1, 0, 0, 2, 0, 0, 3, 0, 0, 4, 0, 0, -1, 0, 0, -2, 0, 0, -3, 0, 0, -4, 0,
            1, 0, 0, 2, 0, 0, 3, 0, 0, 4, 0, 0, -1, 0, 0, -2, 0, 0, -3, 0, 0, -4, 0, 0,
            1, 1, 1, 1, 1, -1, 1, -1, 1, 1, -1, -1, -1, 1, 1, -1, 1, -1, -1, -1, 1, -1, -1, -1};

    public static float[] testSimpleCube = new float[]{
            0, 0, 0
    };

    public static float[] testFourCube = new float[]{
            -3, -3, -3,
            -3, -3, 3,
            -3, 3, -3,
            3, -3, -3,
            3, 3, -3,
            3, -3, 3,
            -3, 3, 3,
            3, 3, 3
    };




}
