package com.example.photoassistant;

import androidx.annotation.NonNull;

/**
 * The list item class is an abstract class which is used
 * many functions to help with SE principles and polymorphism
 * as well as displaying each lens and body in the RecyclerView.
 * it is the base class for ListItemBody and ListItemLens, and
 * contains a few methods which are helpful to both lens and body.
 */

public abstract class ListItem {

    protected String partName;

    public ListItem(String partName){

        this.partName = partName;

    }


    protected abstract String getPartName();

    /**
     * @return string which depends on child class.
     *  to decide what each item displays in the Top
     *  line of the Recycler view default "".
     */

    protected String TopLineText(){return "";}
    /**
     @return string which depends on child class.
      *  to decide what each item displays in the middle
      *  line of the Recycler view default "".
     */


    protected String MiddleLineText(){return "";}
    /**
     @return string which depends on child class.
      *  to decide what each item displays in the middle
      *  line of the Recycler view default "".
     */


    protected String BottomLineText(){return "";}

    /**
     *
     * @return string which is used to print out the item.
     * has part name in it so child class can print easily.
     */

    @NonNull
    @Override
    public String toString() {
        String retString ="";

        retString += getPartName();

        return retString;
    }
}
