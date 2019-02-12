package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.cellular.automata.cellularautomata.Settings;

import java.util.ArrayList;

public class DataBaseLoader {

    private final static DataBaseLoader instance = new DataBaseLoader();

    private static AutomataDatabase dataBase;

    private DataBaseLoader(){

    }

    public static DataBaseLoader getInstance(Context context){

        instance.dataBase = Room.databaseBuilder(context, AutomataDatabase.class, Settings.mainDataBaseName)
                .build();

        return instance;

    }

    public void loadAllAutomata(AllDataLoadedCallback callBack){

        new AllDataLoader(callBack).execute(null, null, null);

    }


    public AutomataEntity getAutomataById(int id){

        return dataBase.automataDao().getById(id);

    }

    public void insert(AutomataEntity entity, RowInserterCallback callback){

        new RowInserter(callback).execute(entity, null, null);

    }

    public void delete(int id){

        dataBase.automataDao().deleteByUserId(id);

    }

    public void delete(AutomataEntity entity){

        dataBase.automataDao().delete(entity);

    }


    // Interfaces

    public interface AllDataLoadedCallback {

        void onDataLoaded(ArrayList<AutomataEntity> dataList);

    }

    public interface RowInserterCallback {

        void onRowsInserted();

    }

    public interface RowDeleteCallback {

        void onRowsDeleted();

    }




    private static class RowInserter extends AsyncTask<AutomataEntity, Void, Void> {

        private RowInserterCallback callback;

        RowInserter(RowInserterCallback callback){

            this.callback = callback;

        }

        @Override
        protected Void doInBackground(AutomataEntity... automataEntities) {

            for(AutomataEntity entity: automataEntities){

                dataBase.automataDao().insert(entity);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(callback!= null){

                callback.onRowsInserted();

            }

        }
    }

    private static class RowDeleteTask extends AsyncTask<Void, Void, Void>{

        private RowDeleteCallback callback;

        public RowDeleteTask(RowDeleteTask callback){
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (UserModel model: modelList){
                mainDatabase.daoAccess().deleteUserModel(model.getId());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (callback!= null){
                callback.onUserModelsDeleted();
            }
        }
    }

    private static class AllDataLoader extends AsyncTask<Void, Void, Void> {

        private AllDataLoadedCallback callback;
        private ArrayList<AutomataEntity> automataList;

        AllDataLoader(AllDataLoadedCallback callback){

            this.callback = callback;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.automataList = new ArrayList<>(dataBase.automataDao().getAll());

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(callback!= null){

                callback.onDataLoaded(automataList);

            }

        }
    }

}
