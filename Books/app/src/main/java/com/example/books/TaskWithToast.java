package com.example.books;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class TaskWithToast extends AsyncTask<String, Void, String> {

    protected Context context;

    public TaskWithToast(Context context) {
        this.context = context;
    }

    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}
