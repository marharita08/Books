package com.example.books.book_activity.tasks;

import android.content.Context;
import android.view.Menu;

import com.example.books.R;

public class CheckBookInReadingTask extends CheckBookInListTask {

    public CheckBookInReadingTask(Menu menu, Context context) {
        super(menu, context);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            menu.findItem(R.id.addToReading).setVisible(false);
        } else {
            menu.findItem(R.id.deleteFromReading).setVisible(false);
        }
    }
}
