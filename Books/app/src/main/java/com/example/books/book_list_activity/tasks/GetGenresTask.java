package com.example.books.book_list_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.books.book_list_activity.adapters.GenreItemAdapter;
import com.example.books.entities.Genre;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;

public class GetGenresTask extends AsyncTask<String, Void, List<Genre>> {

    private static final String TAG = "GetGenresTask";
    private final GenreItemAdapter genreItemAdapter;
    private final Context context;

    public GetGenresTask(GenreItemAdapter genreItemAdapter, Context context) {
        this.genreItemAdapter = genreItemAdapter;
        this.context = context;
    }

    @Override
    protected List<Genre> doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String jsonArray = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonArray, new TypeReference<List<Genre>>() {});
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Genre> result) {
        genreItemAdapter.setGenres(result);
        genreItemAdapter.notifyDataSetChanged();
    }
}
