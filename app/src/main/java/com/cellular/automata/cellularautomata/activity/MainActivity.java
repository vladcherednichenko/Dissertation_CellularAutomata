package com.cellular.automata.cellularautomata.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.interfaces.ActivityListener;
import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.SurfaceViewForAutomata;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.utils.TextResourceReader;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private SurfaceViewForAutomata surfaceView;
    private ActivityInterface applicationManager;
    boolean isPlay = false;

    private ImageView goButton, resetButton, speedUpButton;
    private TextView txtLog;

    public interface ActivityInterface{

        void goBtnPressed(boolean isPlay);
        void resetBtnPressed();
        void speedUpBtnPressed();

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

        txtLog = findViewById(R.id.txt_log);


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
