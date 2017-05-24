package com.example.eliferbil.quickquiz.memogame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.TransitionManager;
import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.database.DbManager;
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
    @DrawableRes
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
    private FlagAdapter flagAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransitionManager.Provider) {
            mtm = ((TransitionManager.Provider) context).provide(this);
        } else {
            throw new IllegalArgumentException("Must provide TransitionManager");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) {

            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Loading Flags...");
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.show();

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    Game.getInstance().getUser().addObserver(MatchingBaseFragment.this);
                    List<Flag.Country> temp = Arrays.asList(Flag.Country.values());
                    List<Flag.Country> countries = new ArrayList<Flag.Country>(temp);
                    Collections.shuffle(countries);
                    targets = new HashSet<>(countries.subList(0, getTargetNum()));
                    DbManager.Provider.getDefault().getFlagBlobs(
                            new DbManager.FlagConfiguration(targets), new DbManager.ResultListener<List<byte[]>>() {
                                @Override
                                public void onComplete(List<byte[]> data) {

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            });

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
            });
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Game game = Game.getInstance();
        User user = game.getUser();
        this.update(user, user.getScore());
        if (!(user instanceof MortalUser)) {
            user = new MortalUser(user);
            game.setUser(user);
        }
        setUIHealth(((MortalUser) user).getHealth(), MortalUser.INIT_HEALTH);

        View view = getView();
        GridView gridview = (GridView) view.findViewById(R.id.cardGrid);

//        int[] ids = new int[board.size()];
//        for (int i = 0; i < ids.length; i++) {
//            ids[i] = board.get(i).getCountry().getMipmapId();
//        }
        flagAdapter = new FlagAdapter(getContext(), CARD_RES_ID, board);
        gridview.setAdapter(flagAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View v,
                                    final int position, long id) {
                switch (chooseState) {
                    case OBSERVE:
                        return;
                    case CHOOSE_1:
                        chooseState = ChooseState.OBSERVE;
                        chosenFlag = getFlagFromView(v);
                        openFlag((ImageView) v);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (chooseState == ChooseState.CHOOSE_2) {
                                    closeFlag((ImageView) getGridView().getItemAtPosition(position));
                                    decrementHealth();
                                    chooseState = ChooseState.CHOOSE_1;
                                    chosenFlag = null;
                                }
                            }
                        }, WAIT_MILLIS);
                        chooseState = ChooseState.CHOOSE_2;
                        break;
                    case CHOOSE_2:
                        chooseState = ChooseState.OBSERVE;
                        if (chosenFlag == getFlagFromView(v)) {
                            chooseState = ChooseState.CHOOSE_2;
                            return;
                        }
                        handler.removeCallbacksAndMessages(null);
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

    private void setUIHealth(int health, int maxHealth) {
        final Resources resources = getResources();
        final String packageName = getActivity().getPackageName();
        for (int i = 1; i <= maxHealth; i++) {

            ImageView iv = (ImageView) getView().findViewById(resources.getIdentifier("life" + i, "id", packageName));
            iv.setImageResource(i > health ? R.mipmap.heart2 : R.mipmap.heart);
        }
    }

    private int getHealth() {
        return ((MortalUser) Game.getInstance().getUser()).getHealth();
    }

    private void closeFlag(ImageView iw) {
        flagAdapter.closeFlag(((FlagAdapter.IndexedFlagTag) iw.getTag()).index);
    }

    private void openFlag(ImageView iw) {
        flagAdapter.openFlag(((FlagAdapter.IndexedFlagTag) iw.getTag()).index);
    }

    private void removeFlag(ImageView iw) {
        flagAdapter.eliminateFlag(((FlagAdapter.IndexedFlagTag) iw.getTag()).index);
    }

    private Flag getFlagFromView(View v) {
        return ((FlagAdapter.IndexedFlagTag) v.getTag()).flag;
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
                iv.invalidate();

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = board.size();
                    GridView gridView = getGridView();
                    for (int i = 0; i < size; i++) {
                        ImageView itemAtPosition = (ImageView) gridView.getItemAtPosition(i);
                        if (itemAtPosition != null) {
                            closeFlag(itemAtPosition);
                        }
                    }
//                    ((TaggedImageAdapter) gridView.getAdapter()).notifyDataSetChanged();
//                    gridView.invalidateViews();
                    chooseState = ChooseState.CHOOSE_1;
                }
            }, WAIT_MILLIS);
            isNew = false;
        }
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
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
