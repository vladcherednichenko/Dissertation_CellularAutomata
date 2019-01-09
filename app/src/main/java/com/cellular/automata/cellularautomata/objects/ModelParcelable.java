package com.cellular.automata.cellularautomata.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelParcelable implements Parcelable{

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public ModelParcelable(String name, String imageName, int blockNumber, int iterationNumber) {
        this.name = name;
        this.imageName = imageName;
        this.blockNumber = blockNumber;
    }

    public ModelParcelable(Parcel in){

        name = in.readString();
        imageName = in.readString();
        blockNumber = in.readInt();

    }

    private String name;
    private String imageName;
    private int blockNumber;

    public ModelParcelable(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(imageName);
        dest.writeInt(blockNumber);


    }

    public static final Creator<ModelParcelable> CREATOR = new Creator<ModelParcelable>() {
        @Override
        public ModelParcelable createFromParcel(Parcel in) {
            return new ModelParcelable(in);
        }

        @Override
        public ModelParcelable[] newArray(int size) {
            return new ModelParcelable[size];
        }
    };
}
