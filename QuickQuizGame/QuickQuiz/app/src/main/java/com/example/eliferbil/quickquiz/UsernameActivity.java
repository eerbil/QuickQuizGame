package com.example.eliferbil.quickquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.database.CachingDbManager;
import com.example.eliferbil.quickquiz.database.DbManager;
import com.example.eliferbil.quickquiz.database.FirebaseDbManager;
import com.example.eliferbil.quickquiz.database.SQLiteDbManager;
import com.example.eliferbil.quickquiz.quickquiz.Game;

public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_EMAIL_EXTRA = "login_email_extra";
    public static final String LOGIN_PASSWORD_EXTRA = "login_password_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        setTextFromIntent(LOGIN_EMAIL_EXTRA, R.id.email);
        setTextFromIntent(LOGIN_PASSWORD_EXTRA, R.id.password);

        listenClickFor(R.id.start, R.id.signup);
        DbManager.Provider.setDefault(
                new CachingDbManager(
                        new FirebaseDbManager(),
                        new SQLiteDbManager(getApplicationContext())));
        Game game = Game.getInstance();
        if (game.getUser() == null) {
            game.setUser(new User(""));
        }
    }

    protected void listenClickFor(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                EditText usernameField = (EditText) findViewById(R.id.email);
                EditText passwordField = (EditText) findViewById(R.id.password);
                String email = usernameField.getText().toString();
                String pass = passwordField.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    TextView errorField = (TextView) findViewById(R.id.errorMessage);
                    errorField.setText("Field cannot be empty!");

                } else {
                    login(email, pass);
                }
                break;
            case R.id.signup:
                Intent nextActivity = new Intent(this, SignupActivity.class);
                startActivity(nextActivity);
                break;
            default:
                break;
        }
    }

    private void login(String email, String pass) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Logging in...");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        DbManager.Provider.getDefault()
                .signIn(new User.Credentials(email, pass),
                        new DbManager.RunOnUIListener<>(
                                this,
                                new DbManager.ResultListener<User>() {
                                    @Override
                                    public void onComplete(User data) {
                                        Game.getInstance().setUser(data);
                                        pd.dismiss();
                                        Intent nextActivity = new Intent(UsernameActivity.this, MainActivity.class);
                                        startActivity(nextActivity);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        pd.dismiss();
                                        Toast.makeText(UsernameActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                }));
    }

    private void setTextFromIntent(String key, @IdRes int id) {
        String text = getIntent().getStringExtra(key);
        if (text != null) {
            ((EditText) findViewById(id)).setText(text);
        }
    }
}
