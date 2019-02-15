package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.cellular.automata.cellularautomata.Settings;

import java.util.ArrayList;

public class DataBaseLoader {

    private final static DataBaseLoader instance = new DataBaseLoader();

    private static AutomataDatabase dataBase;

    private DataBaseLoader() {

    }

    public static DataBaseLoader getInstance(Context context){

        instance.dataBase = Room.databaseBuilder(
                context,
                AutomataDatabase.class,
                Settings.mainDataBaseName)
                .build();

        return instance;

    }


    // Main
    // Ready-to use methods

    public void loadAllAutomata(AllDataLoadedCallback callBack){

        new AllDataLoader(callBack).execute(null, null, null);

    }

    public void insert(AutomataEntity entity, RowInserterCallback callback){

        new RowInserter(callback).execute(entity, null, null);

    }

    public void delete(AutomataEntity entity, RowDeleteCallback callback){

        new RowDeleteTask(callback).execute(entity);

    }



    // rewrite
    public AutomataEntity getAutomataById(int id){

        return dataBase.automataDao().getById(id);

    }

    public void delete(int id){

        dataBase.automataDao().deleteByUserId(id);

    }



    // INTERFACES

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

                if(entity!= null)
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

    private static class RowDeleteTask extends AsyncTask<AutomataEntity, Void, Void>{

        private RowDeleteCallback callback;

        public RowDeleteTask(RowDeleteCallback callback){
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(AutomataEntity... automatas) {

            for (AutomataEntity entity: automatas){

                dataBase.automataDao().delete(entity);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (callback!= null){
                callback.onRowsDeleted();
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
            if(callback != null) {
                callback.onDataLoaded(automataList);
            }

        }
    }

}
