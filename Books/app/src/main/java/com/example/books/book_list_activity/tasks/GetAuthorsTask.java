package com.example.books.book_list_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.books.book_list_activity.adapters.AuthorItemAdapter;
import com.example.books.entities.Author;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;

public class GetAuthorsTask extends AsyncTask<String, Void, List<Author>> {

    private static final String TAG = "GetAuthorsTask";
    private final AuthorItemAdapter authorItemAdapter;
    private final Context context;

    public GetAuthorsTask(AuthorItemAdapter authorItemAdapter, Context context) {
        this.authorItemAdapter = authorItemAdapter;
        this.context = context;
    }

    @Override
    protected List<Author> doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String jsonArray = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonArray, new TypeReference<List<Author>>() {});
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Author> result) {
        authorItemAdapter.setAuthors(result);
        authorItemAdapter.notifyDataSetChanged();
    }

}
