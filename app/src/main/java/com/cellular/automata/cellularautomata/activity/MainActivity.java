package com.cellular.automata.cellularautomata.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.core.InputCommander;
import com.cellular.automata.cellularautomata.interfaces.ActivityListener;
import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.SurfaceViewForAutomata;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.utils.TextResourceReader;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private String TAG = "Main Activity";

    private SurfaceViewForAutomata surfaceView;
    boolean isPlay = false;
    boolean colorBarOpened = false;
    boolean layerIconIsStretchedIcon = false;
    InputCommander inputCommander = new InputCommander();

    private LinearLayout toolBar, color_bar;
    private ImageView goButton, resetButton, speedUpButton, layersButton;
    private TextView txtLog, txtLogTop;
    private ColorPickerView colorPicker;

//    public interface ActivityInterface{
//
//        void goBtnPressed(boolean isPlay);
//        void resetBtnPressed();
//        void speedUpBtnPressed();
//        void colorPicked(int color);
//        void stretchFigure(boolean isStretch);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setActivityListener(this);
        GRFX.activityListener = this;

        CubeDataHolder.getInstance().facetListMedium = TextResourceReader.getFacetsFromFileObject(getApplicationContext(), "cube_medium.obj");
        CubeDataHolder.getInstance().setGraphicsQuality(CubeDataHolder.QUALITY_MEDIUM);


        goButton = findViewById(R.id.play_icon);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPlay = !isPlay;
                if (isPlay){

                    goButton.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                    inputCommander.startPressed();


                }else{

                    goButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                    inputCommander.pausePressed();

                }

            }
        });


        resetButton = findViewById(R.id.reset_icon);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCommander.resetPressed();
            }
        });

        speedUpButton = findViewById(R.id.speed_up_icon);
        speedUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCommander.nextStepPressed();
            }
        });

        toolBar = findViewById(R.id.tool_bar);
        txtLog = findViewById(R.id.txt_log_bottom);
        txtLogTop = findViewById(R.id.txt_log_top);
        color_bar = findViewById(R.id.color_bar);
        colorPicker = findViewById(R.id.color_picker_view);
        layersButton = findViewById(R.id.tool_layers);

        colorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                //Log.d(TAG, "onColorSelected: 0x" + Integer.toHexString(i));
                inputCommander.colorSelected(i);
            }
        });

        colorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                //Log.d(TAG, "onColorChanged: 0x" + Integer.toHexString(i));
                inputCommander.colorSelected(i);
            }
        });

        color_bar.setVisibility(View.INVISIBLE);
        toolBar.setVisibility(View.VISIBLE);

    }


    public void OnColorToolClicked(View v){

        hideToolbar();
        showColorPicker();

    }

    public void OnCloseColorBarClicked(View v){

        hideColorPicker();
        showToolBar();

    }

    public void OnLayersButtonPressed(View v){

        layerIconIsStretchedIcon = !layerIconIsStretchedIcon;
        if(layerIconIsStretchedIcon){

            layersButton.setImageDrawable(getResources().getDrawable(R.drawable.close_layers));
            inputCommander.stretchPressed();

        }else{

            layersButton.setImageDrawable(getResources().getDrawable(R.drawable.open_layers));
            inputCommander.squeezePressed();

        }


    }

    private void hideToolbar(){

        toolBar.startAnimation(getSlideLeftAnimation());
        toolBar.setVisibility(View.INVISIBLE);

    }

    private void showToolBar(){

        toolBar.setVisibility(View.VISIBLE);
        toolBar.startAnimation(getSlideRightAnimation());

    }

    private void showColorPicker(){

        color_bar.setVisibility(View.VISIBLE);
        color_bar.startAnimation(getSlideRightAnimation());
        colorBarOpened = true;

    }

    private void hideColorPicker(){

        color_bar.startAnimation(getSlideLeftAnimation());
        color_bar.setVisibility(View.INVISIBLE);
        colorBarOpened = false;

    }

    private Animation getSlideRightAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.toolbar_slide_right);

    }

    private Animation getSlideLeftAnimation(){

        return AnimationUtils.loadAnimation(getContext(),R.anim.toolbar_slide_left);

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
                txtLog.setText(text);
            }
        });

    }

    @Override
    public void loxTextTop(final String text) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLogTop.setText(text);
            }
        });

    }

    @Override
    public InputCommander getInputCommander() {
        return inputCommander;
    }


}
