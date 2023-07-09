package com.example.books.search_activity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;

import com.example.books.book_list_activity.adapters.BookItemAdapter;
import com.example.books.book_list_activity.tasks.GetBooksTask;
import com.example.books.entities.Author;
import com.example.books.entities.SearchFilters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;

public class AuthorItemAdapter extends RecyclerView.Adapter<AuthorItemAdapter.AuthorItemViewHolder> {

    private static final String TAG = "AuthorItemAdapter";
    private BookItemAdapter bookItemAdapter;
    private Context context;
    private String searchUrl;
    private String title;
    private List<Author> authors;
    private SearchFilters searchFilters;
    private boolean[] checked;

    public AuthorItemAdapter(BookItemAdapter bookItemAdapter, Context context, String searchUrl,
                             String title, SearchFilters searchFilters) {
        this.bookItemAdapter = bookItemAdapter;
        this.context = context;
        this.searchUrl = searchUrl;
        this.title = title;
        this.searchFilters = searchFilters;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
        checked = new boolean[authors.size()];
    }

    @NonNull
    @Override
    public AuthorItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkbox_item, parent, false);

        return new AuthorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorItemViewHolder holder, int position) {
        Author author = authors.get(position);
        holder.checkBox.setText(author.getFullName());
        checked[position] = searchFilters.getAuthors().contains(author);
        holder.checkBox.setChecked(checked[position]);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aPosition = holder.getAdapterPosition();
                checked[aPosition] = !checked[aPosition];
                if (checked[aPosition]) {
                    searchFilters.addAuthor(authors.get(aPosition));
                } else {
                    searchFilters.removeAuthor(authors.get(aPosition));
                }
                if (title != null && !title.isEmpty() || !searchFilters.isEmpty()) {
                    try {
                        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                        String json = ow.writeValueAsString(searchFilters);
                        if (title != null) {
                            new GetBooksTask(bookItemAdapter, context)
                                    .execute(searchUrl + title, json);
                        } else {
                            new GetBooksTask(bookItemAdapter, context)
                                    .execute(searchUrl, json);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                } else {
                    bookItemAdapter.setBooks(null);
                    bookItemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return authors == null ? 0 : authors.size();
    }

    class AuthorItemViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;

        AuthorItemViewHolder(final View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
