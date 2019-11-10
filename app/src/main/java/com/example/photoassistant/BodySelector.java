package com.example.photoassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Body Selector is a fragment whose purpose is to let
 * the users chose from presets set by them of
 * body with 5 saved lenses combinations.
 */

public class BodySelector extends Fragment {


    /**
     * variables used to dictate how many saved lenses
     * and bodies are used in the body selector as well
     * as which lens spot is used.
     */
    public static final int howManySlots = 4;
    public static final int howManySsavedLenses = 5;
    public static ListItem[][] Slots = new ListItem[howManySlots][1 + howManySsavedLenses];
    static int whichLens = 1; // whic
    private static int WhichSlot = 1;
    ListItem[] cacheArray = null;

    /**
     * add a slot to the slot array
     *
     * @param whichSlot which slot you wish to add the lens
     * @param li is the array of body/ combination
     *
     */
    public static void addSlot(int whichSlot, ListItem[] li) {

        switch (whichSlot) {
            case 1:
                Slots[0] = li;
                break;
            case 2:
                Slots[1] = li;
                break;
            case 3:
                Slots[2] = li;
                break;
            case 4:
                Slots[3] = li;
                break;
        }

    }

    /**
     * this tracks whether the array is empty.
     * @return true if it is and false if it is not empty.
     */

    public static boolean empty() {
        int startPoint = getWhichSlot();
        int tracker = getWhichSlot();
        do {
            tracker = tracker + 1;
            tracker = tracker % 4 + 1;
            if (getDataOfSlot(tracker)[0] != null) {
                return false;
            }
            if (tracker == startPoint) {
                return true;
            }
        } while (true);
    }

    /**
     * if there is more than one lens in the slot, you can cycle
     * through your saved slots. this handles that.
     * @return which slot is the next one
     */
    public static int nextSlot() {
        do {
            setWhichSlot(getWhichSlot() + 1);
        } while (getDataOfSlot(getWhichSlot())[0] == null);

        resetLens();
        return getWhichSlot();
    }

    /**
     * reset which lens back to 1
     */
    public static void resetLens() {
        whichLens = 1;
    }

    /**
     * what slot of the body/camera combination you are currently on
     *
     * @return
     */
    public static int getWhichSlot() {
        return WhichSlot;
    }

    /**
     * set the slot specified in params to
     * @param i
     */
    public static void setWhichSlot(int i) {
        WhichSlot = i;
        if (WhichSlot > 4) WhichSlot = 1;
        if (WhichSlot < 1) WhichSlot = 4;
    }

    /**
     * returns the body currently in the body slot, as long
     * as it isnt null.
     * @param whichSlot is which slot you are currently in
     * @return the slot, null is default if there is no
     *          body in this slot.
     */
    public static ListItemBody getBodySlot(int whichSlot) {
        ListItemBody retSlot = null;
        if (isValid(getDataOfSlot(whichSlot)[0])) {
            retSlot = (ListItemBody) getDataOfSlot(whichSlot)[0];
            retSlot.toString();
        }
        return retSlot;
    }

