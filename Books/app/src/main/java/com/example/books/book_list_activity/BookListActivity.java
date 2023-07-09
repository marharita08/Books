package com.example.books.book_list_activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Switch;

import com.example.books.NavigationActivity;
import com.example.books.R;
import com.example.books.book_list_activity.adapters.AuthorItemAdapter;
import com.example.books.book_list_activity.adapters.GenreItemAdapter;
import com.example.books.book_list_activity.tasks.GetAuthorsTask;
import com.example.books.book_list_activity.tasks.GetGenresTask;
import com.example.books.utils.URLBuilder;

public class BookListActivity extends NavigationActivity {
    private static final String GENRE_MODE = "genre";
    private static final String AUTHOR_MODE = "author";
    private static final String MODE_STATE = "mode";
    private String mode;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_book_list, findViewById(R.id.container));

        recyclerView = findViewById(R.id.bookListGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            // set saved mode
            setMode(savedInstanceState.getString(MODE_STATE));
        } else {
            // set default mode
            setMode(GENRE_MODE);
        }

        // switch between modes
        Switch toggle = findViewById(R.id.genreAuthorSwitch);
        toggle.setOnClickListener(
                view -> {
                    if(toggle.isChecked()) {
                        setMode(AUTHOR_MODE);
                    } else {
                        setMode(GENRE_MODE);
                    }
                }
        );

    }

    private void setMode(String mode) {
        this.mode = mode;
        if (mode.equals(GENRE_MODE)) {
            GenreItemAdapter genreItemAdapter =
                    new GenreItemAdapter(
                            URLBuilder.getBooksByGenreUrl(),
                            URLBuilder.getBookImageUrl(),
                            this);
            recyclerView.setAdapter(genreItemAdapter);
            // getting genres
            new GetGenresTask(genreItemAdapter, this).execute(URLBuilder.getGenresUrl());
        } else if (mode.equals(AUTHOR_MODE)) {
            AuthorItemAdapter authorItemAdapter =
                    new AuthorItemAdapter(
                            URLBuilder.getBooksByAuthorUrl(),
                            URLBuilder.getBookImageUrl(),
                            this);
            recyclerView.setAdapter(authorItemAdapter);
            // getting authors
            new GetAuthorsTask(authorItemAdapter, this).execute(URLBuilder.getAuthorsUrl());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save mode state
        savedInstanceState.putString(MODE_STATE, mode);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
