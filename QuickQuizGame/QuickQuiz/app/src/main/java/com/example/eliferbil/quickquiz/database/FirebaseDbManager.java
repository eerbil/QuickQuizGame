package com.example.eliferbil.quickquiz.database;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.database.DTOs.DTO;
import com.example.eliferbil.quickquiz.memogame.Flag;
import com.example.eliferbil.quickquiz.quickquiz.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.QuestionParam;
import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.UserParam;


public class FirebaseDbManager implements DbManager {

    private static final String TAG = "FirebaseDbManager";
    public static final int WRITE_TIMEOUT_MILLIS = 5 * 1000;
    public static final String QUESTIONS = "Questions";
    public static final String USERS = "Users";
    private final FirebaseAuth auth;
    private final FirebaseDatabase database;
    private final Object lock = new Object();
    private volatile boolean authRequestOnFly = false;


    {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public FirebaseDbManager() {

    }

    public FirebaseDbManager(String email, String password) {
        tryAuth(email, password);
    }

//    @Override
//    public <T> void save(UserModel entity, final ResultListener listener) {
//        if (auth.getCurrentUser() == null) {
//            waitAuth();
//        }
//        DatabaseReference myRef = database.getReference("visitors");
//        UserDTO dbEntity = new UserDTO(entity);
//        myRef.push().setValue(dbEntity, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                String error = databaseError != null ? databaseError.getMessage() : null;
//                listener.onComplete(error);
//            }
//        });
//
//    }

//    @Override
//    public FutureTask<String> saveInFuture(final UserModel entity) {
//        return new FutureTask<>(new Callable<String>() {
//            private final Object mLock = new Object();
//            private volatile boolean mResultReturned = false;
//            private volatile String mError = null;
//
//            @Override
//            public String call() {
//                save(entity, new ResultListener() {
//                    @Override
//                    public void onComplete(String error) {
//                        mError = error;
//                        mResultReturned = true;
//                        synchronized (mLock) {
//                            mLock.notify();
//                        }
//                    }
//                });
//                synchronized (mLock) {
//                    try {
//                        if (!mResultReturned) {
//                            mLock.wait(WRITE_TIMEOUT_MILLIS);
//                        }
//                    } catch (InterruptedException e) {
//                        mError = e.getMessage();
//                        Log.d(TAG, "InterruptedException", e);
//                    }
//
//                }
//                return mError;
//            }
//        });
//    }

    private void tryAuth(String email, String password) {
        authRequestOnFly = true;
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Exception taskException = task.getException();
                            String exceptionMessage = taskException.getMessage();
                            Log.e(TAG, exceptionMessage);
                            Log.d(TAG, "AuthFail", taskException);
                        }
                        authRequestOnFly = false;
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                });
    }

