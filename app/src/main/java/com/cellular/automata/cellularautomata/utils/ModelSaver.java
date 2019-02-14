package com.cellular.automata.cellularautomata.utils;

import android.util.Log;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.Storage;
import com.cellular.automata.cellularautomata.database.AutomataEntity;
import com.cellular.automata.cellularautomata.database.DataBaseLoader;
import com.cellular.automata.cellularautomata.objects.AutomataModel;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class ModelSaver{

    private static String TAG = "ModelServer";

    private static boolean imageSaved = false;
    private static boolean baseSaved = false;

    private static int PROCESS_IMAGE_SAVE = 0;
    private static int PROCESS_BASE_SAVE = 1;

    public interface ModelSavedCallback{

        void onModelSaved();

    }

    public static void saveModel(final AutomataModel automataModel, final Storage storage, final DataBaseLoader base, final ModelSavedCallback callback){

        // Check if database is loaded
        storage.checkIfModelsLoaded(base, new Storage.ModelsCheckCallBack() {
            @Override
            public void onModelsChecked() {

                base.insert(modelToEntity(automataModel), new DataBaseLoader.RowInserterCallback() {
                    @Override
                    public void onRowsInserted() {

                        storage.updateAllModels(base, new Storage.ModelsUpdatedCallback() {
                            @Override
                            public void onModelsUpdated() {

                                processFinished(PROCESS_BASE_SAVE, callback);

                            }
                        });

                    }
                });

            }
        });

        ImageHelper.saveImage(storage.currentModel.getScreenshot(), automataModel.getScreenshotName(), LINKER.activityListener.getContext(), new ImageHelper.SaveImageCallback() {
            @Override
            public void onImageSaved() {

                Log.d(TAG, "image saved");
                processFinished(PROCESS_IMAGE_SAVE, callback);

            }
        });



    }

    private static void processFinished(int process, ModelSavedCallback callback){

        if(process == PROCESS_BASE_SAVE) baseSaved = true;
        if(process == PROCESS_IMAGE_SAVE) imageSaved = true;

        if(imageSaved && baseSaved && callback!=null){

            imageSaved = false;
            baseSaved = false;
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

        model.setName(entity.getName());
        model.setScreenshotName(entity.getScreenshotName());
        model.setIteration(entity.getIterationNumber());
        model.setAliveNumber(entity.getAliveCellNumber());
        model.setMap(CubeMap.fromString(entity.getCubeMap(), entity.getRadius()));
        model.setRule(entity.getRule());


        return model;

    }

    public static AutomataEntity modelToEntity(AutomataModel model){

        AutomataEntity entity = new AutomataEntity();

        entity.setName(model.getName());
        entity.setAliveCellNumber(model.getAliveNumber());
        entity.setIterationNumber(model.getIteration());
        entity.setRadius(model.getRadius());
        entity.setScreenshotName(model.getScreenshotName());
        entity.setRule(model.getRule());
        entity.setCubeMap(model.toString());

        return entity;

    }

}