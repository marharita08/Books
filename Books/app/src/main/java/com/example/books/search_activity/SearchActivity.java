package com.example.books.search_activity;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.books.NavigationActivity;
import com.example.books.R;
import com.example.books.book_list_activity.adapters.BookItemAdapter;
import com.example.books.book_list_activity.tasks.GetBooksTask;
import com.example.books.entities.SearchFilters;
import com.example.books.search_activity.adapters.AuthorItemAdapter;
import com.example.books.search_activity.adapters.GenreItemAdapter;
import com.example.books.search_activity.tasks.GetAuthorsTask;
import com.example.books.search_activity.tasks.GetGenresTask;
import com.example.books.utils.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class SearchActivity extends NavigationActivity {

    private static final String TAG = "SearchActivity";
    private static final String SEARCH_FILTERS_KEY = "searchFilters";
    private static final String TITLE_KEY = "title";
    private AuthorItemAdapter authorItemAdapter;
    private GenreItemAdapter genreItemAdapter;
    private BookItemAdapter bookItemAdapter;
    private SearchFilters searchFilters;
    private String title;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_search, findViewById(R.id.container));

        bookItemAdapter = new BookItemAdapter(URLBuilder.getBookImageUrl(), this, true);
        baseUrl = URLBuilder.searchBooksUrl();
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString(SEARCH_FILTERS_KEY);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                searchFilters = objectMapper.readValue(json, new TypeReference<SearchFilters>() {});
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            title = savedInstanceState.getString(TITLE_KEY);
            if (title != null && !title.isEmpty() || !searchFilters.isEmpty()) {
                if (title != null) {
                    new GetBooksTask(bookItemAdapter, this)
                            .execute(baseUrl + title, json);
                } else {
                    new GetBooksTask(bookItemAdapter, this)
                            .execute(baseUrl, json);
                }
            }
        } else {
            searchFilters = new SearchFilters();
        }


        RecyclerView authors = findViewById(R.id.searchAuthors);
        RecyclerView genres = findViewById(R.id.searchGenres);
        RecyclerView books = findViewById(R.id.searchResults);

        genres.setNestedScrollingEnabled(false);
        authors.setNestedScrollingEnabled(false);

        int gridColumnCount =
                getResources().getInteger(R.integer.search_column_count);
        books.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
        authors.setLayoutManager(new LinearLayoutManager(this));
        genres.setLayoutManager(new LinearLayoutManager(this));

        genreItemAdapter =
                new GenreItemAdapter(bookItemAdapter, this, baseUrl, title, searchFilters);
        authorItemAdapter =
                new AuthorItemAdapter(bookItemAdapter,this, baseUrl, title, searchFilters);

        authors.setAdapter(authorItemAdapter);
        genres.setAdapter(genreItemAdapter);
        books.setAdapter(bookItemAdapter);

        new GetGenresTask(genreItemAdapter, this).execute(URLBuilder.getGenresForSearchUrl());
        new GetAuthorsTask(authorItemAdapter, this).execute(URLBuilder.getAuthorsForSearchUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        if(title != null) {
            searchView.setIconified(true);
            searchView.onActionViewExpanded();
            searchView.setQuery(title, false);
            searchView.setFocusable(true);
        }

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void search(String title) {
        this.title = title;
        if (!title.isEmpty() || !searchFilters.isEmpty()) {
            String currentUrl;
            currentUrl = baseUrl + title;
            authorItemAdapter.setTitle(title);
            genreItemAdapter.setTitle(title);

            try {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(searchFilters);
                new GetBooksTask(bookItemAdapter, this).execute(currentUrl, json);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            bookItemAdapter.setBooks(null);
            bookItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonFilters = ow.writeValueAsString(searchFilters);
            outState.putString(SEARCH_FILTERS_KEY, jsonFilters);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        outState.putString(TITLE_KEY, title);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
