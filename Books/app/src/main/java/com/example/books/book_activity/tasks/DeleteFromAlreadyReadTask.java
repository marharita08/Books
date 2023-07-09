package com.example.books.book_activity.tasks;

import android.content.Context;
import android.view.Menu;

import com.example.books.R;
import com.example.books.TaskWithToast;
import com.example.books.utils.NetworkUtils;

public class DeleteFromAlreadyReadTask extends TaskWithToast {

    private final Menu menu;

    public DeleteFromAlreadyReadTask(Context context, Menu menu) {
        super(context);
        this.menu = menu;
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        int responseCode = networkUtils.sendDeleteRequest(strings[0]);
        if (responseCode == context.getResources().getInteger(R.integer.status_ok)) {
            return context.getString(R.string.remove_from_already_read);
        } else {
            return context.getString(R.string.error_message);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!result.equals(context.getString(R.string.error_message))) {
            menu.findItem(R.id.deleteFromAlreadyRead).setVisible(false);
            menu.findItem(R.id.editReview).setVisible(false);
            menu.findItem(R.id.addToAlreadyRead).setVisible(true);
        }
    }
}
