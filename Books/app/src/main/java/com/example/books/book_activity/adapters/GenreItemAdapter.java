package com.example.books.book_activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.entities.Genre;

import java.util.List;

public class GenreItemAdapter extends RecyclerView.Adapter<GenreItemAdapter.GenreViewHolder> {

    private List<Genre> genres;

    public GenreItemAdapter() {
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.genre.setText(genre.getGenre());
    }

    @Override
    public int getItemCount() {
        return genres == null ? 0 : genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        private final TextView genre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre);
        }
    }
}
