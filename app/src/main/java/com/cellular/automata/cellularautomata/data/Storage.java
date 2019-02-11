package com.cellular.automata.cellularautomata.data;

import android.util.Log;

import com.cellular.automata.cellularautomata.objects.AutomataModel;

import java.util.ArrayList;

public class Storage {

    private String TAG = "Storage";

    public AutomataModel currentModel;

    public ArrayList<AutomataModel> allModels;


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

}
