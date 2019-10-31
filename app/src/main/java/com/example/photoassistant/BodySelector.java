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


    public static ListItem[][] Slots = new ListItem[4][6];

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

        ib_topStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 1;
                if (Slots[0][0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slots[0], 1));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[0], 1)).commit();
                }
            }
        });
        ib_topStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[0] != null) {
                    cacheArray = Slots[0].clone();
                    Slots[0][0] = null;
                    Slots[0][1] = null;
                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[0] = cacheArray.clone();
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
                if (Slots[1][0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slots[1], 2));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[1], 2)).commit();
                }
            }
        });
        ib_bottomStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 3;
                if (Slots[2][0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slots[2], 3));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[2], 3)).commit();
                }
            }
        });
        ib_bottomEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichSlot = 4;
                if (Slots[3][0] == null) {
                    MainActivity.fragmentStack.add(new Body(Slots[3], 4));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(Slots[3], 4)).commit();
                }
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
