package com.example.books.book_activity.tasks;

import android.content.Context;
import android.view.Menu;

import com.example.books.R;

public class CheckBookInWishlistTask extends CheckBookInListTask {


    public CheckBookInWishlistTask(Menu menu, Context context) {
        super(menu, context);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            menu.findItem(R.id.addToWishlist).setVisible(false);
        } else {
            menu.findItem(R.id.deleteFromWishlist).setVisible(false);
        }
    }
}
