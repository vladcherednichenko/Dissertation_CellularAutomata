package com.cellular.automata.cellularautomata.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cellular.automata.cellularautomata.presenters.Presenter;
import com.cellular.automata.cellularautomata.R;


public class FragmentLoad extends Fragment {

    private Presenter presenter;

    private View view;

    private ImageView imgReturn;


    private View.OnClickListener onReturnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.loadFragmentReturnPressed();
        }
    };


    public void setPresenter(Presenter p){

        this.presenter = p;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_load, container, false);

        imgReturn = view.findViewById(R.id.ic_back);
        imgReturn.setOnClickListener(onReturnClickListener);


        return view;

    }



}
