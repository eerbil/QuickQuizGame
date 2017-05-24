package com.example.eliferbil.quickquiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.database.DTOs.DTO;
import com.example.eliferbil.quickquiz.quickquiz.Question;

import java.util.ArrayList;
import java.util.List;

import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.QuestionParam;
import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.UserParam;

/**
 * Created by Ata on 20.5.2017.
 */

public class SQLiteDbManager implements DbManager {

    public static final String QUESTIONS_TABLE_NAME = "Questions";
    public static final String ANSWERS_TABLE_NAME = "Answers";

    private static class MySqlLiteOpenHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "GameOSDB";
        public static final int DB_VERSION = 1;

        public MySqlLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + QUESTIONS_TABLE_NAME + " (_id INTEGER PRIMARY KEY, "
                    + "text TEXT UNIQUE, "
                    + "category TEXT, "
                    + "score INTEGER);");
            db.execSQL("CREATE TABLE " + ANSWERS_TABLE_NAME + " (_id INTEGER PRIMARY KEY, "
                    + "text TEXT, "
                    + "isCorrect INTEGER, "
                    + "qid INTEGER, "
                    + "FOREIGN KEY(qid) REFERENCES Questions(_id) ON DELETE CASCADE);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private SQLiteOpenHelper helper;

    public SQLiteDbManager(Context context) {
        this.helper = new MySqlLiteOpenHelper(context);
    }

    @Override
    public void getQuestions(DbQuery<QuestionParam> query, ResultListener<List<Question>> listener) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Questions AS q INNER JOIN Answers AS a ON q._id = a.qid WHERE q.category = ? ORDER BY q._id ASC",
                new String[]{query.getParam(QuestionParam.CATEGORY)});
        // int qCount = cursor.getCount();
        List<Question> questions = new ArrayList<>();
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
                DTO.Question dto = new DTO.Question();
                dto.category = cursor.getString(2);
                dto.answers = new ArrayList<>();
                dto.text = cursor.getString(1);
                dto.score = cursor.getInt(3);
                long qid = cursor.getLong(0);
                for (; !cursor.isAfterLast() && (qid == cursor.getLong(0)); cursor.moveToNext()) {
                    DTO.Answer answer = new DTO.Answer();
                    answer.text = cursor.getString(5);
                    answer.isCorrect = cursor.getInt(6) == 1;
                    dto.answers.add(answer);
                }
                questions.add(Question.fromDTO(dto));

            }
            listener.onComplete(questions);
        } catch (SQLiteException e) {
            listener.onError(e.getMessage());
        } finally {
            cursor.close();
        }
    }

    @Override
    public void getFlagBlobs(FlagConfiguration conf, ResultListener<List<byte[]>> listener) {

    }


    @Override
    public void save(Question question, ResultListener<Question> listener) {
        SQLiteDatabase db = helper.getWritableDatabase();
        saveHelper(question, listener, db);


    }

    private String saveHelper(Question question, ResultListener<Question> listener, SQLiteDatabase db) {
        DTO.Question dto = Question.toDTO(question);

        db.beginTransactionNonExclusive();
        try {
            long rowid = db.replaceOrThrow(QUESTIONS_TABLE_NAME, null, toContentValues(dto));
            for (DTO.Answer answerDto : dto.answers) {
                db.replaceOrThrow(ANSWERS_TABLE_NAME, null, toContentValues(answerDto, rowid));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            if (listener != null) {
                listener.onComplete(question);
            }
            return null;
        } catch (SQLException e) {
            db.endTransaction();
            String eMessage = e.getMessage();
            if (listener != null) {
                listener.onError(eMessage);
            }
            return eMessage;
        }
    }

    private ContentValues toContentValues(DTO.Question dto) {
        ContentValues values = new ContentValues(3);
        values.put("category", dto.category);
        values.put("score", dto.score);
        values.put("text", dto.text);
        return values;
    }

    private ContentValues toContentValues(DTO.Answer dto, long qid) {
        ContentValues values = new ContentValues(3);
        values.put("text", dto.text);
        values.put("isCorrect", dto.isCorrect);
        values.put("qid", qid);
        return values;

    }

    @Override
    public void save(final List<Question> questions, final ResultListener<List<Question>> listener) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String error;
        db.beginTransactionNonExclusive();
        for (Question question : questions) {
            error = saveHelper(question, null, db);
            if (error != null) {
                db.endTransaction();
                listener.onError(error);
                return;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        if (listener != null) {
            listener.onComplete(questions);
        }

    }

    @Override
    public void getUsers(DbQuery<UserParam> query, ResultListener<List<User>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(String uid, User user, ResultListener<User> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveCurrentUser(User user, ResultListener<User> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void signIn(User.Credentials cr, ResultListener<User> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void signUp(User.Credentials cr, User user, ResultListener<User> listener) {
        throw new UnsupportedOperationException();
    }
}
