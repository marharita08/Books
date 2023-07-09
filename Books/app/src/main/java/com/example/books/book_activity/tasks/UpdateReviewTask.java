package com.example.books.book_activity.tasks;

import android.content.Context;

import com.example.books.R;
import com.example.books.TaskWithToast;
import com.example.books.utils.NetworkUtils;

public class UpdateReviewTask extends TaskWithToast {

    public UpdateReviewTask(Context context) {
        super(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        int responseCode = networkUtils.sendPutRequest(strings[0], strings[1]);
        if (responseCode == context.getResources().getInteger(R.integer.status_ok)) {
            return context.getString(R.string.review_edited_message);
        } else {
            return context.getString(R.string.error_message);
        }
    }
}
