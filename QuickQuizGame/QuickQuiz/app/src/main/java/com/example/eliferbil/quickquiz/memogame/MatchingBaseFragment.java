package com.example.eliferbil.quickquiz.memogame;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.TransitionManager;
import com.example.eliferbil.quickquiz.quickquiz.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


public abstract class MatchingBaseFragment extends Fragment implements Observer {
    public static final String TAG = "MatchingBaseFragment";
    public static final int WAIT_MILLIS = 5000;
    public static final int CARD_RES_ID = R.mipmap.card;
    public static final int COMPARE_MILLIS = 500;
    private final Handler handler = new Handler();
    private boolean isNew = true;
    private ChooseState chooseState = ChooseState.OBSERVE;
    private static List<Flag> board = new ArrayList<>();
    private static Set<Flag.Country> targets = new HashSet<>();
    private Flag chosenFlag;
    private MemoTransitionManager mtm;
    private int matches = 0;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MemoTransitionManager) {
            mtm = (MemoTransitionManager) context;
        } else if (context instanceof TransitionManager.Provider) {
            mtm = ((TransitionManager.Provider) context).provide();
        } else {
            throw new IllegalArgumentException("Must provide TransitionManager");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {
            Game.getInstance().getUser().addObserver(this);
            List<Flag.Country> temp = Arrays.asList(Flag.Country.values());
            List<Flag.Country> countries = new ArrayList<Flag.Country>(temp);
            Collections.shuffle(countries);
            targets = new HashSet<>(countries.subList(0, getTargetNum()));
            List<Flag.Country> flagCountries = new ArrayList<>(countries.size());

            // Add targets twice
            flagCountries.addAll(targets);
            flagCountries.addAll(targets);

            int end = Math.min(countries.size(), getEdgeLength() * getEdgeLength() - getTargetNum());
            flagCountries.addAll(countries.subList(getTargetNum(), end));

            board = new ArrayList<Flag>(flagCountries.size());
            for (Flag.Country c : flagCountries) {
                board.add(new Flag(c));
            }
            Collections.shuffle(board);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        GridView gridview = (GridView) view.findViewById(R.id.cardGrid);

        int[] ids = new int[board.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = board.get(i).getCountry().getMipmapId();
        }
        gridview.setAdapter(
                new TaggedImageAdapter<>(getContext(),
                        ids,
                        board));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View v,
                                    final int position, long id) {
                switch (chooseState) {
                    case OBSERVE:
                        return;
                    case CHOOSE_1:
                        chosenFlag = getFlagFromView(v);
                        openFlag((ImageView) v);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (chooseState == ChooseState.CHOOSE_2) {
                                    closeFlag((ImageView) getGridView().getItemAtPosition(position));
                                    chooseState = ChooseState.CHOOSE_1;
                                    chosenFlag = null;
                                }
                            }
                        }, WAIT_MILLIS);
                        chooseState = ChooseState.CHOOSE_2;
                        break;
                    case CHOOSE_2:
                        if (chosenFlag == getFlagFromView(v)) {
                            return;
                        }
                        chooseState = ChooseState.OBSERVE;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (getFlagFromView(v).sameCountry(chosenFlag)) {
                                    removeFlag((ImageView) v);
                                    removeFlag(getViewFromFlag(chosenFlag));
                                    addScore(100);
                                    if (++matches >= getTargetNum()) {
                                        mtm.nextLevel(getNextLevelCode());
                                    }
                                } else {
                                    closeFlag((ImageView) v);
                                    closeFlag(getViewFromFlag(chosenFlag));
                                    decrementHealth();
                                    if (getHealth() <= 0) {
                                        nextLevel();
                                        return;
                                    }
                                }
                                chooseState = ChooseState.CHOOSE_1;
                            }
                        }, COMPARE_MILLIS);
                        openFlag((ImageView) v);
                        break;
                }
            }
        });


    }

    protected void nextLevel() {
        handler.removeCallbacksAndMessages(null);
        mtm.nextLevel(MemoTransitionManager.FINISH);
    }

    private void addScore(int i) {
        Game.getInstance().getUser().addScore(i);
    }

    private void decrementHealth() {
        final Resources resources = getResources();
        final String packageName = getActivity().getPackageName();

        ImageView iv = (ImageView) getView().findViewById(resources.getIdentifier("life" + getHealth(), "id", packageName));
        iv.setImageResource(R.mipmap.heart2);
        ((MortalUser) Game.getInstance().getUser()).decreaseHealth(1);
    }

    private int getHealth() {
        return ((MortalUser) Game.getInstance().getUser()).getHealth();
    }

    private void closeFlag(ImageView iw) {
        Flag f = (Flag) iw.getTag();
        iw.setImageResource(CARD_RES_ID);
    }

    private void openFlag(ImageView iw) {
        Flag f = (Flag) iw.getTag();
        iw.setImageResource(f.getCountry().getMipmapId());
        f.setState(Flag.OpenState.OPEN);
    }

    private void removeFlag(ImageView iw) {
        Flag f = (Flag) iw.getTag();
        iw.setImageResource(0);
        f.setState(Flag.OpenState.ELIMINATED);
    }

    private Flag getFlagFromView(View v) {
        return (Flag) v.getTag();
    }

    private ImageView getViewFromFlag(Flag f) {
        return (ImageView) getGridView().getItemAtPosition(board.indexOf(f));
    }

    protected GridView getGridView() {
        return (GridView) getView().findViewById(R.id.cardGrid);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNew) {
            final Resources resources = getResources();
            final String packageName = getActivity().getPackageName();
            int i = 1;
            for (Flag.Country target : targets) {
                ImageView iv = (ImageView) getView().findViewById(resources.getIdentifier("flag" + i, "id", packageName));
                iv.setImageResource(target.getMipmapId());
                i++;

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = board.size();
                    for (int i = 0; i < size; i++) {
                        ImageView itemAtPosition = (ImageView) getGridView().getItemAtPosition(i);
                        if (itemAtPosition != null) {
                            itemAtPosition.setImageResource(CARD_RES_ID);
                            board.get(i).setState(Flag.OpenState.CLOSED);
                        }
                    }
                    chooseState = ChooseState.CHOOSE_1;
                }
            }, WAIT_MILLIS);
            isNew = false;
        }
    }

    public abstract int getEdgeLength();

    public abstract int getTargetNum();

    public abstract int getNextLevelCode();

    @Override
    public void update(Observable observable, Object score) {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(R.id.score)).setText("Score: " + score);
        }
    }

    private enum ChooseState {
        OBSERVE, CHOOSE_1, CHOOSE_2
    }

}
