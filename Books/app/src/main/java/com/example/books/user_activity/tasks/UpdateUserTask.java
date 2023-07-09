package com.example.books.user_activity.tasks;

import android.content.Context;

import com.example.books.R;
import com.example.books.TaskWithToast;
import com.example.books.utils.NetworkUtils;

public class UpdateUserTask extends TaskWithToast {

    public UpdateUserTask(Context context) {
        super(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        int responseCode = networkUtils.sendPutRequest(strings[0], strings[1]);
        if (responseCode == context.getResources().getInteger(R.integer.status_ok)) {
            return context.getString(R.string.user_updated_message);
        } else {
            return context.getString(R.string.error_message);
        }
    }
}
