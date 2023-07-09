package com.example.books.book_list_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.books.book_list_activity.adapters.BookItemAdapter;
import com.example.books.entities.Book;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class GetBooksTask extends AsyncTask<String, Void, List<Book>> {

    private static final String TAG = "GetBooksTask";
    private final BookItemAdapter bookItemAdapter;
    private final Context context;

    public GetBooksTask(BookItemAdapter bookItemAdapter, Context context) {
        this.bookItemAdapter = bookItemAdapter;
        this.context = context;
    }

    @Override
    protected List<Book> doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context);
            String jsonArray;
            if (strings.length > 1) {
                jsonArray = networkUtils.sendPostWithResponseRequest(strings[0], strings[1]);
            } else  {
                jsonArray = networkUtils.sendGetRequest(strings[0]);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonArray, new TypeReference<List<Book>>() {});
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Book> result) {
        if (result != null) {
            bookItemAdapter.setBooks(result);
            bookItemAdapter.notifyDataSetChanged();
        }
    }
}