    /**
     * if the list item input will not throw an error.
     *
     * @param input this is the child class of list item you
     *              wish to see is valid.
     * @return if it is valid in boolean form.
     */
    public static boolean isValid(ListItem input) {
        try {
            input.getPartName();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * lets the user cycle through their saved lenses in the slots.
     *
     * @return the next lens in their saved location.
     */

    public static ListItemLens nextLens() {
        do {
            whichLens = (whichLens + 1) % 5 + 1;
        } while (getDataOfSlot(WhichSlot)[whichLens] == null);
        return getLensSlot(WhichSlot);
    }

    /**
     * returns the lens currently in the lens slot, as long
     * as it isnt null.
     * @param whichSlot is which slot you are currently in
     * @return the slot, null is default if there is no
     *          body in this slot
     */
    public static ListItemLens getLensSlot(int whichSlot) {

        ListItemLens retSlot = null;
        if (isValid(getDataOfSlot(whichSlot)[whichLens])) {
            retSlot = (ListItemLens) getDataOfSlot(whichSlot)[whichLens];
            retSlot.toString();
        }
        return retSlot;
    }

    /**
     * by passing the index of which slot you wish to find
     * out, you can get the data of said slot.
     *
     * @param whichSlot is which slot you wish to get
     * @return the whole slot.
     */

    public static ListItem[] getDataOfSlot(int whichSlot) {

        switch (whichSlot % 5) {
            case 1:
                return Slots[0];
            case 2:
                return Slots[1];
            case 3:
                return Slots[2];
            case 4:
                return Slots[3];
        }
        return null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View rootView = inflater.inflate(R.layout.body_selection_overlay, container, false);
        changeFragBody(rootView);

        return rootView;
    }

    /**
     * this fragment sets all the ui for the user to change to body fragment.
     * it contains all the button sets and the bindings of ui elements to java
     * code.
     * @param rv is the root view in which the fragment resides in
     */
    public void changeFragBody(View rv) {
        final View rootView = rv;
        ImageButton ib_topStart = rootView.findViewById(R.id.imageButtonTopStart);
        ImageButton ib_topEnd = rootView.findViewById(R.id.imageButtonTopEnd);
        ImageButton ib_bottomStart = rootView.findViewById(R.id.imageButtonBottomStart);
        ImageButton ib_bottomEnd = rootView.findViewById(R.id.imageButtonBottomEnd);
        setOverlayResources(rootView);

        setIbOnClick(ib_topStart, 0, 1);

        setIbOnClick(ib_topEnd, 1, 2);

        setIbOnClick(ib_bottomStart, 2, 3);

        setIbOnClick(ib_bottomEnd, 3, 4);

        setIbOnLongClick(ib_topStart, rootView, 0);

        setIbOnLongClick(ib_topEnd, rootView, 1);

        setIbOnLongClick(ib_bottomStart, rootView, 2);

        setIbOnLongClick(ib_bottomEnd, rootView, 3);

    }

    /**
     * this code is used to enforce SE principles DRY(dont repeat yourself).
     * sets the image buttons on click listener dynamically.
     * @param ib is the image button you wish to set
     * @param arraySpot is where the image button is in the array (-1 than spot))
     * @param spot is where the user sees as the spot
     */

    private void setIbOnClick(ImageButton ib, final int arraySpot, final int spot) {

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = spot;
                if (Slots[arraySpot][0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slots[arraySpot], spot));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[arraySpot], spot)).commit();
                }
            }
        });

    }

    /**
     * this code is used to enforce SE principles DRY(dont repeat yourself).
     * sets the image buttons on long click listener dynamically. on a long
     * click, this code will delete the slot and temporarily cache the slot.
     * it also provides a snackbar to undo the delete back to the cached array.
     *
     * @param ib is the image button you wish to set
     * @param arraySlot is where the slot clicked resides
     */

    private void setIbOnLongClick(final ImageButton ib, final View rootView, final int arraySlot) {

        ib.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[arraySlot] != null) {
                    cacheArray = Slots[arraySlot].clone();
                    for (int i = 0; i < Slots[arraySlot].length; i++) {
                        Slots[arraySlot][i] = null;
                    }

                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[arraySlot] = cacheArray.clone();
                            setOverlayResources(rootView);
                            for (int i = 0; i < Slots[arraySlot].length; i++) {
                                cacheArray[i] = null;
                            }
                        }

                    }).show();
                    ib.setImageResource(android.R.color.transparent);
                }
                return true;
            }
        });

    }

    /**
     * this binds which data we have in our slots to the data the user sees.
     *
     * @param rootView is the view in which the resources from this
     *                 overlay reside.
     */

    private void setOverlayResources(View rootView) {
        TextView tvTS = rootView.findViewById(R.id.tv_ImageButtonTS);
        TextView tvTE = rootView.findViewById(R.id.tv_ImageButtonTE);
        TextView tvBS = rootView.findViewById(R.id.tv_ImageButtonBS);
        TextView tvBE = rootView.findViewById(R.id.tv_ImageButtonBE);
        ImageButton ib_topStart = rootView.findViewById(R.id.imageButtonTopStart);
        ImageButton ib_topEnd = rootView.findViewById(R.id.imageButtonTopEnd);
        ImageButton ib_bottomStart = rootView.findViewById(R.id.imageButtonBottomStart);
        ImageButton ib_bottomEnd = rootView.findViewById(R.id.imageButtonBottomEnd);

        ImageView.ScaleType sctype = ImageView.ScaleType.FIT_CENTER;
        if (ifNullHandler(tvTS, 0)) {
            ib_topStart.setImageResource(findTypeBody((ListItemBody) Slots[0][0]));
            ib_topStart.setScaleType(sctype);
        }
        if (ifNullHandler(tvTE, 1)) {
            ib_topEnd.setImageResource(findTypeBody((ListItemBody) Slots[1][0]));
            ib_topEnd.setScaleType(sctype);
        }
        if (ifNullHandler(tvBS, 2)) {
            ib_bottomStart.setImageResource(findTypeBody((ListItemBody) Slots[2][0]));
            ib_bottomStart.setScaleType(sctype);
        }
        if (ifNullHandler(tvBE, 3)) {
            ib_bottomEnd.setImageResource(findTypeBody((ListItemBody) Slots[3][0]));
            ib_bottomEnd.setScaleType(sctype);
        }

    }

    /**
     * This finds the body type of the camera using its "type" variable, and will
     * get the image which reflects which type of camera is used.
     *
     * @param lib is the body in which you want to find body type
     * @return the image resource to set the icon to the correct
     *          camera body
     */

    private int findTypeBody(ListItemBody lib) {

        switch (lib.getType()) {
            case "SLR":
                return R.drawable.slr;
            case "Digital SLR":
                return R.drawable.slr;
            case "Rangefinder":
                return R.drawable.rangefinder;
            case "Medium Format":
                return R.drawable.medium;
            case "Mirrorless":
                return R.drawable.mirrorless;
            case "Cine":
                return R.drawable.cine;
        }
        return 0;
    }

    /**
     * this function will set the textview if the array slot isnt null.
     * @param tv is the textview to set
     * @param arraynum is where in the array the slot is.
     * @return true if it wasnt null, and false if it is.
     */
    private boolean ifNullHandler(TextView tv, int arraynum) {
        boolean retVal = false;
        if (Slots[arraynum][0] != null) {
            tv.setText(Slots[arraynum][0].getPartName());
            retVal = true;
        } else {
            tv.setText("Not Selected");
        }
        return retVal;
    }


}
