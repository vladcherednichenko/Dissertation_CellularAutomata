package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {AutomataEntity.class}, version = 1)
public abstract class AutomataDatabase extends RoomDatabase{

    public abstract AutomataDao automataDao();

}
