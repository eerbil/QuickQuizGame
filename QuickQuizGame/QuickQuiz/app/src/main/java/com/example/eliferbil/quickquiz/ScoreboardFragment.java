package com.example.eliferbil.quickquiz;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.database.DbManager;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreboardFragment extends ListFragment {


    public ScoreboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DbManager.DbQuery<DbManager.DbQuery.UserParam> query = new DbManager.DbQuery<>();
        query.setOrderBy(DbManager.DbQuery.UserParam.SCORE);
        DbManager.Provider.getDefault().getUsers(
                query,
                new DbManager.RunOnUIListener<>(getActivity(), new DbManager.ResultListener<List<User>>() {
                    @Override
                    public void onComplete(List<User> data) {
                        Collections.reverse(data);
                        int size = data.size();
                        String[] listContents = new String[size];
                        for (int i = size - 1; i >= 0; --i) {
                            User user = data.get(i);
                            listContents[i] = user.getUsername() + "\t\t" + user.getOnlineScore();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getContext(),
                                android.R.layout.simple_list_item_1,
                                listContents
                        );
                        setListAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
