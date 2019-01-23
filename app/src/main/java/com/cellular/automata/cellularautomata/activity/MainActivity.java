package com.cellular.automata.cellularautomata.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.presenters.Presenter;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.fragments.FragmentLoad;
import com.cellular.automata.cellularautomata.fragments.FragmentSave;
import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.SurfaceViewForAutomata;
import com.cellular.automata.cellularautomata.objects.Model;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;


public class MainActivity extends AppCompatActivity implements MainView {

    private String TAG = "Main Activity";

    private SurfaceViewForAutomata surfaceView;
    boolean isPlay = false;
    boolean colorBarOpened = false;
    boolean editIconPressed = false;
    boolean stretchIconIsStretched = false;

    private LinearLayout toolBar, colorBar, controlsBar, layersToolbar;
    private ImageView
            goButton, resetButton, nextStepButton,  // Control bar
            stretchButton, editButton, saveButton, loadButton,  // Main toolbar
            addCubeButton, removeCubeButton, paintButton,layerUpButton, layerDownButton, // Edit toolbar
            closeColorPickerButton;
    private TextView txtLogDown, txtLogTop, txtFpsCounter;
    private ColorPickerView colorPicker;
    private ProgressBar progressBar;

    private android.support.v4.app.FragmentManager fragmentManager;

    private FragmentSave saveFragment;
    private FragmentLoad loadFragment;

    private Presenter presenter;




    private View.OnTouchListener surfaceViewListener = new android.view.View.OnTouchListener() {
        @Override
        public boolean onTouch(android.view.View v, MotionEvent event) {

            presenter.screenTouched();
            return false;
        }

    };

    // Controls listeners
    private View.OnClickListener goButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {

            isPlay = !isPlay;
            if (isPlay){

                goButton.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                presenter.startPressed();


            }else{

                goButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                presenter.pausePressed();

            }

        }
    };

    private View.OnClickListener resetButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            presenter.stopPressed();
        }
    };

    private View.OnClickListener nextStepButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) { presenter.nextStepPressed(); }
    };



    // Toolbar listeners
    private View.OnClickListener editButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {

            editIconPressed = !editIconPressed;
            if (editIconPressed){

                editButton.setImageDrawable(getResources().getDrawable(R.drawable.edit_icon_cross));
                switchToolbarToEditMode(true);
                presenter.editPressed();


            }else{

                editButton.setImageDrawable(getResources().getDrawable(R.drawable.edit_icon));
                switchToolbarToEditMode(false);
                if(colorBarOpened){
                    hideColorPicker();
                }
                presenter.closeEditPressed();

            }

        }
    };

    private View.OnClickListener stretchButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            stretchIconIsStretched = !stretchIconIsStretched;
            if(stretchIconIsStretched){

                stretchButton.setImageDrawable(getResources().getDrawable(R.drawable.close_layers));
                presenter.stretchPressed();

            }else{

                stretchButton.setImageDrawable(getResources().getDrawable(R.drawable.open_layers));
                presenter.squeezePressed();

            }
        }
    };

    private View.OnClickListener loadButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            presenter.loadPressed();
        }
    };

    private View.OnClickListener saveButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            presenter.savePressed();
        }
    };


    // Edit bar listeners
    private View.OnClickListener addCubeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener removeCubeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            hideToolbar();
