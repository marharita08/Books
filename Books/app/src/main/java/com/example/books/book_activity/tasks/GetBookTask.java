package com.example.books.book_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.books.book_activity.adapters.AuthorItemAdapter;
import com.example.books.book_activity.adapters.GenreItemAdapter;
import com.example.books.entities.Book;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class GetBookTask extends AsyncTask<String, Void, Book> {

    private static final String TAG = "GetBookTask";
    private final WeakReference<TextView> titleWeakReference;
    private final WeakReference<TextView> descriptionWeakReference;
    private final WeakReference<TextView> pageAmountWeakReference;
    private final WeakReference<TextView> descriptionLabelWeakReference;
    private final WeakReference<TextView> pageAmountLabelWeakReference;
    private final AuthorItemAdapter authorItemAdapter;
    private final GenreItemAdapter genreItemAdapter;
    private final Context context;


    public GetBookTask(TextView title, TextView description, TextView pageAmount,
                       TextView descriptionLabel, TextView pageAmountLabel,
                       AuthorItemAdapter authorItemAdapter, GenreItemAdapter genreItemAdapter, Context context) {
        titleWeakReference = new WeakReference<>(title);
        descriptionWeakReference = new WeakReference<>(description);
        pageAmountWeakReference = new WeakReference<>(pageAmount);
        descriptionLabelWeakReference = new WeakReference<>(descriptionLabel);
        pageAmountLabelWeakReference = new WeakReference<>(pageAmountLabel);
        this.authorItemAdapter = authorItemAdapter;
        this.genreItemAdapter = genreItemAdapter;
        this.context = context;
    }

    @Override
    protected Book doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String json = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Book>() {});
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Book book) {
        titleWeakReference.get().setText(book.getTitle());
        String description = book.getDescription();
        if (description == null || description.isEmpty()) {
            hideTextView(descriptionLabelWeakReference.get());
            hideTextView(descriptionWeakReference.get());
        } else {
            descriptionWeakReference.get().setText(description);
        }
        int pageAmount = book.getPageAmount();
        if (pageAmount == 0) {
            hideTextView(pageAmountLabelWeakReference.get());
            hideTextView(pageAmountWeakReference.get());
        } else {
            pageAmountWeakReference.get().setText(String.valueOf(book.getPageAmount()));
        }

        genreItemAdapter.setGenres(book.getGenres());
        genreItemAdapter.notifyDataSetChanged();

        authorItemAdapter.setAuthors(book.getAuthors());
        authorItemAdapter.notifyDataSetChanged();
    }

    private void hideTextView(TextView textView) {
        textView.setVisibility(View.INVISIBLE);
        textView.setHeight(0);
        if (textView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            textView.requestLayout();
        }
    }
}
