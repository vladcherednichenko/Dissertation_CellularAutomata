package com.cellular.automata.cellularautomata.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private ActivityInterface applicationManager;
    boolean isPlay = false;
    boolean colorBarOpened = false;

    private LinearLayout toolBar, color_bar;
    private ImageView goButton, resetButton, speedUpButton;
    private TextView txtLog, txtLogTop;
    private ColorPickerView colorPicker;

    public interface ActivityInterface{

        void goBtnPressed(boolean isPlay);
        void resetBtnPressed();
        void speedUpBtnPressed();
        void colorPicked(int color);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setActivityListener(this);
        this.applicationManager = GRFX.appManager;
        GRFX.activityListener = this;

        CubeDataHolder.getInstance().facetListMedium = TextResourceReader.getFacetsFromFileObject(getApplicationContext(), "cube_medium.obj");
        CubeDataHolder.getInstance().setGraphicsQuality(CubeDataHolder.QUALITY_MEDIUM);


        goButton = findViewById(R.id.play_icon);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationManager!= null){

                    isPlay = !isPlay;
                    if (isPlay){

                        goButton.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));


                    }else{
                        goButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                    }

                    applicationManager.goBtnPressed(isPlay);
                }
            }
        });

//        color_picker = findViewById(R.id.color_picker);
//        color_picker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ColorPickerDialogBuilder
//                        .with(MainActivity.this)
//                        .setTitle("Choose color")
//                        .initialColor(Color.BLUE)
//                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).
//                        lightnessSliderOnly()
//                        .density(12)
//                        .setOnColorSelectedListener(new OnColorSelectedListener() {
//                            @Override
//                            public void onColorSelected(int selectedColor) {
//
//                                Log.d(TAG, "onColorSelected: 0x" + Integer.toHexString(selectedColor));
//                            }
//                        })
//                        .setPositiveButton("ok", new ColorPickerClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//
//                                Log.d(TAG, "ok clicked");
//                                //changeBackgroundColor(selectedColor);
//                            }
//                        })
//                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Log.d(TAG, "cancel clicked");
//
//                            }
//                        })
//                        .build()
//                        .show();
//
//
//                //applicationManager.colorPicked();
//            }
//        });

        resetButton = findViewById(R.id.reset_icon);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationManager!= null){
                    applicationManager.resetBtnPressed();
                }
            }
        });

        speedUpButton = findViewById(R.id.speed_up_icon);
        speedUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationManager!= null){
                    applicationManager.speedUpBtnPressed();
                }
            }
        });

        toolBar = findViewById(R.id.tool_bar);
        txtLog = findViewById(R.id.txt_log_bottom);
        txtLogTop = findViewById(R.id.txt_log_top);
        color_bar = findViewById(R.id.color_bar);
        colorPicker = findViewById(R.id.color_picker_view);

        colorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                Log.d(TAG, "onColorSelected: 0x" + Integer.toHexString(i));
            }
        });

        colorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                Log.d(TAG, "onColorChanged: 0x" + Integer.toHexString(i));
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
    public void logText(String text) {

        txtLog.setText(text);

    }
}
