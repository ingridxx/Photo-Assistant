package com.example.photoassistant;


import androidx.annotation.NonNull;

public abstract class ListItem {

    protected String partName;

    public static int numFields;

    public ListItem(String partName){

        this.partName = partName;

    }


    protected abstract String getPartName();

    protected String TopLineText(){return "";}

    protected String MiddleLineText(){return "";}

    protected String BottomLineText(){return "";}

    @NonNull
    @Override
    public String toString() {
        String retString ="";

        retString += getPartName();

        return retString;
    }
}
