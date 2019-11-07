package com.example.photoassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

public class BodySelector extends Fragment {


    public static final int howManySlots = 4;
    public static final int howManySsavedLenses = 5;

    public static ListItem[][] Slots = new ListItem[howManySlots][1 + howManySsavedLenses];
    static int whichLens = 1;
    private static int WhichSlot = 1;
    ListItem[] cacheArray = null;

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

    public static boolean empty() {
        int startPoint = getWhichSlot();
        int tracker = getWhichSlot();
        do {
            tracker = tracker + 1;
            tracker = tracker % 4 + 1;
            if (resolveSlot(tracker)[0] != null) {
                return false;
            }
            if (tracker == startPoint) {
                return true;
            }
        } while (true);
    }

    public static int nextSlot() {
        do {
            setWhichSlot(getWhichSlot() + 1);
        } while (resolveSlot(getWhichSlot())[0] == null);

        resetLens();
        return getWhichSlot();
    }

    public static void resetLens() {
        whichLens = 1;
    }

    public static int getWhichSlot() {
        return WhichSlot;
    }

    public static void setWhichSlot(int i) {
        WhichSlot = i;
        if (WhichSlot > 4) WhichSlot = 1;
        if (WhichSlot < 1) WhichSlot = 4;
    }

    public static ListItemBody getBodySlot(int whichSlot) {
        ListItemBody retSlot = null;
        if (isParsable(resolveSlot(whichSlot)[0])) {
            retSlot = (ListItemBody) resolveSlot(whichSlot)[0];
            retSlot.toString();
        }
        return retSlot;
    }

    public static boolean isParsable(ListItem input) {
        try {
            input.getPartName();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ListItemLens nextLens() {
        do {
            whichLens = (whichLens + 1) % 5 + 1;
        } while (resolveSlot(WhichSlot)[whichLens] == null);
        return getLensSlot(WhichSlot);
    }

    public static ListItemLens getLensSlot(int whichSlot) {

        ListItemLens retSlot = null;
        if (isParsable(resolveSlot(whichSlot)[whichLens])) {
            retSlot = (ListItemLens) resolveSlot(whichSlot)[whichLens];
            retSlot.toString();
        }
        return retSlot;
    }

    public static ListItem[] resolveSlot(int whichSlot) {

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

    public void changeFragBody(View rv) {
        final View rootView = rv;
        ImageButton ib_topStart = rootView.findViewById(R.id.imageButtonTopStart);
        ImageButton ib_topEnd = rootView.findViewById(R.id.imageButtonTopEnd);
        ImageButton ib_bottomStart = rootView.findViewById(R.id.imageButtonBottomStart);
        ImageButton ib_bottomEnd = rootView.findViewById(R.id.imageButtonBottomEnd);
        setOverlayResources(rootView);

//        ib_topStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WhichSlot = 1;
//                if (Slots[0][0] == null) {
//                    MainActivity.fragmentStack.add(new Body(Slots[0], 1));
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[0], 1)).commit();
//                }
//            }
//        });

        setIbOnClick(ib_topStart,0,1);

        setIbOnClick(ib_topEnd,1,2);

        setIbOnClick(ib_bottomStart,2,3);

        setIbOnClick(ib_bottomEnd,3,4);

        setIbOnLongClick(ib_topStart,rootView,0);

        setIbOnLongClick(ib_topEnd,rootView,1);

        setIbOnLongClick(ib_bottomStart,rootView,2);

        setIbOnLongClick(ib_bottomEnd,rootView,3);



    }

    private void setIbOnClick(ImageButton ib, final int arraySpot, final int spot){

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

    private void setIbOnLongClick(ImageButton ib, final View rootView, final int arraySlot){

        ib.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[arraySlot] != null) {
                    cacheArray = Slots[arraySlot].clone();
                    for(int i=0;i<Slots[arraySlot].length;i++) {Slots[arraySlot][i] = null;}

                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[arraySlot] = cacheArray.clone();
                            setOverlayResources(rootView);
                            for(int i=0;i<Slots[arraySlot].length;i++) {cacheArray[i] = null;}
                        }

                    }).show();
                }
                return true;
            }
        });

    }

    private void setOverlayResources(View rootView) {
        TextView tvTS = rootView.findViewById(R.id.tv_ImageButtonTS);
        TextView tvTE = rootView.findViewById(R.id.tv_ImageButtonTE);
        TextView tvBS = rootView.findViewById(R.id.tv_ImageButtonBS);
        TextView tvBE = rootView.findViewById(R.id.tv_ImageButtonBE);


        if (Slots[0][0] != null) {
            tvTS.setText(Slots[0][0].getPartName());
        } else {
            tvTS.setText("Not Selected");
        }

        if (Slots[1][0] != null) {
            tvTE.setText(Slots[1][0].getPartName());
        } else {
            tvTE.setText("Not Selected");
        }

        if (Slots[2][0] != null) {
            tvBS.setText(Slots[2][0].getPartName());
        } else {
            tvBS.setText("Not Selected");
        }

        if (Slots[3][0] != null) {
            tvBE.setText(Slots[3][0].getPartName());
        } else {
            tvBE.setText("Not Selected");
        }

    }


}
