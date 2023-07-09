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
import com.example.books.entities.Genre;
import com.example.books.entities.SearchFilters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenreItemAdapter extends RecyclerView.Adapter<GenreItemAdapter.GenreItemViewHolder> {

    private static final String TAG = "GenreItemAdapter";
    private BookItemAdapter bookItemAdapter;
    private Context context;
    private String searchUrl;
    private String title;
    private List<Genre> genres;
    private SearchFilters searchFilters;
    private boolean[] checked;

    public GenreItemAdapter(BookItemAdapter bookItemAdapter, Context context,
                            String searchUrl, String title, SearchFilters searchFilters) {
        this.bookItemAdapter = bookItemAdapter;
        this.context = context;
        this.searchUrl = searchUrl;
        this.title = title;
        this.searchFilters = searchFilters;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        checked = new boolean[genres.size()];
    }

    @NonNull
    @Override
    public GenreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkbox_item, parent, false);

        return new GenreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreItemViewHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.checkBox.setText(genre.getGenre());
        checked[position] = searchFilters.getGenres().contains(genre);
        holder.checkBox.setChecked(checked[position]);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aPosition = holder.getAdapterPosition();
                checked[aPosition] = !checked[aPosition];
                if (checked[aPosition]) {
                    searchFilters.addGenre(genres.get(aPosition));
                } else {
                    searchFilters.removeGenre(genres.get(aPosition));
                }
                if (title != null && !title.isEmpty() || !searchFilters.isEmpty()) {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
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
        return genres==null?0:genres.size();
    }

    class GenreItemViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;

        GenreItemViewHolder(final View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
