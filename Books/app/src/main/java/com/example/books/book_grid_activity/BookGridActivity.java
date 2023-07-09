package com.example.books.book_grid_activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.NavigationActivity;
import com.example.books.R;
import com.example.books.utils.SessionManager;
import com.example.books.book_list_activity.adapters.BookItemAdapter;
import com.example.books.book_list_activity.tasks.GetBooksTask;
import com.example.books.utils.URLBuilder;

public class BookGridActivity extends NavigationActivity {

    private static final String USERNAME_KEY = "username";
    private static final String WISHLIST_MODE = "wishlist";
    private static final String READING_MODE = "reading";
    private static final String ALREADY_READ_MODE = "already_read";
    private static final String MODE_STATE = "mode";
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.book_list, findViewById(R.id.container));
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String username = sessionManager.getStringData(USERNAME_KEY);

        mode = getIntent().getStringExtra(MODE_STATE);

        //get views
        TextView title = findViewById(R.id.groupName);
        RecyclerView books = findViewById(R.id.bookList);
        LinearLayout linearLayout = findViewById(R.id.bookListLinearLayout);

        //set height and width for linear layout and recycler view
        linearLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        books.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        books.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        int gridColumnCount =
                getResources().getInteger(R.integer.grid_column_count);
        books.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
        //create and set adapter
        BookItemAdapter bookItemAdapter =
                new BookItemAdapter(URLBuilder.getBookImageUrl(), this, true);
        books.setAdapter(bookItemAdapter);

        String url;
        //set title and load data depending on mode
        switch (mode) {
            case WISHLIST_MODE:
                title.setText(R.string.wishlist);
                url = URLBuilder.getWishlistUrl(username);
                new GetBooksTask(bookItemAdapter, this).execute(url);
                break;
            case ALREADY_READ_MODE:
                title.setText(R.string.already_read);
                url = URLBuilder.getAlreadyReadUrl(username);
                new GetBooksTask(bookItemAdapter, this).execute(url);
                break;
            case READING_MODE:
                title.setText(getString(R.string.reading));
                url = URLBuilder.getReadingUrl(username);
                new GetBooksTask(bookItemAdapter, this).execute(url);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
