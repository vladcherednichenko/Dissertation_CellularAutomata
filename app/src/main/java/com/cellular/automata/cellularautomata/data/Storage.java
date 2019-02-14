package com.cellular.automata.cellularautomata.data;

import android.util.Log;

import com.cellular.automata.cellularautomata.adapters.LoadScreenAdapter;
import com.cellular.automata.cellularautomata.database.AutomataEntity;
import com.cellular.automata.cellularautomata.database.DataBaseLoader;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.utils.ModelSaver;

import java.util.ArrayList;

public class Storage {

    private String TAG = "Storage";

    public AutomataModel currentModel;

    public ArrayList<AutomataModel> allModels;

    private LoadScreenAdapter loadScreenAdapter;


    // Callbacks
    public interface ModelsCheckCallBack {

        void onModelsChecked();

    }

    public interface ModelsUpdatedCallback{

        void onModelsUpdated();

    }


    // Stuff
    public boolean automataNameExists(String name){

        if(allModels == null) {

            Log.d(TAG, "models array not initialized");
            return true;

        }

        for(AutomataModel model: allModels){

            if(model.getName().equals(name)){
                return true;
            }

        }

        return false;

    }

    // if model list is not created - loads all automata models or creates an empty list
    public void checkIfModelsLoaded(DataBaseLoader base, final ModelsCheckCallBack callback){

        if(allModels!= null) {

            callback.onModelsChecked();
            return;

        }

        updateAllModels(base, new ModelsUpdatedCallback() {
            @Override
            public void onModelsUpdated() {

                callback.onModelsChecked();

            }
        });

    }

    public void updateAllModels(DataBaseLoader base, final ModelsUpdatedCallback callback){

        base.loadAllAutomata(new DataBaseLoader.AllDataLoadedCallback() {
            @Override
            public void onDataLoaded(ArrayList<AutomataEntity> dataList) {

                if(dataList == null || dataList.size() == 0){

                    allModels = new ArrayList<>();

                }else{

                    allModels = ModelSaver.automataEntityListToModel(dataList);

                }

                if(callback!= null){

                    callback.onModelsUpdated();

                }
            }
        });

    }


    public LoadScreenAdapter getLoadScreenAdapter(LoadScreenAdapter.RecyclerListener listener){

        if(loadScreenAdapter == null){

            loadScreenAdapter = new LoadScreenAdapter(allModels);

        }else{

            loadScreenAdapter.setModels(allModels);

        }

        loadScreenAdapter.setListener(listener);

        return loadScreenAdapter;

    }




}
