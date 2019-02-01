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
    private boolean isViewMode = true;

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

                if(isViewMode) return;

                //adding / painting / deleting a cube
                if(modelRenderBuilder.isTouched()){

                    String color = Integer.toHexString(rendererController.currentColor);

                    if(color.length()>=6){ color = "#" + color.substring(2); }

                    //Log.d(TAG, String.valueOf(rule.getNeighboursAmount(new RenderCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, null, false), modelRenderBuilder.getRenderMap())));
                    if(actionWithCube == RendererController.ADD_CUBE){

                        if(!modelRenderBuilder.cubeInCurrentOpenedLayer(modelRenderBuilder.getTouchResult().newCubeCenter)) break;

                        Cube cube = new Cube(color, modelRenderBuilder.getTouchResult().newCubeCenter);
                        cube.setAlive(true);

                        automata.addNewCube(cube);

                    }
                    if(actionWithCube == RendererController.REMOVE_CUBE){

                        if(!modelRenderBuilder.cubeInCurrentOpenedLayer(modelRenderBuilder.getTouchResult().touchedCubeCenter)) break;

                        automata.removeCube(new Cube(modelRenderBuilder.getTouchResult().touchedCubeCenter));

                    }
                    if(actionWithCube == RendererController.PAINT_CUBE){

                        if(!modelRenderBuilder.cubeInCurrentOpenedLayer(modelRenderBuilder.getTouchResult().touchedCubeCenter)) break;

                        automata.paintCube(new Cube(color, modelRenderBuilder.getTouchResult().touchedCubeCenter));

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
                this.isViewMode = false;

                if(gridRenderBuilder == null){
                    gridRenderBuilder = new GridRenderBuilder(automata.getAutomataRadius());
                }

                gridRenderBuilder.build();
                gridRenderBuilder.bindAttributesData();

                gridRenderBuilder.reset();
                environment.addGrid(gridRenderBuilder);

                break;

            }
            case RendererController.VIEW_MODE:{

                this.isViewMode = true;
                modelRenderBuilder.setViewMode(true);
                gridIsVisible = false;
                environment.removeGrids();
                GRFX.renderer.resetAdditionalStride();
                break;
            }
            case RendererController.LAYER_UP:{

                //GRFX.renderer.translateFigureVertical(-Settings.renderCubeSize);
                modelRenderBuilder.layerUp();
                gridRenderBuilder.translateGrid((int)Settings.renderCubeSize);

                break;
            }

            case RendererController.LAYER_DOWN:{

                //GRFX.renderer.translateFigureVertical(Settings.renderCubeSize);
                modelRenderBuilder.layerDown();
                gridRenderBuilder.translateGrid(-(int)Settings.renderCubeSize);

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
