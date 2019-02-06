package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class AutomataEntity {


    @PrimaryKey
    public int id;

    public String name;

    public int radius;

    public int iterationNumber;

    public int aliveCellNumber;

    public String rule;

    public String cubeMap;

    public String screenshotName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRadius() {
        return radius;
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public int getAliveCellNumber() {
        return aliveCellNumber;
    }

    public String getRule() {
        return rule;
    }

    public String getCubeMap() {
        return cubeMap;
    }

    public String getScreenshotName() {
        return screenshotName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public void setAliveCellNumber(int aliveCellNumber) {
        this.aliveCellNumber = aliveCellNumber;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setCubeMap(String cubeMap) {
        this.cubeMap = cubeMap;
    }

    public void setScreenshotName(String screenshotName) {
        this.screenshotName = screenshotName;
    }
}
