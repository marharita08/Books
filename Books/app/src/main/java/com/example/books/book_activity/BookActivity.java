package com.example.books.book_activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.books.NavigationActivity;
import com.example.books.R;
import com.example.books.book_activity.tasks.AddToReadingTask;
import com.example.books.book_activity.tasks.CheckBookInReadingTask;
import com.example.books.book_activity.tasks.DeleteFromReadingTask;
import com.example.books.utils.GlideUrlBuilder;
import com.example.books.utils.SessionManager;
import com.example.books.book_activity.adapters.AuthorItemAdapter;
import com.example.books.book_activity.adapters.GenreItemAdapter;
import com.example.books.book_activity.adapters.ReviewItemAdapter;
import com.example.books.book_activity.tasks.AddToWishListTask;
import com.example.books.book_activity.tasks.CheckBookInAlreadyReadTask;
import com.example.books.book_activity.tasks.CheckBookInWishlistTask;
import com.example.books.book_activity.tasks.DeleteFromAlreadyReadTask;
import com.example.books.book_activity.tasks.DeleteFromWishlistTask;
import com.example.books.book_activity.tasks.GetBookTask;
import com.example.books.book_activity.tasks.GetRatingTask;
import com.example.books.book_activity.tasks.GetReviewsTask;
import com.example.books.book_list_activity.adapters.BookItemAdapter;
import com.example.books.book_list_activity.tasks.GetBooksTask;
import com.example.books.utils.URLBuilder;

