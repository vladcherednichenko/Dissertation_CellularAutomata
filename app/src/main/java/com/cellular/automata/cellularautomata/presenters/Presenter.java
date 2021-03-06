package com.cellular.automata.cellularautomata.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.adapters.LoadScreenAdapter;
import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.data.Storage;
import com.cellular.automata.cellularautomata.database.DataBaseLoader;
import com.cellular.automata.cellularautomata.dialogs.AutomataLoadContextDialog;
import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.interfaces.ScreenshotListener;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.utils.ModelSaver;

public class Presenter {

    private String TAG = "Presenter";

    private MainView view;
    private DataBaseLoader DBmodel;

    private boolean isEditState = false;
    private boolean isGridVisible = false;

    private Storage storage = new Storage();
    private RendererController rendererController = new RendererController(storage);

    private final LoadScreenAdapter.RecyclerListener loadScreenRecyclerListener;

    private final AutomataLoadContextDialog.AutomataContextDialogListener loadAutomataContextMenuListener = new AutomataLoadContextDialog.AutomataContextDialogListener() {

        @Override
        public void onOpen(AutomataModel model) {

            storage.setCurrentModel(model);
            rendererController.loadModel();
            loadFragmentReturnPressed();

        }

        @Override
        public void onDelete(AutomataModel model) {

            DBmodel.delete(ModelSaver.modelToEntity(model), new DataBaseLoader.RowDeleteCallback() {
                @Override
                public void onRowsDeleted() {

                    storage.updateAllModels(DBmodel, new Storage.ModelsUpdatedCallback() {
                        @Override
                        public void onModelsUpdated() {

                            view.attachAdapter(storage.getLoadScreenAdapter(loadScreenRecyclerListener));

                        }
                    });

                }
            });

        }
    };


    public Presenter() {

        LINKER.rendererController = this.rendererController;

        loadScreenRecyclerListener = new LoadScreenAdapter.RecyclerListener() {
            @Override
            public void modelSelected(AutomataModel model) {

                AutomataLoadContextDialog contextDialog = new AutomataLoadContextDialog(view.getContext(), model);

                contextDialog.setListener(loadAutomataContextMenuListener);

                contextDialog.show();

            }

            @Override
            public void contextMenuCalled(AutomataModel model) {

            }

            @Override
            public Context getContext() {

                return view.getContext();

            }
        };

    }
    public void attachModel(DataBaseLoader model){

        this.DBmodel = model;

    }

    public void attachView (MainView view){

        this.view = view;

    }

    public void screenTouched(){

        view.hideInterface();

    }

    public void startPressed(){

        rendererController.startPressed();

    }

    public void pausePressed(){

        rendererController.pausePressed();

    }

    public void stopPressed(){

        rendererController.resetPressed();

    }

    public void nextStepPressed(){

        rendererController.nextStepPressed();

    }

    public void stretchPressed(){

        rendererController.stretchPressed();

    }

    public void squeezePressed(){

        rendererController.squeezePressed();

    }

    public void colorSelected(int color){

        rendererController.colorSelected(color);

    }

    public void layerUpPressed(){

        rendererController.layerUp();

    }

    public void layerDownPressed(){

        rendererController.layerDown();

    }

    public void savePressed(){

        view.hideControlsBar();
        view.hideToolbar();
        view.showProgressBar();

        view.logTextTop("Save pressed");

        LINKER.renderer.screenshot(new ScreenshotListener() {
            @Override
            public void onScreenShot(Bitmap screenshot) {

                storage.currentModel = LINKER.gameInstance.getCurrentModel();
                storage.currentModel.setScreenshot(screenshot);


                view.openSaveFragment(storage);
                view.hideProgressBar();

            }
        });


    }

    public void loadPressed(){

        if(Settings.log_top){ view.logTextTop("Load pressed"); }

        view.hideControlsBar();
        view.hideToolbar();
        view.showProgressBar();

        storage.checkIfModelsLoaded(DBmodel, new Storage.ModelsCheckCallBack() {
            @Override
            public void onModelsChecked() {

                view.openLoadFragment();

                view.attachAdapter(storage.getLoadScreenAdapter(loadScreenRecyclerListener));

                view.hideProgressBar();

            }
        });

    }

    public void editPressed(){

        isEditState = true;
        isGridVisible = false;
        rendererController.editModePressed();
        view.resetInterfaceToEdit();


    }

    public void closeEditPressed(){

        isEditState = false;
        isGridVisible = false;
        rendererController.vewModePressed();
        view.resetInterfaceToView();

    }

    public void gridButtonPressed(boolean gridVisible){

        isGridVisible = gridVisible;
        if(isGridVisible){
            rendererController.showGrid();
        }else{
            rendererController.hideGrid();
        }


    }

    public void settingsToolPressed(){

        view.hideControlsBar();
        view.hideToolbar();
        view.openSettingsFragment();

    }


    // Editor controls

    public void addCubePressed(){

        rendererController.addCubePressed();

    }

    public void removeCubePressed(){

        rendererController.removeCsubePressed();

    }

    public void paintCubePressed(){

        rendererController.paintCubePressed();

    }


    // Fragments
    // Fragment Save
    public void saveFragmentSavePressed(){

        String name = view.getSaveName();

        if(name.equals("") || storage == null) return;

        view.showProgressBar();

        storage.currentModel.setName(name);
        storage.currentModel.setScreenshotName(Settings.imagePrefix + name);

        ModelSaver.saveModel(storage.currentModel, storage, DBmodel, new ModelSaver.ModelSavedCallback() {
            @Override
            public void onModelSaved() {

                view.removeFragments();
                view.showControlsBar();
                view.showToolbar();
                view.hideProgressBar();

            }
        });

    }

    public void saveFragmentReturnPressed(){

        view.removeFragments();
        view.showControlsBar();
        view.showToolbar();

    }

    // Fragment Load
    public void loadFragmentReturnPressed(){

        view.removeFragments();
        view.showControlsBar();
        view.showToolbar();

    }

    // Fragment Settings
    public void settingsFragmentReturnPressed(){

        view.removeFragments();
        view.showControlsBar();
        view.showToolbar();

    }

    public void ruleChanged(Rule rule){

        storage.rule = rule;
        rendererController.updateRule();

    }

    public Rule getCurrentRule(){

        return LINKER.gameInstance.getCurrentRule();

    }

    public void automataRadiusChanged(){

        rendererController.updateRadius();

    }



}


