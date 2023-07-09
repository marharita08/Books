package com.example.books.book_activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.R;
import com.example.books.book_activity.tasks.AddToAlreadyReadTask;
import com.example.books.book_activity.tasks.GetReviewTask;
import com.example.books.book_activity.tasks.UpdateReviewTask;
import com.example.books.entities.Review;
import com.example.books.utils.GlideUrlBuilder;
import com.example.books.utils.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class AlreadyReadDialogFragment extends DialogFragment {

    private static final String TAG = "AlreadyReadDialog";
    private static final String BOOK_ID_KEY = "bookId";
    private static final String USERNAME_KEY = "username";
    private static final String BOOK_TITLE_KEY = "bookTitle";
    private static final String IMAGE_URL_KEY = "imageUrl";
    private static final String ADD_MODE = "add";
    private static final String EDIT_MODE = "edit";
    private final String mode;
    private Menu menu;

    public AlreadyReadDialogFragment(Menu menu, String mode) {
        this.menu = menu;
        this.mode = mode;
    }

    public AlreadyReadDialogFragment(String mode) {
        this.mode = mode;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get arguments
        int bookId = getArguments().getInt(BOOK_ID_KEY);
        String username = getArguments().getString(USERNAME_KEY);
        String bookTitle = getArguments().getString(BOOK_TITLE_KEY);
        String imageUrl = getArguments().getString(IMAGE_URL_KEY);

        //find views
        View root = getLayoutInflater().inflate((R.layout.already_read_dialog),null);
        ImageView coverPhoto = root.findViewById(R.id.dialogBookCover);
        TextView titleTextView = root.findViewById(R.id.dialogBookTitle);
        EditText reviewTextField = root.findViewById(R.id.reviewTextField);
        RatingBar ratingBar = root.findViewById(R.id.ratingBar);

        //load book image and title
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(imageUrl, this.getContext());
        Glide.with(root).load(glideUrl).into(coverPhoto);
        titleTextView.setText(bookTitle);

        String dialogTitle = "";
        String positiveButtonText = getString(R.string.save);
        String negativeButtonText = getString(R.string.cancel);

        if (mode.equals(EDIT_MODE)) {
            //load review to edit
            new GetReviewTask(reviewTextField, ratingBar, this.getContext())
                    .execute(URLBuilder.getReviewUrl(bookId, username));
            dialogTitle = getString(R.string.edit_review);
        } else if (mode.equals(ADD_MODE)) {
            dialogTitle = getString(R.string.add_to_already_read);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle(dialogTitle)
                .setView(root)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            String url = URLBuilder.getAlreadyReadUrl();

                            Review review = new Review(username, bookId, ratingBar.getRating(),
                                    reviewTextField.getText().toString());

                            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            String json = ow.writeValueAsString(review);
                            if (mode.equals(ADD_MODE)) {
                                new AddToAlreadyReadTask(getContext(), menu).execute(url, json);
                            } else if (mode.equals(EDIT_MODE)) {
                                new UpdateReviewTask(getContext()).execute(url, json);
                            }

                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }})
                .create();

    }
}
