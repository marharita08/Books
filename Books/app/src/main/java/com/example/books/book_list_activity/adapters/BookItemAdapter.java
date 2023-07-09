package com.example.books.book_list_activity.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.NavigationActivity;
import com.example.books.R;
import com.example.books.book_activity.BookActivity;
import com.example.books.entities.Book;
import com.example.books.utils.GlideUrlBuilder;


import java.util.List;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.BookViewHolder> {

    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String BOOK_ID_KEY = "bookId";
    private static final String IMAGE_URL_KEY = "imageUrl";
    private List<Book> books;
    private final String coverPhotoUrl;
    private final Context context;
    private final boolean grid;

    public BookItemAdapter(String imageUrl, Context context, boolean grid) {
        this.coverPhotoUrl = imageUrl;
        this.context = context;
        this.grid = grid;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.book_list_row,
                        viewGroup, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int position) {
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = books.get(bookViewHolder.getAdapterPosition());
                String transitionName = String.valueOf(System.currentTimeMillis());
                Intent intent = new Intent(view.getContext(), BookActivity.class);
                intent.putExtra(BOOK_ID_KEY, book.getId());
                intent.putExtra(IMAGE_URL_KEY, coverPhotoUrl + book.getCoverPhoto());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent.putExtra(TRANSITION_NAME_KEY, transitionName);
                    ActivityOptions options = null;
                    if (context instanceof NavigationActivity) {
                        options = ActivityOptions.makeSceneTransitionAnimation(
                                (NavigationActivity) context,
                                bookViewHolder.bookCoverPhoto,
                                transitionName
                        );
                        context.startActivity(intent, options.toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                } else {
                    context.startActivity(intent);
                }
            }
        });
        if (grid) {
            bookViewHolder.linearLayout.getLayoutParams().width
                    = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        Book book = books.get(position);
        bookViewHolder.bookTitle.setText(book.getTitle());
        String imageUrl = coverPhotoUrl + book.getCoverPhoto();
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(imageUrl,
                bookViewHolder.bookCoverPhoto.getContext());
        Glide.with(bookViewHolder.itemView)
                .load(glideUrl)
                .into(bookViewHolder.bookCoverPhoto);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        private final TextView bookTitle;
        private final ImageView bookCoverPhoto;
        private final LinearLayout linearLayout;


        BookViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.title);
            bookCoverPhoto = itemView.findViewById(R.id.coverPhoto);
            linearLayout = itemView.findViewById(R.id.bookListRowLinearLayout);
        }
    }
}
