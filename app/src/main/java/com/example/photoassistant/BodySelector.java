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


    public static ListItem[] Slot1 = new ListItem[6];
    public static ListItem[] Slot2 = new ListItem[6];
    public static ListItem[] Slot3 = new ListItem[6];
    public static ListItem[] Slot4 = new ListItem[6];
    private static int WhichSlot = 1;
    ListItem[] cacheArray = null;

    public static void addSlot(int whichSlot, ListItem[] li) {

        switch (whichSlot) {
            case 1:
                Slot1 = li;
                break;
            case 2:
                Slot2 = li;
                break;
            case 3:
                Slot3 = li;
                break;
            case 4:
                Slot4 = li;
                break;
        }

    }
    public static boolean empty(){
        int startPoint = getWhichSlot();
        int tracker = getWhichSlot();
        do
        {
            tracker = tracker + 1;
            tracker = tracker%4+1;
            if(resolveSlot(tracker)[0] != null){ return false;}
            if(tracker==startPoint) {return true;}
        }while (true);
    }

    public static int nextSlot() {
        do
        {
            setWhichSlot(getWhichSlot() + 1);
        }while (resolveSlot(getWhichSlot())[0] == null);

        resetLens();
        return getWhichSlot();
    }
    public static void resetLens()
    {
        whichLens=1;
    }

    public static int getWhichSlot() {
        return WhichSlot;
    }

    public static void setWhichSlot(int i) {
        WhichSlot = i;
        if(WhichSlot>4) WhichSlot = 1;
        if(WhichSlot<1) WhichSlot = 4;
    }

    public static ListItemBody getBodySlot(int whichSlot) {
        ListItemBody retSlot = null;
        if (Calculator.isParsable(resolveSlot(whichSlot)[0])) {
            retSlot = (ListItemBody) resolveSlot(whichSlot)[0];
            retSlot.toString();
        }
        return retSlot;
    }
    static int whichLens = 1;
    public static ListItemLens nextLens()
    {
        do{
            whichLens = (whichLens+1)%5+1;
        }while(resolveSlot(WhichSlot)[whichLens]==null);
        return getLensSlot(WhichSlot);
    }

    public static ListItemLens getLensSlot(int whichSlot) {

        ListItemLens retSlot = null;
        if (Calculator.isParsable(resolveSlot(whichSlot)[whichLens])) {
            retSlot = (ListItemLens) resolveSlot(whichSlot)[whichLens];
            retSlot.toString();
        }
        return retSlot;
    }

    public static ListItem[] resolveSlot(int whichSlot) {

        switch (whichSlot % 5) {
            case 1:
                return Slot1;
            case 2:
                return Slot2;
            case 3:
                return Slot3;
            case 4:
                return Slot4;
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

        ib_topStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 1;
                if (Slot1[0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slot1, 1));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slot1, 1)).commit();
                }
            }
        });
        ib_topStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slot1 != null) {
                    cacheArray = Slot1.clone();
                    Slot1[0] = null;
                    Slot1[1] = null;
                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slot1 = cacheArray.clone();
                            setOverlayResources(rootView);
                            cacheArray[0] = null;
                            cacheArray[1] = null;

                        }

                    }).show();
                }
                return true;
            }
        });
        ib_topEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 2;
                if (Slot2[0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slot2, 2));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slot2, 2)).commit();
                }
            }
        });
        ib_bottomStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 3;
                if (Slot3[0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slot3, 3));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slot3, 3)).commit();
                }
            }
        });
        ib_bottomEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 4;
                if (Slot4[0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slot4, 4));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slot4, 4)).commit();
                }
            }
        });
    }


    private void setOverlayResources(View rootView) {
        TextView tvTS = rootView.findViewById(R.id.tv_ImageButtonTS);
        TextView tvTE = rootView.findViewById(R.id.tv_ImageButtonTE);
        TextView tvBS = rootView.findViewById(R.id.tv_ImageButtonBS);
        TextView tvBE = rootView.findViewById(R.id.tv_ImageButtonBE);


        if (Slot1[0] != null) {
            tvTS.setText(Slot1[0].getPartName());
        } else {
            tvTS.setText("Not Selected");
        }

        if (Slot2[0] != null) {
            tvTE.setText(Slot2[0].getPartName());
        } else {
            tvTE.setText("Not Selected");
        }

        if (Slot3[0] != null) {
            tvBS.setText(Slot3[0].getPartName());
        } else {
            tvBS.setText("Not Selected");
        }

        if (Slot4[0] != null) {
            tvBE.setText(Slot4[0].getPartName());
        } else {
            tvBE.setText("Not Selected");
        }

    }




}
