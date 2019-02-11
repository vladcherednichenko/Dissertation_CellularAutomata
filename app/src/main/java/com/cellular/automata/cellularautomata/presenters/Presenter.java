package com.cellular.automata.cellularautomata.presenters;

import android.graphics.Bitmap;
import android.util.Log;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.data.Storage;
import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.interfaces.ScreenshotListener;
import com.cellular.automata.cellularautomata.utils.ImageHelper;

public class Presenter {

    private String TAG = "Presenter";

    private MainView view;
    private boolean isEditState = false;
    private boolean isGridVisible = false;
    private Storage storage = new Storage();

    private RendererController rendererController = new RendererController();

    public Presenter(){

        LINKER.rendererController = this.rendererController;

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
        view.openLoadFragment();
        view.hideProgressBar();


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

        storage.currentModel.setName(name);

        ImageHelper.saveImage(storage.currentModel.getScreenshot(), "screen1", LINKER.activityListener.getContext(), new ImageHelper.SaveImageCallback() {
            @Override
            public void onImageSaved() {

                Log.d(TAG, "image saved");

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
    

}
