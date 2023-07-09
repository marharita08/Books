package com.example.books.login_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.books.R;
import com.example.books.entities.User;
import com.example.books.utils.SessionManager;
import com.example.books.book_list_activity.BookListActivity;
import com.example.books.signup_activity.SignUpActivity;
import com.example.books.utils.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, TextWatcher {

    private static final String TAG = "LoginActivity";
    private EditText username;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, BookListActivity.class);
            startActivity(intent);
        }

        //find views
        TextView toSignUp = findViewById(R.id.toSignUp);
        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setEnabled(false);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);

        //set onclick listeners
        toSignUp.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toSignUp:
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                try {
                    String url = URLBuilder.getLoginUrl();
                    User user = new User(username.getText().toString(), password.getText().toString());
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String json = ow.writeValueAsString(user);
                    new LoginTask(this).execute(url, json);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (username.length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        if (password.length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        loginButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}