package com.cellular.automata.cellularautomata.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cellular.automata.cellularautomata.presenters.Presenter;
import com.cellular.automata.cellularautomata.R;


public class FragmentSave extends Fragment {

    private Presenter presenter;

    private View view;

    private ImageView imgReturn, imgScreenshot;

    private Bitmap screenshot;

    private View.OnClickListener onReturnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.saveFragmentReturnPressed();
        }
    };


    public void setPresenter(Presenter p){

        this.presenter = p;

    }

    public void setScreenshot(Bitmap screenshot) {

        this.screenshot = screenshot;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_save, container, false);

        imgReturn = view.findViewById(R.id.ic_back);
        imgReturn.setOnClickListener(onReturnClickListener);
        imgScreenshot = view.findViewById(R.id.img_screenshot);

        if(screenshot != null){
            imgScreenshot.setImageBitmap(screenshot);
        }




        return view;

    }



}
