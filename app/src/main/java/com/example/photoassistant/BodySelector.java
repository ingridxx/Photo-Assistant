package com.example.photoassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

public class BodySelector extends Fragment {


    public static final int howManySlots = 4;
    public static final int howManySavedLenses = 5;

    public static ListItem[][] Slots = new ListItem[howManySlots][1 + howManySavedLenses];
    static int whichLens = 1;
    private static int currentSlot = 1;
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
        return currentSlot;
    }

    public static void setWhichSlot(int i) {
        currentSlot = i;
        if (currentSlot > 4) currentSlot = 1;
        if (currentSlot < 1) currentSlot = 4;
    }

    public static ListItemBody getBodySlot(int whichSlot) {
        //returns a specific list item from the current
        ListItemBody retSlot = null;
        if (isValid(resolveSlot(currentSlot)[whichSlot])) {
            retSlot = (ListItemBody) resolveSlot(whichSlot)[whichSlot];
            retSlot.toString();
        }
        return retSlot;
    }

    public static boolean isValid(ListItem input) {
        try {
            input.getPartName();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static ListItemLens nextLens() {
//        do {
//            whichLens = (whichLens + 1) % 5 + 1;
//        } while (resolveSlot(currentSlot)[whichLens] == null);
//        return getLensSlot(currentSlot);
//    }

    public static ListItemLens getLensSlot(int whichSlot, int whichLens) {

        ListItemLens retSlot = null;
        if (isValid(resolveSlot(whichSlot)[whichLens])) {
            retSlot = (ListItemLens) resolveSlot(whichSlot)[whichLens];
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
                currentSlot = 1;
                if (Slots[0][0] == null) {
                    MainActivity.fragmentStack.add(1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(1)).commit();
                } else {
                    MainActivity.fragmentStack.add(1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Lens(Slots[0], (ListItemBody) Slots[0][0], 1)).commit();
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
                currentSlot = 2;
                if (Slots[1][0] == null) {
                    MainActivity.fragmentStack.add(2);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(2)).commit();
                } else {
                    MainActivity.fragmentStack.add(2);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Lens(Slots[1], (ListItemBody) Slots[1][0], 2)).commit();
                }
            }
        });
        ib_topEnd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[1] != null) {
                    cacheArray = Slots[0].clone();
                    Slots[1][0] = null;
                    for (int i =1; i <  1+howManySavedLenses; i++){
                        Slots[1][i] = null;
                    }

                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[1] = cacheArray.clone();
                            setOverlayResources(rootView);
                            for (int i =0; i <  cacheArray.length; i++){
                                cacheArray[i] = null;
                            }


                        }

                    }).show();
                }
                return true;
            }
        });
        ib_bottomStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSlot = 3;
                if (Slots[2][0] == null) {
                    MainActivity.fragmentStack.add(3);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(3)).commit();
                } else {
                    MainActivity.fragmentStack.add(3);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Lens(Slots[2], (ListItemBody) Slots[2][0], 3)).commit();
                }
            }
        });

        ib_topStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[2] != null) {
                    cacheArray = Slots[0].clone();
                    Slots[2][0] = null;
                    for (int i =1; i <  1+howManySavedLenses; i++){
                        Slots[2][i] = null;
                    }

                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[2] = cacheArray.clone();
                            setOverlayResources(rootView);
                            for (int i =0; i <  cacheArray.length; i++){
                                cacheArray[i] = null;
                            }


                        }

                    }).show();
                }
                return true;
            }
        });
        ib_bottomEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSlot = 4;
                if (Slots[3][0] == null) {
                    MainActivity.fragmentStack.add(3);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Body(4)).commit();
                } else {
                    MainActivity.fragmentStack.add(3);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl, new Lens(Slots[3], (ListItemBody) Slots[3][0], 4)).commit();
                }
            }
        });

        ib_topEnd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Slots[3] != null) {
                    cacheArray = Slots[0].clone();
                    for (int i =0; i <  1+howManySavedLenses; i++){
                        Slots[1][i] = null;
                    }

                    setOverlayResources(rootView);

                    Snackbar.make(v, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Slots[1] = cacheArray.clone();
                            setOverlayResources(rootView);
                            for (int i =0; i <  cacheArray.length; i++){
                                cacheArray[i] = null;
                            }


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
