package com.example.eliferbil.quickquiz;


import android.app.Activity;
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
public class MenuFragment extends Fragment {
    public static String[] gamesList = new String[2];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gamesList[0]="Quick Quiz";
        gamesList[1]="Flag Match";
        // Inflate the layout for this fragment
        View view = getView();
        //TODO: Array Adapter hata veriyor
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                gamesList);
        ListView list =(ListView) view.findViewById(R.id.gameList);
        list.setAdapter(adapter);*/
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

  /*  @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }*/

}
