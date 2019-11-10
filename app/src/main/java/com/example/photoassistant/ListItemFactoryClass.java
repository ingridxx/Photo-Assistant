package com.example.photoassistant;
/**
 * This is a factory class which creates ListItems from the values of array.
 * It is useful as it encapsulates data so the calling class only needs to give it an array of values
 * and what you want and itt will handle all calling and return the needed object.
 */
public class ListItemFactoryClass {

    /**
     * static method which handles the factory design pattern implementation.
     *
     * @param type is the type of ListItem child class you want as string
     *
     * @param array is the array of values to instansiate the object with
     *
     * @return an instance of the object, if a correct item was passed
     * through type otherwise return null.
     */

    public static ListItem getListItemInstance(String type, String[] array){


        if("Body".equalsIgnoreCase(type)){ return new ListItemBody(type, array);}

        else if("Lens".equalsIgnoreCase(type)){ return new ListItemLens(type,array);}

        else if("Combo".equalsIgnoreCase(type)){
            int i=0;
            while (i != MainActivity.lens_al.size()){
                if (array[0].equals(MainActivity.lens_al.get(i).partName)) {
                    ListItem listItem = MainActivity.lens_al.get(i);
                    return listItem;
                }
                i++;
            }
        }

        return null;
    }
}
