package com.cellular.automata.cellularautomata.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "automatas")
public class AutomataEntity {


    @PrimaryKey(autoGenerate = true)
    @Nullable
    @ColumnInfo(name = "id")
    private Integer id;

    @Nullable
    @ColumnInfo(name = "name")
    private String name;

    @Nullable
    @ColumnInfo(name = "radius")
    private Integer radius;

    @Nullable
    @ColumnInfo(name = "iterationNumber")
    private Integer iterationNumber;

    @Nullable
    @ColumnInfo(name = "aliveCellNumber")
    private Integer aliveCellNumber;

    @Nullable
    @ColumnInfo(name = "rule")
    private String rule;

    @Nullable
    @ColumnInfo(name = "cubeMap")
    private String cubeMap;

    @Nullable
    @ColumnInfo(name = "screenshotName")
    private String screenshotName;

    @Nullable
    public Integer getId() { return id; }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public Integer getRadius() {
        return radius;
    }

    @Nullable
    public Integer getIterationNumber() {
        return iterationNumber;
    }

    @Nullable
    public Integer getAliveCellNumber() {
        return aliveCellNumber;
    }

    @Nullable
    public String getRule() {
        return rule;
    }

    @Nullable
    public String getCubeMap() {
        return cubeMap;
    }

    @Nullable
    public String getScreenshotName() {
        return screenshotName;
    }




    public void setId(@Nullable Integer id) { this.id = id; }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setRadius(@Nullable Integer radius) {
        this.radius = radius;
    }

    public void setIterationNumber(@Nullable Integer iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public void setAliveCellNumber(@Nullable Integer aliveCellNumber) {
        this.aliveCellNumber = aliveCellNumber;
    }

    public void setRule(@Nullable String rule) {
        this.rule = rule;
    }

    public void setCubeMap(@Nullable String cubeMap) {
        this.cubeMap = cubeMap;
    }

    public void setScreenshotName(@Nullable String screenshotName) {
        this.screenshotName = screenshotName;
    }
}
