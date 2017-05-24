package com.example.eliferbil.quickquiz.database;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.memogame.Flag;
import com.example.eliferbil.quickquiz.quickquiz.Question;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface DbManager {
    void save(Question question, ResultListener<Question> listener);

    void save(List<Question> questions, ResultListener<List<Question>> listener);

    void getQuestions(DbQuery<DbQuery.QuestionParam> query, ResultListener<List<Question>> listener);

    void getFlagBlobs(FlagConfiguration conf, ResultListener<List<byte[]>> listener);

    void getUsers(DbQuery<DbQuery.UserParam> query, ResultListener<List<User>> listener);

    void save(String uid, User user, ResultListener<User> listener);

    void saveCurrentUser(User user, ResultListener<User> listener);

    void signIn(User.Credentials cr, ResultListener<User> listener);

    void signUp(User.Credentials cr, User user, ResultListener<User> listener);

    class Provider {
        private static DbManager defaultDbm;

        public static DbManager getDefault() {
            if (defaultDbm == null) {
                throw new NullPointerException("Default DBM is null!");
            }
            return defaultDbm;
        }

        public static void setDefault(DbManager defaultDbm) {
            Provider.defaultDbm = defaultDbm;
        }


    }

    class FlagConfiguration {
        public int limit;
        public Collection<Flag.Country> coll;

        public FlagConfiguration(int limit) {
            this.limit = limit;
        }

        public FlagConfiguration(Collection<Flag.Country> coll) {
            this.coll = coll;
            this.limit = coll.size();
        }
    }

    class DbQuery<P extends DbQuery.KeyNameProvider> {
        public interface KeyNameProvider {
            String getKeyName();
        }

        private String orderBy;
        private Map<String, String> params = new HashMap<>();

        public String getParam(P param) {
            return params.get(param.getKeyName());
        }

        public String setParam(P param, String val) {
            return params.put(param.getKeyName(), val);
        }

        public String deleteParam(P param) {
            return params.remove(param.getKeyName());
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(P param) {
            this.orderBy = param != null ? param.getKeyName() : null;
        }

        public Iterable<Map.Entry<String, String>> params() {
            // Warning: Representation exposure!
            return params.entrySet();
        }

        @Override
        public String toString() {
            return "DbQuery{" +
                    "params=" + params +
                    '}';
        }

        public enum UserParam implements KeyNameProvider {
            UID("uid"), EMAIL("email"), NAME("name"), SURNAME("surname"),
            CITY("city"), SCORE("onlineScore");

            private String keyName;

            UserParam(String keyName) {
                this.keyName = keyName;
            }

            public String getKeyName() {
                return keyName;
            }
        }


        public enum QuestionParam implements KeyNameProvider {
            CATEGORY("category");
            private String keyName;

            QuestionParam(String keyName) {
                this.keyName = keyName;
            }

            public String getKeyName() {
                return keyName;
            }
        }

    }


    interface ResultListener<T> {
        void onComplete(T data);

        void onError(String error);
    }

    public static class RunOnUIListener<T> implements ResultListener<T> {

        private Activity act;
        private ResultListener<T> listener;

        public RunOnUIListener(Activity act, @NonNull ResultListener<T> listener) {
            this.act = act;
            this.listener = listener;
        }

        @Override
        public void onComplete(final T data) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onComplete(data);
                }
            });
        }

        @Override
        public void onError(final String error) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onError(error);
                }
            });
        }
    }

}
