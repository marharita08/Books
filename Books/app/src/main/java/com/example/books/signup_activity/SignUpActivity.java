package com.example.books.signup_activity;

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
import com.example.books.book_list_activity.BookListActivity;
import com.example.books.entities.User;
import com.example.books.login_activity.LoginActivity;
import com.example.books.login_activity.LoginTask;
import com.example.books.utils.SessionManager;
import com.example.books.utils.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SignUpActivity extends AppCompatActivity
        implements View.OnClickListener, TextWatcher {

    private static final String TAG = "SignUpActivity";
    private EditText username;
    private EditText fullName;
    private EditText password;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, BookListActivity.class);
            startActivity(intent);
        }

        username = findViewById(R.id.signUpUsername);
        fullName = findViewById(R.id.signUpFullName);
        password = findViewById(R.id.signUpPassword);
        signupButton = findViewById(R.id.signUpButton);
        TextView toLogin = findViewById(R.id.toLogin);

        username.addTextChangedListener(this);
        fullName.addTextChangedListener(this);
        password.addTextChangedListener(this);

        signupButton.setEnabled(false);

        signupButton.setOnClickListener(this);
        toLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toLogin:
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.signUpButton:
                try {
                    String url = URLBuilder.getSignUpUrl();
                    User user = new User(username.getText().toString(),
                            fullName.getText().toString(),
                            password.getText().toString());
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
        new ValidateFieldsTask(this, signupButton, username, password)
                .execute(URLBuilder.checkUsernameUrl(username.getText().toString()));
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}