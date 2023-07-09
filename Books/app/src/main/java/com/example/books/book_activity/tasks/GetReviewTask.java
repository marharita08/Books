package com.example.books.book_activity.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.books.entities.Review;
import com.example.books.utils.NetworkUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;

public class GetReviewTask extends AsyncTask<String, Void, Review> {

    private static final String TAG = "GetReviewTask";
    private final WeakReference<EditText> reviewWeakReference;
    private final WeakReference<RatingBar> ratingWeakReference;
    private final Context context;

    public GetReviewTask(EditText review, RatingBar rating, Context context) {
        ratingWeakReference = new WeakReference<>(rating);
        reviewWeakReference = new WeakReference<>(review);
        this.context = context;
    }

    @Override
    protected Review doInBackground(String... strings) {
        try {
            NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
            String jsonArray = networkUtils.sendGetRequest(strings[0]);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonArray, new TypeReference<Review>() {});
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Review review) {
        if (review != null) {
            reviewWeakReference.get().setText(review.getReview());
            ratingWeakReference.get().setRating(review.getRating());
        }
    }
}
