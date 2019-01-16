package com.cellular.automata.cellularautomata;


import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.data.Automata;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.RenderBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.FPSCounter;

public class GameInstance implements ApplicationListener{

    private String TAG = "GameInstance";

    private RenderBuilder renderBuilder;
    private Automata automata;
    private Environment environment;
    private RendererController rendererController;
    private FPSCounter fps = new FPSCounter();

    private Model testModel;

    @Override
    public void create() {

        rendererController = GRFX.rendererController;
        environment = new Environment();
        automata = new Automata();

        testModel = Model.fromCoordsArray(Settings.testSimpleCube);

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

                automata.setModel(testModel);
                GRFX.renderer.resetCam();

                break;

            }
            case RendererController.NEXT:{

                automata.next();
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
