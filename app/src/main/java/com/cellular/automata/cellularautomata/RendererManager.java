package com.cellular.automata.cellularautomata;


import android.util.Log;

import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.data.Automata;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.RenderBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.FPSCounter;

public class RendererManager implements ApplicationListener{

    private String TAG = "RendererManager";

    private RenderBuilder renderBuilder;
    private Automata automata;
    private Environment environment;
    private RendererController rendererController;
    private FPSCounter fps = new FPSCounter();

    private long startTime = 0;
    private long delay = 500;
    private boolean isGenerating = false;

    private Rule rule;

    @Override
    public void create() {

        rendererController = GRFX.rendererController;
        environment = new Environment();
        automata = new Automata();

        CellColor colors[] = new CellColor[Settings.testAutomataCoords.length/3];
        for(int i = 0; i< colors.length; i++){
            colors[i] = new CellColor("#4286f4");
        }

        Model testModel = new Model(Settings.testAutomataCoords, colors);

        automata.setModel(testModel);
        automata.setRule(new LifeRule());

        renderBuilder = automata.getRenderBuilder();
        environment.addBuilder(renderBuilder);

        renderBuilder.setOnTouchListener(new RenderBuilder.OnTouchListener() {
            @Override
            public void onTouch() {

                if(GRFX.activityListener!= null){
                    //GRFX.activityListener.logText("nb: " + String.valueOf(rule.getNeighboursAmount(new RenderCube(renderBuilder.getTouchResult().touchedCubeCenter, null, false), renderBuilder.getRenderMap())));
                }

            }
        });

        rule = new Rule();

    }

    @Override
    public void render() {

        final int command = rendererController.readCommand();

        switch (command){

            //MAIN CONTROLS
            case RendererController.START:{

                automata.start();
                break;

            }
            case RendererController.PAUSE:{

                automata.pause();
                break;

            }
            case RendererController.RESET:{

                isGenerating = false;
                CellColor colors[] = new CellColor[Settings.testAutomataCoords.length/3];
                for(int i = 0; i< colors.length; i++){
                    colors[i] = new CellColor("#4286f4");
                }
                Model testModel = new Model(Settings.testAutomataCoords, colors);

                automata.setModel(testModel);
                GRFX.renderer.resetCam();

                break;

            }
            case RendererController.NEXT:{

                automata.speedUp();
                break;

            }
            //when the figure is touched
            case RendererController.FIGURE_TOUCHED:{

                //adding / painting / deleting a cube
                if(renderBuilder.isTouched()){

                    //Log.d(TAG, String.valueOf(rule.getNeighboursAmount(new RenderCube(renderBuilder.getTouchResult().touchedCubeCenter, null, false), renderBuilder.getRenderMap())));

                    String color = Integer.toHexString(rendererController.currentColor);

                    if(color.length()>=6){

                        color = "#" + color.substring(2);
                        renderBuilder.paintCube(renderBuilder.getTouchResult().touchedCubeCenter, new CellColor(color));

                    }
                    //renderBuilder.addNewCube(renderBuilder.getTouchResult().newCubeCenter, new CellColor("#4286f4"));
                }

                break;

            }

            case RendererController.STRETCH:{

                renderBuilder.stretch();
                break;

            }
            case RendererController.SQUEEZE:{

                renderBuilder.squeeze();
                break;

            }



        }

        automata.execute();
        renderBuilder.draw();

        GRFX.activityListener.logFps(fps.frames());

    }


    @Override
    public void resize() {

        //not needed

    }

}
