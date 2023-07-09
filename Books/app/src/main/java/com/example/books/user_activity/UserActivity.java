package com.example.books.user_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.NavigationActivity;
import com.example.books.entities.User;
import com.example.books.utils.GlideUrlBuilder;
import com.example.books.utils.SessionManager;
import com.example.books.user_activity.tasks.GetUserTask;
import com.example.books.user_activity.tasks.UploadAvatarTask;
import com.example.books.R;
import com.example.books.user_activity.tasks.UpdateUserTask;
import com.example.books.utils.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.dhaval2404.imagepicker.ImagePicker;


import java.io.IOException;

public class UserActivity extends NavigationActivity implements View.OnClickListener  {

    private static final String TAG = "UserActivity";
    private static final String USERNAME_KEY = "username";
    private static final int SELECT_IMAGE = 1;
    private TextView usernameField;
    private EditText fullNameField;
    private EditText passwordField;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_user, findViewById(R.id.container));
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String username = sessionManager.getStringData(USERNAME_KEY);

        //find views
        Button save = findViewById(R.id.save);
        save.setOnClickListener(this);
        ImageButton changeAvatar = findViewById(R.id.imageButton);
        changeAvatar.setOnClickListener(this);
        usernameField = findViewById(R.id.usernameField);
        fullNameField = findViewById(R.id.fullNameField);
        passwordField = findViewById(R.id.passwordField);
        avatar = findViewById(R.id.avatar);

        if (savedInstanceState == null) {
            //load user data
            new GetUserTask(this, usernameField, fullNameField)
                    .execute(URLBuilder.getUserUrl(username));
        }

        String avatarUrl = URLBuilder.getUserAvatarUrl(username);
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(avatarUrl, this);
        //load user avatar
        Glide.with(this)
                .load(glideUrl)
                .circleCrop()
                .into(avatar);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.save:
                updateUser();
                break;
            case R.id.imageButton:
                chooseImage();
                break;
        }
    }

    public void updateUser() {
        try {
            User user = new User(usernameField.getText().toString(),
                    fullNameField.getText().toString(),
                    passwordField.getText().toString());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(user);
            new UpdateUserTask(this)
                    .execute(URLBuilder.getUserUrl(), json);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void chooseImage() {
        ImagePicker.with(this).cropSquare().start(SELECT_IMAGE);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).circleCrop().into(avatar);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            new UploadAvatarTask(this, bitmap).execute(imageUri.getLastPathSegment(),
                    URLBuilder.getUserAvatarUrl(usernameField.getText().toString()));
        } else if (requestCode == SELECT_IMAGE && resultCode == ImagePicker.RESULT_ERROR) {
            Log.e(TAG, ImagePicker.getError(data));
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
