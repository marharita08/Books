package com.example.books.book_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.books.book_activity.adapters.ReviewItemAdapter;
import com.example.books.entities.Review;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetReviewsTask extends AsyncTask<String, Void, List<Review>> {

    private static final String TAG = "GetReviewsTask";
    private final ReviewItemAdapter reviewItemAdapter;
    private final WeakReference<TextView> reviewLabelWeakReference;
    private final Context context;

    public GetReviewsTask(ReviewItemAdapter reviewItemAdapter, TextView reviewLabel, Context context) {
        this.reviewItemAdapter = reviewItemAdapter;
        reviewLabelWeakReference = new WeakReference<>(reviewLabel);
        this.context = context;
    }

    @Override
    protected List<Review> doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String jsonArray = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonArray, new TypeReference<List<Review>>() {});
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews == null || reviews.size() == 0) {
            reviewLabelWeakReference.get().setVisibility(View.INVISIBLE);
        } else {
            reviewItemAdapter.setReviews(reviews);
            reviewItemAdapter.notifyDataSetChanged();
        }
    }
}
