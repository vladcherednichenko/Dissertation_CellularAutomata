package com.cellular.automata.cellularautomata;


import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.LifeRule;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.data.Automata;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.objects.GridRenderBuilder;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
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

    private AutomataModel testModel;

    @Override
    public void create() {

        rendererController = LINKER.rendererController;
        environment = new Environment();
        automata = new Automata();

        testModel = AutomataModel.fromCoordsArray(Settings.testSimpleCube, Settings.automataRadius);

        automata.setModel(testModel);
        automata.setRule(new LifeRule());

        modelRenderBuilder = automata.getModelRenderBuilder();
        environment.addBuilder(modelRenderBuilder);

        modelRenderBuilder.setOnTouchListener(new ModelRenderBuilder.OnTouchListener() {
            @Override
            public void onTouch() {

                if(LINKER.activityListener!= null){
                    //LINKER.activityListener.logText("nb: " + String.valueOf(rule.getNeighboursAmount(new RenderCube(modelRenderBuilder.getTouchResult().touchedCubeCenter, null, false), modelRenderBuilder.getRenderMap())));
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

                testModel = AutomataModel.fromCoordsArray(Settings.testSimpleCube, Settings.automataRadius);
                automata.setModel(testModel);
                LINKER.renderer.resetCam();
                break;

            }
            case RendererController.NEXT:{

                automata.next();
                break;


            }

            case RendererController.LOAD_MODEL: {

                automata.setModel(rendererController.storage.currentModel);
                LINKER.renderer.resetCam();
                break;
            }

            case RendererController.LOAD_RULE:{

                automata.setRule(rendererController.storage.rule);

            }

            case RendererController.UPDATE_RADIUS:{

                automata.setRadius(Settings.automataRadius);
                if(gridRenderBuilder!= null) gridRenderBuilder.updateGridRadius(Settings.automataRadius);

            }

            //when the figure is touched
            case RendererController.FIGURE_TOUCHED:{

                if(isViewMode) return;

                //adding / painting / deleting a cube
                if(modelRenderBuilder.isTouched()){

                    String color = Integer.toHexString(rendererController.currentColor);

                    if(color.length()>=6){ color = "#" + color.substring(2); }

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
                LINKER.renderer.resetAdditionalStride();
                break;
            }
            case RendererController.LAYER_UP:{

                modelRenderBuilder.layerUp();
                gridRenderBuilder.translateGrid((int)Settings.renderCubeSize);

                break;
            }

            case RendererController.LAYER_DOWN:{

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

        LINKER.renderer.setFigureUniforms();
        LINKER.renderer.setFigureScaleFactor();

        automata.execute();
        modelRenderBuilder.draw();

        if(gridIsVisible){

            LINKER.renderer.setGridUniforms();
            LINKER.renderer.setGridScaleFactor();

            gridRenderBuilder.draw();
        }

        LINKER.activityListener.logFps(fps.frames());

    }

    public AutomataModel getCurrentModel(){

        if(automata == null) return null;

        return automata.getModel();

    }

    public Rule getCurrentRule(){

        if(automata == null) return null;

        return automata.getRule();

    }


}
