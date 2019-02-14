package com.cellular.automata.cellularautomata.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cellular.automata.cellularautomata.adapters.LoadScreenAdapter;
import com.cellular.automata.cellularautomata.presenters.Presenter;
import com.cellular.automata.cellularautomata.R;


public class FragmentLoad extends Fragment {

    private Presenter presenter;

    private View view;

    private ImageView imgReturn;

    private RecyclerView automataModelsList;

    private LoadScreenAdapter adapter;


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
        automataModelsList = view.findViewById(R.id.load_recycler_view);

        if(adapter != null){attachAdapter(adapter);}

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void attachAdapter(LoadScreenAdapter adapter){

        if(automataModelsList == null) {
            this.adapter = adapter;
            return;
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        automataModelsList.setLayoutManager(mLayoutManager);
        automataModelsList.setItemAnimator(new DefaultItemAnimator());
        automataModelsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }




}
