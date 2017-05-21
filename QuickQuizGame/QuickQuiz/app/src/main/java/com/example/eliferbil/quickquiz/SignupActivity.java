package com.example.eliferbil.quickquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.database.DbManager;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        listenClickFor(R.id.userSubmit);
    }

    protected void listenClickFor(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userSubmit:
                User newUser = createUserFromInput();
                String password = getTextFrom(R.id.passwordBox);
                if (password.equals(getTextFrom(R.id.passwordRepeatBox))) {
                    register(newUser, password);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Password mismatch!", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null)
                            .setActionTextColor(Color.RED)
                            .show();
                }

                break;
        }
    }

    private User createUserFromInput() {
        int[] editTxtIds = {R.id.usernameBox, R.id.emailBox, R.id.nameBox, R.id.surnameBox, R.id.cityBox};
        String[] values = new String[editTxtIds.length];
        for (int i = 0; i < editTxtIds.length; ++i) {
            values[i] = getTextFrom(editTxtIds[i]);
        }
        int i = 0;
        return new User(values[i++], values[i++], values[i++], values[i++], values[i++]);
    }

    private void register(User newUser, final String password) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Signing Up...");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        DbManager.Provider.getDefault()
                .signUp(new User.Credentials(newUser.getEmail(), password),
                        newUser,
                        new DbManager.RunOnUIListener<>(
                                this,
                                new DbManager.ResultListener<User>() {
                                    @Override
                                    public void onComplete(User data) {
                                        //Game.getInstance().setUser(data);

                                        Intent nextActivity = new Intent(SignupActivity.this, UsernameActivity.class);
                                        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        nextActivity.putExtra(UsernameActivity.LOGIN_EMAIL_EXTRA, data.getEmail());
                                        nextActivity.putExtra(UsernameActivity.LOGIN_PASSWORD_EXTRA, password);

                                        pd.dismiss();
                                        startActivity(nextActivity);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        pd.dismiss();
                                        Toast.makeText(SignupActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                }));
    }

    private String getTextFrom(@IdRes int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

}