//    private void waitAuth() {
//        synchronized (lock) {
//            try {
//                while (authRequestOnFly) {
//                    lock.wait();
//                }
//            } catch (InterruptedException e) {
//                Log.e(TAG, e.getMessage());
//                Log.d(TAG, "InterruptedException", e);
//            }
//        }
//    }

    @Override
    public void save(final Question question, final ResultListener<Question> listener) {
        DatabaseReference myRef = database.getReference(QUESTIONS).child(question.getCategory());
        DTO.Question dbEntity = Question.toDTO(question);
        myRef.push().setValue(dbEntity, new SimpleCompletionListener<>(question, listener));
    }

    @Override
    public void save(final List<Question> questions, final ResultListener<List<Question>> listener) {
        ResultListener<Question> singleListener = new ResultListener<Question>() {
            private final List<Question> questionResults = new ArrayList<>();
            private final List<String> errors = new ArrayList<>();
            private final int total = questions.size();

            private void tryFinish() {
                int qSize = questionResults.size();
                int eSize = errors.size();
                if (qSize + eSize >= total) {
                    if (qSize > 0) {
                        listener.onComplete(new ArrayList<>(questionResults));
                    }
                    if (eSize > 0) {
                        listener.onError(TextUtils.join(", ", new ArrayList<>(errors)));
                    }
                }
            }

            @Override
            public synchronized void onComplete(Question data) {
                questionResults.add(data);
                tryFinish();
            }

            @Override
            public synchronized void onError(String error) {
                errors.add(error);
                tryFinish();

            }
        };

        for (Question question : questions) {
            save(question, singleListener);
        }
    }


    @Override
    public void getQuestions(DbQuery<QuestionParam> query, final ResultListener<List<Question>> listener) {
        DatabaseReference myRef = database.getReference(QUESTIONS);
        String categoryQuery = query.getParam(QuestionParam.CATEGORY);
        if (categoryQuery != null) {
            myRef = myRef.child(categoryQuery);
        }
        ValueEventListener valueEventListener = new SimpleValueEventListener<>(
                listener,
                new Function<DTO.Question, Question>() {
                    @Override
                    public Question apply(DTO.Question dto) {
                        return Question.fromDTO(dto);
                    }
                },
                DTO.Question.class
        );
        myRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getFlags(FlagConfiguration conf, ResultListener<List<Flag>> listener) {

    }

    @Override
    public void getUsers(DbQuery<UserParam> query, ResultListener<List<User>> listener) {
        Query dbRef = database.getReference(USERS);

        String uid = query.getParam(UserParam.UID);
        if (uid != null) {
            dbRef = dbRef.orderByKey().startAt(uid).limitToFirst(1);
        } else {
            for (Map.Entry<String, String> entry : query.params()) {
                dbRef = dbRef.equalTo(entry.getValue(), entry.getKey());
            }
        }

        ValueEventListener valueEventListener = new SimpleValueEventListener<>(
                listener,
                new Function<DTO.User, User>() {
                    @Override
                    public User apply(DTO.User dto) {
                        return User.fromDTO(dto);
                    }
                },
                DTO.User.class
        );
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void save(String uid, User user, ResultListener<User> listener) {
        database.getReference(USERS).child(uid)
                .setValue(
                        User.toDTO(user),
                        new SimpleCompletionListener<>(user, listener));
    }

    @Override
    public void signIn(User.Credentials cr, final ResultListener<User> listener) {
        auth.signInWithEmailAndPassword(cr.email, cr.password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fu = auth.getCurrentUser();
                            DbQuery<UserParam> uq = new DbQuery<>();
                            uq.setParam(UserParam.UID, fu.getUid());
                            getUsers(uq, new ResultListener<List<User>>() {
                                @Override
                                public void onComplete(List<User> data) {
                                    if (data != null && data.size() == 1) {
                                        listener.onComplete(data.get(0));
                                    } else {
                                        listener.onError("Not only 1 user has returned: " + data.size());
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    listener.onError(error);
                                }
                            });
                        } else {
                            if (listener != null) {
                                listener.onError(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void signUp(User.Credentials cr, final User user, final ResultListener<User> listener) {
        auth.createUserWithEmailAndPassword(cr.email, cr.password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fu = auth.getCurrentUser();
                            save(fu.getUid(), user, listener);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (listener != null) {
                                listener.onError(task.getException().getMessage());

                            }
                        }
                    }
                });

    }

    private static class SimpleValueEventListener<T, R> implements ValueEventListener {
        private ResultListener<List<R>> listener;
        private Function<T, R> converter;
        private Class<T> dtoClass;

        public SimpleValueEventListener(ResultListener<List<R>> listener, Function<T, R> converter, Class<T> dtoClass) {
            this.listener = listener;
            this.converter = converter;
            this.dtoClass = dtoClass;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<R> results = new ArrayList<>((int) dataSnapshot.getChildrenCount());
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                T dto = snapshot.getValue(dtoClass);
                results.add(converter.apply(dto));
            }

            if (listener != null) {
                listener.onComplete(results);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (listener != null) {
                listener.onError(databaseError.getMessage());
            }
        }
    }

    private static class SimpleCompletionListener<T> implements DatabaseReference.CompletionListener {

        private final ResultListener<T> listener;
        private final T result;

        public SimpleCompletionListener(T result, ResultListener<T> listener) {
            this.listener = listener;
            this.result = result;
        }

        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if (listener == null) {
                return;
            }
            if (databaseError != null) {
                listener.onError(databaseError.getMessage());
            } else {
                listener.onComplete(result);
            }
        }
    }

    // Copied from Java 8 java.util.function
    interface Function<T, R> {
        R apply(T t);
    }

//    @IgnoreExtraProperties
//    private static class UserDTO {
//        private String name;
//        private String company;
//        private String email;
//        private String phone;
//        Map<String, String> timestamp = ServerValue.TIMESTAMP;
//
//        public UserDTO() {
//            // Default constructor required for calls to DataSnapshot.getValue(UserDTO.class)
//        }
//
//        public UserDTO(String name, String company, String email, String phone) {
//            this.name = name;
//            this.company = company;
//            this.email = email;
//            this.phone = phone;
//        }
//
//        public UserDTO(UserModel model) {
//            this(model.getName(), model.getCompany(), model.getEmail(), model.getPhone());
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getCompany() {
//            return company;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public Map<String, String> getTimestamp() {
//            return timestamp;
//        }
//    }
}
