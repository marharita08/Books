package com.example.books.book_activity.tasks;

import android.content.Context;
import android.view.Menu;

import com.example.books.R;

public class CheckBookInAlreadyReadTask extends CheckBookInListTask {


    public CheckBookInAlreadyReadTask(Menu menu, Context context) {
        super(menu, context);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            menu.findItem(R.id.addToAlreadyRead).setVisible(false);
        } else {
            menu.findItem(R.id.deleteFromAlreadyRead).setVisible(false);
            menu.findItem(R.id.editReview).setVisible(false);
        }
    }
}
