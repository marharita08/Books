package com.example.books.book_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;

import com.example.books.utils.NetworkUtils;

public abstract class CheckBookInListTask extends AsyncTask<String, Void, Boolean> {

    protected Menu menu;
    protected Context context;

    public CheckBookInListTask(Menu menu, Context context) {
        this.menu = menu;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        String response = networkUtils.sendGetRequest(strings[0]);
        return Boolean.valueOf(response.replace("\n", ""));
    }
}
