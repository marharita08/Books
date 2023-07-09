package com.example.books;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.book_grid_activity.BookGridActivity;
import com.example.books.book_list_activity.BookListActivity;
import com.example.books.login_activity.LoginActivity;
import com.example.books.search_activity.SearchActivity;
import com.example.books.user_activity.UserActivity;
import com.example.books.utils.GlideUrlBuilder;
import com.example.books.utils.SessionManager;
import com.example.books.utils.URLBuilder;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String USERNAME_KEY = "username";
    private static final String JWT_TOKEN_KEY = "jwtToken";
    private static final String WISHLIST_MODE = "wishlist";
    private static final String READING_MODE = "reading";
    private static final String ALREADY_READ_MODE = "already_read";
    private static final String MODE_KEY = "mode";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        String username = sessionManager.getStringData(USERNAME_KEY);

        View navHeader = navigationView.inflateHeaderView(R.layout.nav_header);

        ImageView avatar = navHeader.findViewById(R.id.navAvatar);
        String avatarUrl = URLBuilder.getUserAvatarUrl(username);
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(avatarUrl, this);
        Glide.with(this).load(glideUrl)
                .circleCrop().into(avatar);

        TextView usernameField = navHeader.findViewById(R.id.navUsername);
        usernameField.setText(username);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.nav_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.nav_catalog:
                Intent catalogIntent = new Intent(this, BookListActivity.class);
                startActivity(catalogIntent);
                break;
            case R.id.nav_wishlist:
                Intent wishlistIntent = new Intent(this, BookGridActivity.class);
                wishlistIntent.putExtra(MODE_KEY, WISHLIST_MODE);
                startActivity(wishlistIntent);
                break;
            case R.id.nav_reading:
                Intent readingIntent = new Intent(this, BookGridActivity.class);
                readingIntent.putExtra(MODE_KEY, READING_MODE);
                startActivity(readingIntent);
                break;
            case R.id.nav_already_read:
                Intent alreadyReadIntent = new Intent(this, BookGridActivity.class);
                alreadyReadIntent.putExtra(MODE_KEY, ALREADY_READ_MODE);
                startActivity(alreadyReadIntent);
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(this, UserActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_logout:
                sessionManager.setLogin(false);
                sessionManager.deleteStringData(USERNAME_KEY);
                sessionManager.deleteStringData(JWT_TOKEN_KEY);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
        }

        return false;
    }
}
