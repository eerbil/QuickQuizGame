package com.example.eliferbil.quickquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    public static String[] gamesList = new String[2];

    static interface GameListListener {
        void itemClicked(long id);
    }

    private GameListListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gamesList[0]="Quick Quiz";
        gamesList[1]="Flag Match";

        View view = super.onCreateView(inflater,container,savedInstanceState);

        //Add the listener to the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                gamesList);
        setListAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //TODO: Hata veriyor
        this.listener = (GameListListener) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }
}
