package com.example.photoassistant;

import androidx.annotation.NonNull;

public class ListItemCombination extends ListItem {

    public static int resourceID = R.raw.combination;
    private String CompatibleLens;


    public ListItemCombination(String[] partName) {
        super(partName[1]);
        this.CompatibleLens = partName[0];

    }

    @Override
    protected String getPartName() {
        return partName;
    }


    @Override
    protected String TopLineText() {
        return super.TopLineText();
    }

    @Override
    protected String MiddleLineText() {
        return super.MiddleLineText();
    }

    @Override
    protected String BottomLineText() {
        return super.BottomLineText();
    }

    public String getCompatibleLens() {
        return CompatibleLens;
    }

    @NonNull
    @Override
    public String toString() {

        String retString ="";

        retString += getPartName();
        retString += getCompatibleLens();

        return retString;

    }
}
