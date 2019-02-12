package com.cellular.automata.cellularautomata.utils;

import android.util.Log;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.data.Storage;
import com.cellular.automata.cellularautomata.database.AutomataEntity;
import com.cellular.automata.cellularautomata.database.DataBaseLoader;
import com.cellular.automata.cellularautomata.objects.AutomataModel;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class ModelSaver{

    private static boolean baseSaved = false;
    private static boolean imageSaved = false;

    public interface ModelSavedCallback{

        void onModelSaved();

    }

    public static void saveModel(final AutomataModel automataModel, Storage storage, final DataBaseLoader base, final ModelSavedCallback callback){

        // Check if database is loaded

        storage.checkIfModelsLoaded(base, new Storage.ModelsCheckCallBack() {
            @Override
            public void onModelsChecked() {

                fdfdfbase.insert(modelToEntity(automataModel));
                processFinished(baseSaved, callback);

            }
        });

        ImageHelper.saveImage(storage.currentModel.getScreenshot(), "screen1", LINKER.activityListener.getContext(), new ImageHelper.SaveImageCallback() {
            @Override
            public void onImageSaved() {

                Log.d(TAG, "image saved");
                processFinished(imageSaved, callback);

            }
        });



    }

    private static void processFinished(boolean process, ModelSavedCallback callback){

        if(baseSaved && imageSaved && callback!=null){
            callback.onModelSaved();
        }

    }


    public static ArrayList<AutomataModel> automataEntityListToModel(ArrayList<AutomataEntity> entityList) {

        ArrayList<AutomataModel> automataModelList = new ArrayList<>();

        for(AutomataEntity entity: entityList){

            automataModelList.add(entityToModel(entity));

        }

        return automataModelList;

    }

    public static ArrayList<AutomataEntity> automataModelListToEntity(ArrayList<AutomataModel> modelList){

        ArrayList<AutomataEntity> entities = new ArrayList<>();

        for(AutomataModel model: modelList){

            entities.add(modelToEntity(model));

        }

        return entities;

    }

    public static AutomataModel entityToModel(AutomataEntity entity) {


        AutomataModel model = new AutomataModel();

        return model;

    }

    public static AutomataEntity modelToEntity(AutomataModel model){

        AutomataEntity entity = new AutomataEntity();

        return entity;

    }
}