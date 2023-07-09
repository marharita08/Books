package com.example.books.signup_activity;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;

import com.example.books.R;
import com.example.books.utils.NetworkUtils;

public class ValidateFieldsTask extends AsyncTask<String, Void, Boolean> {

    private final Context context;
    private final Button signupButton;
    private final EditText username;
    private final EditText password;

    public ValidateFieldsTask(Context context, Button signupButton,
                              EditText username, EditText password) {
        this.context = context;
        this.signupButton = signupButton;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (username.length() > 0) {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String response = networkUtils.sendGetRequest(strings[0]);
            return Boolean.valueOf(response.replace("\n", ""));
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            signupButton.setEnabled(false);
            username.setError(context.getString(R.string.username_taken));
        } else {
            signupButton.setEnabled(username.length() != 0 && password.length() != 0);
        }
    }
}
