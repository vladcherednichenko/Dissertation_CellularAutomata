package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.cellular.automata.cellularautomata.Settings;

import java.util.ArrayList;

public class DataBaseLoader {

    private final static DataBaseLoader instance = new DataBaseLoader();

    private AutomataDatabase dataBase;

    private DataBaseLoader(){

    }

    public DataBaseLoader getInstance(Context context){

        instance.dataBase = Room.databaseBuilder(context, AutomataDatabase.class, Settings.mainDataBaseName)
                .build();

        return instance;

    }

    public ArrayList<AutomataEntity> getAllAutomata(){

        return new ArrayList<>(dataBase.automataDao().getAll());

    }


    public AutomataEntity getAutomataById(int id){

        return dataBase.automataDao().getById(id);

    }

    public void insert(AutomataEntity entity){

        dataBase.automataDao().insert(entity);

    }

    public void delete(int id){

        dataBase.automataDao().deleteByUserId(id);

    }

    public void delete(AutomataEntity entity){

        dataBase.automataDao().delete(entity);

    }




}
