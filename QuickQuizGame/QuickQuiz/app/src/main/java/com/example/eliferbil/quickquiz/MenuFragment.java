package com.example.eliferbil.quickquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    public static String[] gamesList = new String[5];

    public interface GameListListener {
        void itemClicked(long id);
    }

    private GameListListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gamesList[0]="Quick Quiz";
        gamesList[1]="Flag Match";
        gamesList[2]="Profile";
        gamesList[3]="Friend List";
        gamesList[4]="High Scores";

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
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.listener = (GameListListener) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }
}
