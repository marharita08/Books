package com.example.books.user_activity.tasks;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.books.R;
import com.example.books.TaskWithToast;
import com.example.books.utils.NetworkUtils;

public class UploadAvatarTask extends TaskWithToast {
    private final Bitmap bitmap;

    public UploadAvatarTask(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    @Override
    protected String doInBackground(String... strings) {
        NetworkUtils networkUtils = new NetworkUtils(context.getApplicationContext());
        int responseCode = networkUtils.uploadImage(strings[0], strings[1], bitmap);
        if (responseCode == context.getResources().getInteger(R.integer.status_ok)) {
            return context.getString(R.string.image_uploaded_message);
        } else {
            return context.getString(R.string.error_message);
        }
    }
}