//            showColorPicker();
        }
    };

    private View.OnClickListener paintButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideEditBar();
            showColorPicker();
        }
    };

    private View.OnClickListener layerUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener layerDownListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    // Color picker listeners
    private OnColorSelectedListener onColorSelectedListener = new OnColorSelectedListener() {
        @Override
        public void onColorSelected(int i) {
            //Log.d(TAG, "onColorSelected: 0x" + Integer.toHexString(i));
            presenter.colorSelected(i);
        }
    };

    private OnColorChangedListener onColorChangeListener = new OnColorChangedListener() {
        @Override
        public void onColorChanged(int i) {
            //Log.d(TAG, "onColorChanged: 0x" + Integer.toHexString(i));
            presenter.colorSelected(i);
        }
    };

    private View.OnClickListener closeColorPickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideColorPicker();
            showEditBar();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setActivityListener(this);
        surfaceView.setOnTouchListener(surfaceViewListener);

        GRFX.activityListener = this;

        // FIND VIEWS
        toolBar = findViewById(R.id.tool_bar);
        controlsBar = findViewById(R.id.controls_bar);
        layersToolbar = findViewById(R.id.layers_navigation_toolbar);
        colorBar = findViewById(R.id.color_bar);
        // Controls bar
        goButton = findViewById(R.id.play_icon);
        resetButton = findViewById(R.id.reset_icon);
        nextStepButton = findViewById(R.id.next_step_icon);
        // Edit toolbar
        addCubeButton = findViewById(R.id.add_cube);
        removeCubeButton = findViewById(R.id.remove_cube);
        paintButton = findViewById(R.id.paint_cube);
        layerUpButton = findViewById(R.id.layers_up);
        layerDownButton = findViewById(R.id.layers_down);
        // Texts
        txtLogDown = findViewById(R.id.txt_log_bottom);
        txtLogTop = findViewById(R.id.txt_log_top);
        txtFpsCounter = findViewById(R.id.txt_fps_counter);
        // Main toolbar
        editButton = findViewById(R.id.tool_edit);
        stretchButton = findViewById(R.id.tool_layers);
        loadButton = findViewById(R.id.tool_load);
        saveButton= findViewById(R.id.tool_save);
        // Other
        progressBar = findViewById(R.id.progress_bar);
        closeColorPickerButton = findViewById(R.id.close_color_bar);

        // LISTENERS
        // Controls Bar
        goButton.setOnClickListener(goButtonListener);
        resetButton.setOnClickListener(resetButtonListener);
        nextStepButton.setOnClickListener(nextStepButtonListener);
        // Main toolbar
        editButton.setOnClickListener(editButtonListener);
        stretchButton.setOnClickListener(stretchButtonListener);
        loadButton.setOnClickListener(loadButtonListener);
        saveButton.setOnClickListener(saveButtonListener);
        // Edit bar
        addCubeButton.setOnClickListener(addCubeListener);
        removeCubeButton.setOnClickListener(removeCubeListener);
        paintButton.setOnClickListener(paintButtonListener);
        layerUpButton.setOnClickListener(layerUpListener);
        layerDownButton.setOnClickListener(layerDownListener);

        closeColorPickerButton.setOnClickListener(closeColorPickerListener);


        // Color picker
        colorPicker = findViewById(R.id.color_picker_view);
        colorPicker.addOnColorSelectedListener(onColorSelectedListener);
        colorPicker.addOnColorChangedListener(onColorChangeListener);

        // VISIBILITY
        colorBar.setVisibility(android.view.View.INVISIBLE);
        toolBar.setVisibility(android.view.View.VISIBLE);
        layersToolbar.setVisibility(View.INVISIBLE);
        controlsBar.setVisibility(View.VISIBLE);
        txtFpsCounter.setVisibility(Settings.log_fps_counter ? android.view.View.VISIBLE: android.view.View.INVISIBLE);
        txtLogDown.setVisibility(Settings.log_down? android.view.View.VISIBLE: android.view.View.INVISIBLE);
        txtLogTop.setVisibility(Settings.log_top? android.view.View.VISIBLE: android.view.View.INVISIBLE);

        fragmentManager = getSupportFragmentManager();
        presenter = new Presenter();
        presenter.attachView(this);

    }



    // support methods
    // utils

    private void showColorPicker(){

        colorBar.setVisibility(android.view.View.VISIBLE);
        colorBar.startAnimation(getRightSideSlideLeftAnimation());
        colorBarOpened = true;

    }

    private void hideColorPicker(){

        colorBar.startAnimation(getRightSideSlideRightAnimation());
        colorBar.setVisibility(android.view.View.INVISIBLE);
        colorBarOpened = false;

    }

    private Animation getLeftSideSlideRightAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.left_side_slide_right);

    }

    private Animation getLeftSideSlideLeftAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.left_side_slide_left);

    }

    private Animation getRightSideSlideRightAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.right_side_slide_right);

    }

    private Animation getRightSideSlideLeftAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.right_side_slide_left);

    }

    private Animation getFragmentSlideLeftAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.fragment_slide_left);

    }

    private Animation getFragmentBarSlideRightAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.fragment_slide_right);

    }

    private void loadLoadFragment(){

        removeFragments();

        loadFragment = new FragmentLoad();
        loadFragment.setPresenter(presenter);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_left, R.anim.fragment_slide_left);
        transaction.replace(R.id.fragment_frame, loadFragment, "SETS");
        transaction.commit();;

    }



    // View for presenter

    public void switchToolbarToEditMode(boolean isEditMode){

        if(isEditMode){

            int childAmount = toolBar.getChildCount();

            if(childAmount <= 1) return;

            toolBar.removeAllViews();
            toolBar.addView(editButton);

        }else{

            toolBar.addView(stretchButton);
            toolBar.addView(loadButton);
            toolBar.addView(saveButton);

        }

    }

    public void hideEditBar(){

        layersToolbar.startAnimation(getRightSideSlideRightAnimation());
        layersToolbar.setVisibility(android.view.View.INVISIBLE);

    }

    public void showEditBar(){

        layersToolbar.setVisibility(android.view.View.VISIBLE);
        layersToolbar.startAnimation(getRightSideSlideLeftAnimation());

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void logGesture(String text) {

    }

    @Override
    public void logText(final String text) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLogDown.setText(text);
            }
        });

    }

    @Override
    public void logTextTop(final String text) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLogTop.setText(text);
            }
        });

    }

    @Override
    public void logFps(final int fps) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtFpsCounter.setText("fps " + String.valueOf(fps));
            }
        });


    }

    @Override
    public void hideInterface() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(colorBarOpened){

                    hideColorPicker();

                    if(editIconPressed){

                        showEditBar();

                    }else{

                        showControlsBar();

                    }



                }


            }
        });

    }

    @Override
    public void openSaveFragment(final Model model, final Bitmap screenshot) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                saveFragment = new FragmentSave();
                saveFragment.setPresenter(presenter);
                saveFragment.setScreenshot(screenshot);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_slide_left, R.anim.fragment_slide_left);
                transaction.replace(R.id.fragment_frame, saveFragment, "SETS");
                transaction.commit();

            }
        });

    }

    public void openLoadFragment(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                loadFragment = new FragmentLoad();
                loadFragment.setPresenter(presenter);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_slide_left, R.anim.fragment_slide_left);
                transaction.replace(R.id.fragment_frame, loadFragment, "SETS");
                transaction.commit();

            }
        });

    }

    @Override
    public void removeFragments() {

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_slide_left, R.anim.fragment_slide_right);
            transaction.remove(fragment).commit();

        }

    }

    @Override
    public void hideControlsBar(){

        controlsBar.startAnimation(getRightSideSlideRightAnimation());
        controlsBar.setVisibility(android.view.View.INVISIBLE);

    }

    @Override
    public void showControlsBar(){

        controlsBar.setVisibility(android.view.View.VISIBLE);
        controlsBar.startAnimation(getRightSideSlideLeftAnimation());

    }

    public void hideToolbar(){

        toolBar.startAnimation(getLeftSideSlideLeftAnimation());
        toolBar.setVisibility(android.view.View.INVISIBLE);

    }

    public void showToolbar(){

        toolBar.setVisibility(android.view.View.VISIBLE);
        toolBar.startAnimation(getLeftSideSlideRightAnimation());

    }

    @Override
    public void showProgressBar() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void hideProgressBar() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

    }



}
