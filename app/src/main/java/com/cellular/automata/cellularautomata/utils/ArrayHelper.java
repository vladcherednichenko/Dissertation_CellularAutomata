package com.cellular.automata.cellularautomata.utils;

public class ArrayHelper {

    public static int [] stringToIntArray(String string, String delimeter) {

        String [] stringArray = string.split(delimeter);

        int [] intArray = new int [stringArray.length];

        for (int i = 0; i< stringArray.length; i++){

            intArray[i] = Integer.valueOf(stringArray[i]);

        }

        return intArray;

    }

    public static String intArrayToString(int [] array, String delimeter){

        StringBuilder sb = new StringBuilder();

        for(int i: array){

            sb.append(String.valueOf(i)).append(delimeter);

        }

        sb.deleteCharAt(sb.length()-1);

        return sb.toString();

    }

}
