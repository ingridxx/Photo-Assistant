package com.example.photoassistant;

public class ListItemFactoryClass {

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
