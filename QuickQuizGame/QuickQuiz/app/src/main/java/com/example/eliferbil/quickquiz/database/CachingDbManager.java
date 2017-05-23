package com.example.eliferbil.quickquiz.database;

import android.util.Log;

import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.quickquiz.Question;

import java.util.List;

/**
 * Created by Ata on 21.5.2017.
 */

public class CachingDbManager implements DbManager {

    public static final String TAG = "CachingDBManager";
    //    private class BatchingListener<T> implements ResultListener<T>{
//        private int limit;
//        private ResultListener<T> listener;
//
//        public BatchingListener(int limit, ResultListener<T> listener) {
//            this.limit = limit;
//            this.listener = listener;
//        }
//
//        @Override
//        public synchronized void onComplete(Object data) {
//
//        }
//
//        @Override
//        public synchronized void onError(String error) {
//
//        }
//    }
    private FirebaseDbManager source;
    private SQLiteDbManager cache;

    public CachingDbManager(FirebaseDbManager source, SQLiteDbManager cache) {
        this.source = source;
        this.cache = cache;
    }

    @Override
    public void save(Question question, ResultListener<Question> listener) {
        source.save(question, listener);
        cache.save(question, null);
    }

    @Override
    public void save(List<Question> questions, ResultListener<List<Question>> listener) {
        source.save(questions, listener);
        cache.save(questions, listener);
    }

    @Override
    public void getQuestions(final DbQuery<DbQuery.QuestionParam> query, final ResultListener<List<Question>> listener) {
        source.getQuestions(query, new ResultListener<List<Question>>() {
            @Override
            public void onComplete(List<Question> data) {
                Log.i(TAG, "Retrieved Questions from Firebase (" + query + ")");
                cache.save(data, null);
                if (listener != null) {
                    listener.onComplete(data);
                }
            }

            @Override
            public void onError(String error) {
                if (listener != null) {
                    cache.getQuestions(query, listener);
                    Log.i(TAG, "Retrieved Questions from SQLite (" + query + ")");
                }
            }
        });
    }

    @Override
    public void getFlagBlobs(FlagConfiguration conf, ResultListener<List<byte[]>> listener) {

    }

    @Override
    public void getUsers(DbQuery<DbQuery.UserParam> query, ResultListener<List<User>> listener) {
        source.getUsers(query, listener);
    }

    @Override
    public void save(String uid, User user, ResultListener<User> listener) {
        source.save(uid, user, listener);
    }

    @Override
    public void signIn(User.Credentials cr, ResultListener<User> listener) {
        source.signIn(cr, listener);
    }

    @Override
    public void signUp(User.Credentials cr, User user, ResultListener<User> listener) {
        source.signUp(cr, user, listener);
    }
}
