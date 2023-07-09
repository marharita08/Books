package com.example.books.book_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.books.utils.NetworkUtils;

import java.lang.ref.WeakReference;

public class GetRatingTask extends AsyncTask<String, Void, String> {

    private final WeakReference<TextView> ratingFieldWeakReference;
    private final Context context;

    public GetRatingTask(TextView ratingField, Context context) {
        ratingFieldWeakReference = new WeakReference<>(ratingField);
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        return networkUtils.sendGetRequest(strings[0]);
    }

    @Override
    protected void onPostExecute(String rating) {
        ratingFieldWeakReference.get().setText(rating);
    }
}
