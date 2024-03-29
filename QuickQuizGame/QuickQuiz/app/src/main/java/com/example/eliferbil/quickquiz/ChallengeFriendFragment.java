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

import com.example.eliferbil.quickquiz.memogame.DifficultySelectorFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFriendFragment extends ListFragment {

    TabletActivity ta;
    public ChallengeFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ta = (TabletActivity) context;
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

        Fragment nextFragment = new DifficultySelectorFragment();
        ta.pushDetailFragment(nextFragment);
    }
}
