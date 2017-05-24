package com.example.eliferbil.quickquiz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.eliferbil.quickquiz.database.CachingDbManager;
import com.example.eliferbil.quickquiz.database.DbManager;
import com.example.eliferbil.quickquiz.database.FirebaseDbManager;
import com.example.eliferbil.quickquiz.database.SQLiteDbManager;
import com.example.eliferbil.quickquiz.quickquiz.Question;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public static final String TAG = "TestTag";

    @Test
    public void useAppContext() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.eliferbil.quickquiz", appContext.getPackageName());
    }

    @Test
    public void cached() {
        final List<List<Question>> list = new Vector<>(2);

        CachingDbManager dbm = new CachingDbManager(
                new FirebaseDbManager(),
                new SQLiteDbManager(InstrumentationRegistry.getTargetContext()));
        DbManager.DbQuery<DbManager.DbQuery.QuestionParam> query = new DbManager.DbQuery<>();
        query.setParam(DbManager.DbQuery.QuestionParam.CATEGORY, "music");
        for (int i = 0; i < 2; i++) {
            Log.d(TAG, "Trying... " + i);
            dbm.getQuestions(query, new DbManager.ResultListener<List<Question>>() {
                @Override
                public void onComplete(List<Question> data) {
                    Log.d(TAG, data.toString());
                    list.add(data);
                }

                @Override
                public void onError(String error) {
                    throw new RuntimeException(error);
                }
            });
            if (i < 0) {
                wait(this, 10000);
            }
        }

        wait(this, 10000);
        wait(this, 10000);
        assertEquals(list.get(0), list.get(1));
    }


//    public void sqlite(){
//        final SQLiteDbManager dbm = new SQLiteDbManager(InstrumentationRegistry.getTargetContext());
//        dbm.save(Game.getInstance().getFoodQuestions(), new DbManager.ResultListener<List<Question>>() {
//            @Override
//            public void onComplete(List<Question> data) {
//                Log.d(TAG, data.toString());
//                DbManager.DbQuery<DbManager.DbQuery.QuestionParam> query = new DbManager.DbQuery<>();
//                query.setParam(DbManager.DbQuery.QuestionParam.CATEGORY, "food");
//                dbm.getQuestions(query, new DbManager.ResultListener<List<Question>>() {
//                    @Override
//                    public void onComplete(List<Question> data) {
//                        Log.d(TAG, data.toString());
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        Log.d(TAG, error);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.d(TAG, error);
//            }
//        });
//
//        wait(new Object(), 10000);
//    }


    public void stuff() {
        final Object lock = new Object();
        System.out.println("Hello");
        Log.d(TAG, "World");
        FirebaseDbManager dbm = new FirebaseDbManager("akesfeden13@ku.edu.tr", "putthissomewheresafe");
        DbManager.DbQuery<DbManager.DbQuery.QuestionParam> query = new DbManager.DbQuery<>();
        query.setParam(DbManager.DbQuery.QuestionParam.CATEGORY, "music");
        dbm.getQuestions(query, new DbManager.ResultListener<List<Question>>() {
            @Override
            public void onComplete(List<Question> data) {
                Log.d(TAG, data.toString());
                //poke(lock);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
                //poke(lock);
            }
        });

//        for (Question question : Game.getInstance().getFoodQuestions()) {
//            dbm.save(question, new DbManager.ResultListener<Question>() {
//                @Override
//                public void onComplete(Question data) {
//                    poke(lock);
//                }
//
//                @Override
//                public void onError(String error) {
//                    poke(lock);
//                }
//            });
//        }

        wait(lock, 5000);
    }


    protected static void wait(Object lock, int timeout) {
        synchronized (lock) {
            {
                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected static void poke(Object lock) {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
