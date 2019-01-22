package com.cellular.automata.cellularautomata;


import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.data.Automata;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.FPSCounter;

public class GameInstance implements ApplicationListener{

    private String TAG = "GameInstance";

    private ModelRenderBuilder modelRenderBuilder;
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

        modelRenderBuilder = automata.getModelRenderBuilder();
        environment.addBuilder(modelRenderBuilder);

        modelRenderBuilder.setOnTouchListener(new ModelRenderBuilder.OnTouchListener() {
            @Override
            public void onTouch() {

                if(GRFX.activityListener!= null){
                    //GRFX.activityListener.logText("nb: " + String.valueOf(rule.getNeighboursAmount(new RenderCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, null, false), modelRenderBuilder.getRenderMap())));
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
                if(modelRenderBuilder.isTouched()){

                    //Log.d(TAG, String.valueOf(rule.getNeighboursAmount(new RenderCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, null, false), modelRenderBuilder.getRenderMap())));

                    String color = Integer.toHexString(rendererController.currentColor);

                    if(color.length()>=6){

                        color = "#" + color.substring(2);
                        modelRenderBuilder.paintCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, new CellColor(color));

                    }
                    //modelRenderBuilder.addNewCube(modelRenderBuilder.getTouchResult().newCubeCenter, new CellColor("#4286f4"));
                }

                break;

            }

            case RendererController.STRETCH:{

                modelRenderBuilder.stretch();
                break;

            }
            case RendererController.SQUEEZE:{

                modelRenderBuilder.squeeze();
                break;

            }



        }

        automata.execute();
        modelRenderBuilder.draw();

        GRFX.activityListener.logFps(fps.frames());

    }


    @Override
    public void resize() {

        //not needed

    }

}
