package com.cellular.automata.cellularautomata;


import android.graphics.Bitmap;
import android.util.Log;

import com.cellular.automata.cellularautomata.core.InputCommander;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.Cube;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.AutomataBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.FPSCounter;
import com.cellular.automata.cellularautomata.utils.ImageHelper;

public class ApplicationManager implements ApplicationListener{

    private String TAG = "ApplicationManager";

    private AutomataBuilder builder;
    private Environment environment;
    private InputCommander inputCommander;
    private FPSCounter fps = new FPSCounter();

    private boolean isGenerating = false;

    private Rule rule;

    @Override
    public void create() {

        inputCommander = GRFX.activityListener.getInputCommander();
        builder = new AutomataBuilder();
        environment = new Environment();

        CellColor colors[] = new CellColor[Settings.testAutomataCoords.length/3];
        for(int i = 0; i< colors.length; i++){
            colors[i] = new CellColor("#4286f4");
        }

        Model testModel = new Model(Settings.testAutomataCoords, colors);

        builder.setModel(testModel);
        builder.build();
        builder.bindAttributesData();

        builder.setRule(new LifeRule());
        environment.addBuilder(builder);

        builder.setOnTouchListener(new AutomataBuilder.OnTouchListener() {
            @Override
            public void onTouch() {

                if(GRFX.activityListener!= null){
                    GRFX.activityListener.logText("nb: " + String.valueOf(rule.getNeighboursAmount(new Cube(builder.getTouchResult().touchedCubeCenter, null, false), builder.getMap())));
                }

            }
        });

        rule = new Rule();

    }

    @Override
    public void render() {

        final int command = inputCommander.readCommand();

        switch (command){

            //MAIN CONTROLS
            case InputCommander.START:{

                builder.start();
                break;

            }
            case InputCommander.PAUSE:{

                builder.pause();
                break;

            }
            case InputCommander.RESET:{

                isGenerating = false;
                CellColor colors[] = new CellColor[Settings.testAutomataCoords.length/3];
                for(int i = 0; i< colors.length; i++){
                    colors[i] = new CellColor("#4286f4");
                }
                Model testModel = new Model(Settings.testAutomataCoords, colors);

                builder.setModel(testModel);
                builder.build();
                builder.bindAttributesData();

                GRFX.renderer.resetCam();

                break;

            }
            case InputCommander.NEXT:{

                builder.speedUp();
                break;

            }
            //when the figure is touched
            case InputCommander.FIGURE_TOUCHED:{

                //adding / painting / deleting a cube
                if(builder.isTouched()){

                    Log.d(TAG, String.valueOf(rule.getNeighboursAmount(new Cube(builder.getTouchResult().touchedCubeCenter, null, false), builder.getMap())));

                    String color = Integer.toHexString(inputCommander.currentColor);

                    if(color.length()>=6){

                        color = "#" + color.substring(2);
                        builder.paintCube(builder.getTouchResult().touchedCubeCenter, new CellColor(color));

                    }
                    //builder.addNewCube(builder.getTouchResult().newCubeCenter, new CellColor("#4286f4"));
                }

                break;

            }
            //when the screen is touched
            case InputCommander.SCREEN_TOUCHED:{

                GRFX.activityListener.hideInterface();
                break;

            }
            case InputCommander.STRETCH:{

                builder.stretch();
                break;

            }
            case InputCommander.SQUEEZE:{

                builder.squeeze();
                break;

            }
            case InputCommander.SAVE:{

                GRFX.activityListener.logTextTop("Save pressed");
                Bitmap image = GRFX.activityListener.takeScreenshot();

                ImageHelper.saveImage(image, "screen1", GRFX.activityListener.getContext(), new ImageHelper.SaveImageCallback() {
                    @Override
                    public void onImageSaved() {
                        Log.d(TAG, "image saved");
                    }
                });

                break;

            }
            case InputCommander.LOAD:{

                GRFX.activityListener.logTextTop("Load pressed");
                break;

            }




        }

        builder.execute();
        builder.draw();

        GRFX.activityListener.logFps(fps.frames());

    }


    @Override
    public void resize() {

        //not needed

    }

}
