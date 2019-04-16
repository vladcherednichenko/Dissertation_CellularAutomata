package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AutomataDao {

    @Query("SELECT * FROM automatas")
    List<AutomataEntity> getAll();

    @Query("SELECT * FROM automatas WHERE id = :id")
    AutomataEntity getById(int id);

    @Insert
    void insert(AutomataEntity automata);


    @Query("DELETE FROM automatas WHERE id = :automataId")
    void deleteByUserId(long automataId);

    @Query("DELETE FROM automatas WHERE name = :name")
    void deleteByUserName(String name);

    @Delete
    void delete(AutomataEntity automata);




}
