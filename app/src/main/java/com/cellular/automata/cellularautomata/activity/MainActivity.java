package com.cellular.automata.cellularautomata.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
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

import com.cellular.automata.cellularautomata.Presenter;
import com.cellular.automata.cellularautomata.Settings;
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
    boolean layerIconIsStretchedIcon = false;

    private LinearLayout toolBar, colorBar, controlsBar;
    private ImageView goButton, resetButton, speedUpButton, layersButton;
    private TextView txtLogDown, txtLogTop, txtFpsCounter;
    private ColorPickerView colorPicker;
    private ProgressBar progressBar;

    private android.support.v4.app.FragmentManager fragmentManager;
    private FragmentSave saveFragment;

    private Presenter presenter;




    View.OnTouchListener surfaceViewListener = new android.view.View.OnTouchListener() {
        @Override
        public boolean onTouch(android.view.View v, MotionEvent event) {

            presenter.screenTouched();
            return false;
        }

    };

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

    private View.OnClickListener speedUpButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) { presenter.nextStepPressed(); }
    };

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

        goButton = findViewById(R.id.play_icon);
        goButton.setOnClickListener(goButtonListener);

        resetButton = findViewById(R.id.reset_icon);
        resetButton.setOnClickListener(resetButtonListener);

        speedUpButton = findViewById(R.id.speed_up_icon);
        speedUpButton.setOnClickListener(speedUpButtonListener);

        toolBar = findViewById(R.id.tool_bar);
        controlsBar = findViewById(R.id.controls_bar);
        txtLogDown = findViewById(R.id.txt_log_bottom);
        txtLogTop = findViewById(R.id.txt_log_top);
        txtFpsCounter = findViewById(R.id.txt_fps_counter);
        colorBar = findViewById(R.id.color_bar);
        layersButton = findViewById(R.id.tool_layers);
        progressBar = findViewById(R.id.progress_bar);

        // Color picker
        colorPicker = findViewById(R.id.color_picker_view);
        colorPicker.addOnColorSelectedListener(onColorSelectedListener);
        colorPicker.addOnColorChangedListener(onColorChangeListener);

        // Set default visibility
        colorBar.setVisibility(android.view.View.INVISIBLE);
        toolBar.setVisibility(android.view.View.VISIBLE);
        txtFpsCounter.setVisibility(Settings.fpsCounter? android.view.View.VISIBLE: android.view.View.INVISIBLE);
        txtLogDown.setVisibility(Settings.log_down? android.view.View.VISIBLE: android.view.View.INVISIBLE);
        txtLogTop.setVisibility(Settings.log_top? android.view.View.VISIBLE: android.view.View.INVISIBLE);

        fragmentManager = getSupportFragmentManager();
        presenter = new Presenter();
        presenter.attachView(this);

    }

    // buttons clicked
    // onClick methods

    public void OnSaveToolClicked(android.view.View v){

        presenter.savePressed();

    }

    public void OnLoadToolClicked(android.view.View v){

        presenter.loadPressed();

    }

    public void OnColorToolClicked(android.view.View v){

        hideToolbar();
        showColorPicker();

    }

    public void OnCloseColorBarClicked(android.view.View v){

        hideColorPicker();
        showToolbar();

    }

    public void OnLayersButtonClicked(android.view.View v){

        layerIconIsStretchedIcon = !layerIconIsStretchedIcon;
        if(layerIconIsStretchedIcon){

            layersButton.setImageDrawable(getResources().getDrawable(R.drawable.close_layers));
            presenter.stretchPressed();

        }else{

            layersButton.setImageDrawable(getResources().getDrawable(R.drawable.open_layers));
            presenter.squeezePressed();

        }


    }

    //support methods
    // utils

    private void showColorPicker(){

        colorBar.setVisibility(android.view.View.VISIBLE);
        colorBar.startAnimation(getSlideRightAnimation());
        colorBarOpened = true;

    }

    private void hideColorPicker(){

        colorBar.startAnimation(getSlideLeftAnimation());
        colorBar.setVisibility(android.view.View.INVISIBLE);
        colorBarOpened = false;

    }

    private Animation getSlideRightAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.toolbar_slide_right);

    }

    private Animation getSlideLeftAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.toolbar_slide_left);

    }

    private void loadSaveFragment(){

        saveFragment = new FragmentSave();
        saveFragment.setPresenter(presenter);
        fragmentManager.beginTransaction().add(R.id.fragment_frame, saveFragment, "CATEGORIES").commit();

//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.fade_in, R.anim.menu_slide_out);
//        transaction.replace(R.id.fragment_container, setsFragment, "SETS");
//        transaction.commit();

    }



    // View for presenter

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
                    showToolbar();
                }


            }
        });

    }

    @Override
    public void openSaveActivity(final Model model, final Bitmap screenShot) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                loadSaveFragment();

//                ModelParcelable pModel = new ModelParcelable("123", "path", 1, 1);
//                Intent intent = new Intent(MainActivity.this, SaveActivity.class);
//                //intent.putExtra("SCREEN", screenShot);
//                intent.putExtra("MODEL", pModel);
//                startActivity(intent);

            }
        });


    }

    @Override
    public void hideFragments() {

        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

    }

    @Override
    public void hideControlsBar(){

        controlsBar.startAnimation(getSlideLeftAnimation());
        controlsBar.setVisibility(android.view.View.INVISIBLE);

    }

    @Override
    public void showControlsBar(){

        controlsBar.setVisibility(android.view.View.VISIBLE);
        controlsBar.startAnimation(getSlideRightAnimation());

    }

    public void hideToolbar(){

        toolBar.startAnimation(getSlideLeftAnimation());
        toolBar.setVisibility(android.view.View.INVISIBLE);

    }

    public void showToolbar(){

        toolBar.setVisibility(android.view.View.VISIBLE);
        toolBar.startAnimation(getSlideRightAnimation());

    }

    @Override
    public void showProgressBar() {

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressBar() {

        progressBar.setVisibility(View.INVISIBLE);

    }



}
