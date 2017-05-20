package com.example.eliferbil.quickquiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.memogame.DifficultySelectorFragment;
import com.example.eliferbil.quickquiz.memogame.MatchingEasyFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFriendFragment extends android.app.ListFragment {

    public ChallengeFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.friends));
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onListItemClick(ListView listView,
                                View itemView,
                                int position,
                                long id) {
        android.app.Fragment nextFragment;
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        nextFragment = new DifficultySelectorFragment();
        ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
