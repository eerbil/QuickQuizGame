package com.example.eliferbil.quickquiz.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.database.DTOs.DTO;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.QuestionParam;
import static com.example.eliferbil.quickquiz.database.DbManager.DbQuery.UserParam;


public class FirebaseDbManager implements DbManager {

    private static final String TAG = "FirebaseDbManager";
    //    public static final int WRITE_TIMEOUT_MILLIS = 5 * 1000;
    public static final String QUESTIONS = "Questions";
    public static final String USERS = "Users";
    public static final long TIMEOUT_MILLIS = 10000L;
    public static final String FLAGS = "Flags";
    private final FirebaseAuth auth;
    private final FirebaseDatabase database;
    private final Object lock = new Object();
    private volatile boolean authRequestOnFly = false;
    private volatile boolean isConnected;


    {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        listenConnection();
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
        ResultListener<Question> singleListener =
                new ListenerAggregator<>(questions.size(), listener);

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
        SimpleValueEventListener valueEventListener = new SimpleValueEventListener<>(
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
        timeoutListener(myRef, valueEventListener);

    }

    @Override
    public void getFlagBlobs(FlagConfiguration conf, ResultListener<List<byte[]>> listener) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        getFlagBlob(listener, storageRef);
    }

    private void getFlagBlob(final ResultListener<byte[]> listener, StorageReference storageRef) {
        OnCompleteListener<byte[]> onCompleteListener = new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (listener != null) {
                    if (task.isSuccessful()) {
                        byte[] blob = task.getResult();
                        listener.onComplete(blob);

                    } else {
                        listener.onError(task.getException().getMessage());
                    }
                }
            }
        };
        storageRef.child(FLAGS).getBytes(Long.MAX_VALUE).addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void getUsers(DbQuery<UserParam> query, ResultListener<List<User>> listener) {
        Query dbRef = database.getReference(USERS);

        String uid = query.getParam(UserParam.UID);
        if (uid != null) {
            dbRef = dbRef.orderByKey().startAt(uid).limitToFirst(1);
        } else {
            String orderBy = query.getOrderBy();
            if (orderBy != null) {
                dbRef = dbRef.orderByChild(orderBy);
            }
            for (Map.Entry<String, String> entry : query.params()) {
                dbRef = dbRef.equalTo(entry.getValue(), entry.getKey());
            }
        }

        final SimpleValueEventListener valueEventListener = new SimpleValueEventListener<>(
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
        timeoutListener(dbRef, valueEventListener);

    }

    @Override
    public void save(String uid, User user, ResultListener<User> listener) {
        database.getReference(USERS).child(uid)
                .setValue(
                        User.toDTO(user),
                        new SimpleCompletionListener<>(user, listener));
    }

    @Override
    public void saveCurrentUser(User user, ResultListener<User> listener) {
        save(auth.getCurrentUser().getUid(), user, listener);
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

    private void timeoutListener(final Query dbq, final SimpleValueEventListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isConnected) {
                        Thread.sleep(TIMEOUT_MILLIS);
                    }
                    dbq.removeEventListener(listener);
                    listener.removeListener();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void timeoutListener(final SimpleCompletionListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isConnected) {
                        Thread.sleep(TIMEOUT_MILLIS);
                    }
                    listener.removeListener();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void listenConnection() {
        DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isConnected = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Listener was cancelled");
            }
        });
    }

    private static class SimpleValueEventListener<T, R> implements ValueEventListener {
        private ResultListener<List<R>> listener;
        private Function<T, R> converter;
        private Class<T> dtoClass;

        private int callCount = 0;

        public SimpleValueEventListener(ResultListener<List<R>> listener, Function<T, R> converter, Class<T> dtoClass) {
            this.listener = listener;
            this.converter = converter;
            this.dtoClass = dtoClass;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            callCount++;
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
            callCount++;
            if (listener != null) {
                listener.onError(databaseError.getMessage());
            }
        }

        public void removeListener() {
            if (listener != null && callCount <= 0) {
                listener.onError("Exceeded Timeout Limit!");
            }
            listener = null;
        }
    }

    private static class SimpleCompletionListener<T> implements DatabaseReference.CompletionListener {

        private ResultListener<T> listener;
        private final T result;
        private int callCount = 0;

        public SimpleCompletionListener(T result, ResultListener<T> listener) {
            this.listener = listener;
            this.result = result;
        }

        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            callCount++;
            if (listener == null) {
                return;
            }
            if (databaseError != null) {
                listener.onError(databaseError.getMessage());
            } else {
                listener.onComplete(result);
            }
        }

        public void removeListener() {
            if (listener != null && callCount <= 0) {
                listener.onError("Exceeded Timeout Limit!");
            }
            listener = null;
        }
    }

    // Copied from Java 8 java.util.function
    interface Function<T, R> {
        R apply(T t);
    }
}
