package com.cellular.automata.cellularautomata.presenters;

import android.graphics.Bitmap;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.core.RendererController;
import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.interfaces.ScreenshotListener;
import com.cellular.automata.cellularautomata.objects.Model;

public class Presenter {

    private MainView view;
    private boolean isEditState = false;

    private RendererController rendererController = new RendererController();

    public Presenter(){

        GRFX.rendererController = this.rendererController;

    }

    public void attachView (MainView view){

        this.view = view;

    }

    public void loadFragmentReturnPressed(){

        view.removeFragments();
        view.showControlsBar();
        view.showToolbar();

    }

    public void saveFragmentReturnPressed(){

        view.removeFragments();
        view.showControlsBar();
        view.showToolbar();

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

    public void savePressed(){

        view.hideControlsBar();
        view.hideToolbar();
        view.showProgressBar();

        view.logTextTop("Save pressed");

        GRFX.renderer.screenshot(new ScreenshotListener() {
            @Override
            public void onScreenShot(Bitmap bitmap) {

                Model model = new Model();
                view.openSaveFragment(model, bitmap);
                view.hideProgressBar();

            }
        });

//        ImageHelper.saveImage(image, "screen1", GRFX.activityListener.getContext(), new ImageHelper.SaveImageCallback() {
//            @Override
//            public void onImageSaved() {
//                Log.d(TAG, "image saved");
//            }
//        });




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

        rendererController.editModePressed();
        view.hideControlsBar();
        view.showEditBar();


    }

    public void closeEditPressed(){

        rendererController.vewModePressed();
        view.hideEditBar();
        view.showControlsBar();

    }

}
