package com.example.books.book_list_activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.book_list_activity.tasks.GetBooksTask;
import com.example.books.entities.Genre;


import java.util.List;

public class GenreItemAdapter extends RecyclerView.Adapter<GenreItemAdapter.GenreItemViewHolder>{
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Genre> genres;
    private final String bookUrl;
    private final String imageUrl;
    private final Context context;

    public GenreItemAdapter(String bookUrl,
                               String imageUrl, Context context) {
        this.bookUrl = bookUrl;
        this.imageUrl = imageUrl;
        this.context = context;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreItemAdapter.GenreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list, parent, false);

        return new GenreItemAdapter.GenreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreItemAdapter.GenreItemViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.genreTitle.setText(genre.getGenre());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.bookListRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        holder.bookListRecyclerView.setLayoutManager(layoutManager);
        holder.bookListRecyclerView.setRecycledViewPool(viewPool);
        BookItemAdapter bookItemAdapter = new BookItemAdapter(imageUrl, context, false);
        holder.bookListRecyclerView.setAdapter(bookItemAdapter);
        new GetBooksTask(bookItemAdapter, context).execute(bookUrl + genre.getId());
    }

    @Override
    public int getItemCount() {
        return genres==null?0:genres.size();
    }

    class GenreItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView genreTitle;
        private final RecyclerView bookListRecyclerView;

        GenreItemViewHolder(final View itemView) {
            super(itemView);

            genreTitle = itemView.findViewById(R.id.groupName);
            bookListRecyclerView = itemView.findViewById(R.id.bookList);
        }
    }
}
