package com.example.books.user_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.books.entities.User;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;


public class GetUserTask extends AsyncTask<String, Void, User> {

    private static final String TAG = "GetUserTask";
    private final Context context;
    private final WeakReference<TextView> usernameField;
    private final WeakReference<EditText> fullNameField;

    public GetUserTask(Context context, TextView username, EditText fullName) {
        this.context = context;
        usernameField = new WeakReference<>(username);
        fullNameField = new WeakReference<>(fullName);
    }

    @Override
    protected User doInBackground(String ...strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String json = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<User>() {});
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(User user) {
        if (user != null) {
            usernameField.get().setText(user.getUsername());
            fullNameField.get().setText(user.getFullName());
        }
    }
}
