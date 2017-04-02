package com.example.eliferbil.quickquiz.memogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ata on 2.4.2017.
 */

public abstract class MatchingBaseFragment extends Fragment {
    public static final String TAG = "MatchingBaseFragment";
    private boolean isNew = true;
    private static List<Flag.Country> board = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            List<Flag.Country> temp = Arrays.asList(Flag.Country.values());
            List<Flag.Country> countries = new ArrayList<Flag.Country>(temp);
            Collections.shuffle(countries);
            Set<Flag.Country> targets = new HashSet<>(countries.subList(0, getTargetNum()));

            // Add targets twice
            board.addAll(targets);
            board.addAll(targets);

            board.addAll(countries.subList(getTargetNum(), countries.size()));

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
            ids[i] = board.get(i).getMipmapId();
        }
        gridview.setAdapter(
                new TaggedImageAdapter<Flag.Country>(getContext(),
                        ids,
                        board));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), v.getTag() + "--" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        GridView gridview = (GridView) getView().findViewById(R.id.cardGrid);
        ImageView iw = ((ImageView) gridview.getItemAtPosition(0));
        Log.d(TAG, iw.getTag().toString());
        iw.setImageResource(R.mipmap.card);

    }

    protected void shuffleFlags(List<List<Flag>> flags) {
        final int size = flags.size();
        for (int i = 0; i < size; i++) {
            Collections.shuffle(flags.get(i));
        }
        Collections.shuffle(flags);
    }

    public abstract int getEdgeLength();

    public abstract int getTargetNum();

}
