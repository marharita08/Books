package com.example.books.book_activity.tasks;

import android.content.Context;
import android.view.Menu;

import com.example.books.R;
import com.example.books.TaskWithToast;
import com.example.books.utils.NetworkUtils;

public class AddToReadingTask extends TaskWithToast {

    private final Menu menu;

    public AddToReadingTask(Context context, Menu menu) {
        super(context);
        this.menu = menu;
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        int responseCode = networkUtils.sendPostRequest(strings[0], "");
        if (responseCode == context.getResources().getInteger(R.integer.status_ok)) {
            return context.getString(R.string.reading_added_message);
        } else {
            return context.getString(R.string.error_message);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!result.equals(context.getString(R.string.error_message))) {
            menu.findItem(R.id.addToWishlist).setVisible(true);
            menu.findItem(R.id.deleteFromWishlist).setVisible(false);
            menu.findItem(R.id.addToReading).setVisible(false);
            menu.findItem(R.id.deleteFromReading).setVisible(true);
        }
    }
}
