package com.cellular.automata.cellularautomata;


import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.data.Automata;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.GridRenderBuilder;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.FPSCounter;

public class GameInstance implements ApplicationListener{

    private String TAG = "GameInstance";

    private ModelRenderBuilder modelRenderBuilder;
    private GridRenderBuilder gridRenderBuilder;
    private Automata automata;
    private Environment environment;
    private RendererController rendererController;
    private FPSCounter fps = new FPSCounter();

    private int actionWithCube = 0;

    private boolean gridIsVisible = false;

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

            case -1:{

                break;

            }

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

                    String color = Integer.toHexString(rendererController.currentColor);

                    //Log.d(TAG, String.valueOf(rule.getNeighboursAmount(new RenderCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, null, false), modelRenderBuilder.getRenderMap())));
                    if(actionWithCube == RendererController.ADD_CUBE){

                        //modelRenderBuilder.addNewCube(modelRenderBuilder.getTouchResult().newCubeCenter, new CellColor(color));

                        Cube cube = new Cube(color, new int []{
                                (int) modelRenderBuilder.getTouchResult().newCubeCenter.x,
                                (int) modelRenderBuilder.getTouchResult().newCubeCenter.y,
                                (int) modelRenderBuilder.getTouchResult().newCubeCenter.z});

                        cube.setAlive(true);

                        automata.addNewCube(cube);

                    }
                    if(actionWithCube == RendererController.REMOVE_CUBE){

                        modelRenderBuilder.deleteCube(modelRenderBuilder.getTouchResult().touchedCubeCenter);

                    }
                    if(actionWithCube == RendererController.PAINT_CUBE){

                        if(color.length()>=6){

                            color = "#" + color.substring(2);
                            modelRenderBuilder.paintCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, new CellColor(color));

                        }
                    }

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
            case RendererController.EDIT_MODE:{

                // do sth with this
                // in the main Activity
                modelRenderBuilder.squeeze();
                modelRenderBuilder.setViewMode(false);

                if(gridRenderBuilder == null){
                    gridRenderBuilder = new GridRenderBuilder(automata.getAutomataRadius());
                }

                gridRenderBuilder.build();
                gridRenderBuilder.bindAttributesData();

                environment.addGrid(gridRenderBuilder);

                break;

            }
            case RendererController.VIEW_MODE:{

                modelRenderBuilder.setViewMode(true);
                gridIsVisible = false;
                environment.removeGrids();
                GRFX.renderer.resetAdditionalStride();
                break;
            }
            case RendererController.LAYER_UP:{

                GRFX.renderer.translateFigureVertical(-Settings.renderCubeSize);
                modelRenderBuilder.layerUp();
                break;
            }

            case RendererController.LAYER_DOWN:{

                GRFX.renderer.translateFigureVertical(Settings.renderCubeSize);
                modelRenderBuilder.layerDown();

                break;
            }

            case RendererController.SHOW_GRID:{

                gridIsVisible = true;
                modelRenderBuilder.setSliced(true);
                break;
            }
            case RendererController.HIDE_GRID:{

                gridIsVisible = false;
                modelRenderBuilder.setSliced(false);
                break;
            }
            case RendererController.PAINT_CUBE:{

                actionWithCube = RendererController.PAINT_CUBE;
                break;

            }
            case RendererController.ADD_CUBE:{

                actionWithCube = RendererController.ADD_CUBE;
                break;

            }
            case RendererController.REMOVE_CUBE:{

                actionWithCube = RendererController.REMOVE_CUBE;
                break;

            }


        }

        GRFX.renderer.setFigureUniforms();
        GRFX.renderer.setFigureScaleFactor();

        automata.execute();
        modelRenderBuilder.draw();

        if(gridIsVisible){

            GRFX.renderer.setGridUniforms();
            GRFX.renderer.setGridScaleFactor();

            gridRenderBuilder.draw();
        }

        GRFX.activityListener.logFps(fps.frames());

    }


    @Override
    public void resize() {

        //not needed

    }


}
