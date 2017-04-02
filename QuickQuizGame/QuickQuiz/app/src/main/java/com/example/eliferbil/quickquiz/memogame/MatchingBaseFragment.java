package com.example.eliferbil.quickquiz.memogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
    private boolean isNew = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Flag.Country> temp = Arrays.asList(Flag.Country.values());
        List<Flag.Country> countries = new ArrayList<Flag.Country>(temp);
        Collections.shuffle(countries);
        Set<Flag.Country> targets = new HashSet<>(countries.subList(0, getTargetNum()));


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        GridView gridview = (GridView) view.findViewById(R.id.cardGrid);

        int[][] ids = new int[4][4];
        for (int i = 0; i < ids.length; i++) {
            for (int j = 0; j < ids[i].length; j++) {
                ids[i][j] = R.mipmap.czechrepublic;
            }
        }
        gridview.setAdapter(new ImageAdapter(getContext(), ids));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

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
