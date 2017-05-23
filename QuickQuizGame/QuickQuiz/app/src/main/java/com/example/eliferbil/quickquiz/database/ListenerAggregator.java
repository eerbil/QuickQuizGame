package com.example.eliferbil.quickquiz.database;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ListenerAggregator<T> implements DbManager.ResultListener<T> {

    private final List<T> results = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final int total;
    private DbManager.ResultListener<List<T>> listener;

    public ListenerAggregator(int total, DbManager.ResultListener<List<T>> listener) {
        this.total = total;
        this.listener = listener;
    }

    private void tryFinish() {
        if (listener == null) {
            return;
        }
        int rSize = results.size();
        int eSize = errors.size();
        if (rSize + eSize >= total) {
            if (rSize > 0) {
                listener.onComplete(new ArrayList<>(results));

            }
            if (eSize > 0) {
                listener.onError(TextUtils.join(", ", errors));
            }
        }
    }

    @Override
    public synchronized void onComplete(T data) {
        results.add(data);
        tryFinish();
    }

    @Override
    public synchronized void onError(String error) {
        errors.add(error);
        tryFinish();

    }
}
