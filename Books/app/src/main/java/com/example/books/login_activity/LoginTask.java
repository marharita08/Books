package com.example.books.login_activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.books.R;
import com.example.books.utils.SessionManager;
import com.example.books.book_list_activity.BookListActivity;
import com.example.books.entities.Session;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class LoginTask extends AsyncTask<String, Void, Session> {

    private static final String TAG = "LoginTask";
    private static final String USERNAME_KEY = "username";
    private static final String JWT_TOKEN_KEY = "jwtToken";

    private final Context context;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected Session doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String response = networkUtils.sendPostWithResponseRequest(strings[0], strings[1]);
            ObjectMapper objectMapper = new ObjectMapper();
            Glide.get(context).clearDiskCache();
            return objectMapper.readValue(response, new TypeReference<Session>() {});
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Session session) {
        if (session != null && !session.getUsername().isEmpty()
                && !session.getJwtToken().isEmpty()) {
            SessionManager sessionManager = new SessionManager(context.getApplicationContext());
            sessionManager.setLogin(true);
            sessionManager.saveStringData(USERNAME_KEY, session.getUsername());
            sessionManager.saveStringData(JWT_TOKEN_KEY, session.getJwtToken());
            Intent intent = new Intent(context, BookListActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

}