public class BookActivity extends NavigationActivity {
    private static final String CUSTOM_TAG = "custom";
    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String BOOK_ID_KEY = "bookId";
    private static final String USERNAME_KEY = "username";
    private static final String BOOK_TITLE_KEY = "bookTitle";
    private static final String IMAGE_URL_KEY = "imageUrl";
    private static final String ADD_MODE = "add";
    private static final String EDIT_MODE = "edit";
    private int currentBookId;
    private String currentBookPhotoUrl;
    private String username;
    private TextView title;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_book, findViewById(R.id.container));
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        username = sessionManager.getStringData(USERNAME_KEY);

        Intent intent = getIntent();
        currentBookId = intent.getIntExtra(BOOK_ID_KEY, 0);
        currentBookPhotoUrl = intent.getStringExtra(IMAGE_URL_KEY);

        //find views
        ImageView coverPhoto = findViewById(R.id.bookCoverPhoto);
        title = findViewById(R.id.bookTitle);
        TextView description = findViewById(R.id.description);
        TextView pageAmount = findViewById(R.id.pageAmount);
        TextView averageRating = findViewById(R.id.averageRating);
        TextView myRating = findViewById(R.id.myRating);
        TextView reviewsLabel = findViewById(R.id.reviewsLabel);
        TextView descriptionLabel = findViewById(R.id.descriptionLabel);
        TextView pageAmountLabel = findViewById(R.id.pageAmountLabel);
        RecyclerView genres = findViewById(R.id.genresRecyclerView);
        RecyclerView authors = findViewById(R.id.authorsRecyclerView);
        RecyclerView relatedBooks = findViewById(R.id.relatedBooksRecyclerView);
        RecyclerView reviews = findViewById(R.id.reviewsRecyclerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = intent.getStringExtra(TRANSITION_NAME_KEY);
            coverPhoto.setTransitionName(transitionName);
        }

        //set layout managers to RecyclerViews
        genres.setLayoutManager(getNewHorizontalLinearLayoutManager());
        authors.setLayoutManager(getNewHorizontalLinearLayoutManager());
        relatedBooks.setLayoutManager(getNewHorizontalLinearLayoutManager());
        reviews.setLayoutManager(new LinearLayoutManager(this));
        reviews.setNestedScrollingEnabled(false);

        //create adapters
        GenreItemAdapter genreItemAdapter = new GenreItemAdapter();
        AuthorItemAdapter authorItemAdapter = new AuthorItemAdapter(URLBuilder.getAuthorPhotoUrl());
        BookItemAdapter bookItemAdapter =
                new BookItemAdapter(URLBuilder.getBookImageUrl(), this, false);
        ReviewItemAdapter reviewItemAdapter = new ReviewItemAdapter(URLBuilder.getUserAvatarUrl());

        //set adapters to RecyclerViews
        genres.setAdapter(genreItemAdapter);
        authors.setAdapter(authorItemAdapter);
        relatedBooks.setAdapter(bookItemAdapter);
        reviews.setAdapter(reviewItemAdapter);


        //load book image
        Glide.with(this).load(GlideUrlBuilder.getGlideUrl(currentBookPhotoUrl, this))
                .into(coverPhoto);
        //load book data
        new GetBookTask(
                title,
                description,
                pageAmount,
                descriptionLabel,
                pageAmountLabel,
                authorItemAdapter,
                genreItemAdapter,
                this).execute(URLBuilder.getBookUrl(currentBookId));
        //load list of related books
        new GetBooksTask(bookItemAdapter, this)
                .execute(URLBuilder.getRelatedBooksUrl(currentBookId));
        //load average rating of book
        new GetRatingTask(averageRating, this)
                .execute(URLBuilder.getAverageRatingUrl(currentBookId));
        //load my rating
        new GetRatingTask(myRating, this)
                .execute(URLBuilder.getMyRatingUrl(currentBookId, username));
        //load reviews
        new GetReviewsTask(reviewItemAdapter, reviewsLabel, this)
                .execute(URLBuilder.getReviewsUrl(currentBookId));
    }

    private LinearLayoutManager getNewHorizontalLinearLayoutManager() {
        return new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
        this.menu = menu;
        new CheckBookInWishlistTask(menu, this)
                .execute(URLBuilder.checkBookInWishlistUrl(currentBookId, username));
        new CheckBookInAlreadyReadTask(menu, this)
                .execute(URLBuilder.checkBookInAlreadyReadUrl(currentBookId, username));
        new CheckBookInReadingTask(menu, this)
                .execute(URLBuilder.checkBookInReadingUrl(currentBookId, username));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String url;

        switch (item.getItemId()) {
            case R.id.addToWishlist:
                url = URLBuilder.getWishlistUrl(currentBookId, username);
                new AddToWishListTask(this, menu).execute(url);
                break;
            case R.id.deleteFromWishlist:
                url = URLBuilder.getWishlistUrl(currentBookId, username);
                new DeleteFromWishlistTask(this, menu).execute(url);
                break;
            case R.id.addToReading:
                url = URLBuilder.getReadingUrl(currentBookId, username);
                new AddToReadingTask(this, menu).execute(url);
                break;
            case R.id.deleteFromReading:
                url = URLBuilder.getReadingUrl(currentBookId, username);
                new DeleteFromReadingTask(this, menu).execute(url);
                break;
            case R.id.addToAlreadyRead:
                AlreadyReadDialogFragment addDialog =
                        new AlreadyReadDialogFragment(menu, ADD_MODE);
                Bundle addArgs = new Bundle();
                addArgs.putString(USERNAME_KEY, username);
                addArgs.putInt(BOOK_ID_KEY, currentBookId);
                addArgs.putString(IMAGE_URL_KEY, currentBookPhotoUrl);
                addArgs.putString(BOOK_TITLE_KEY, title.getText().toString());
                addDialog.setArguments(addArgs);
                addDialog.show(getSupportFragmentManager(), CUSTOM_TAG);
                break;
            case R.id.deleteFromAlreadyRead:
                url = URLBuilder.getAlreadyReadUrl(currentBookId, username);
                new DeleteFromAlreadyReadTask(this, menu).execute(url);
                break;
            case R.id.editReview:
                AlreadyReadDialogFragment editDialog =
                        new AlreadyReadDialogFragment(EDIT_MODE);
                Bundle editArgs = new Bundle();
                editArgs.putString(USERNAME_KEY, username);
                editArgs.putInt(BOOK_ID_KEY, currentBookId);
                editArgs.putString(IMAGE_URL_KEY, currentBookPhotoUrl);
                editArgs.putString(BOOK_TITLE_KEY, title.getText().toString());
                editDialog.setArguments(editArgs);
                editDialog.show(getSupportFragmentManager(), CUSTOM_TAG);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
