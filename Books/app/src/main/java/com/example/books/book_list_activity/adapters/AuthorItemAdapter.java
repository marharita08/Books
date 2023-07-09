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
import com.example.books.entities.Author;

import java.util.List;

public class AuthorItemAdapter extends RecyclerView.Adapter<AuthorItemAdapter.AuthorItemViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Author> authors;
    private final String bookUrl;
    private final String imageUrl;
    private final Context context;

    public AuthorItemAdapter(String bookUrl,
                            String imageUrl, Context context) {
        this.bookUrl = bookUrl;
        this.imageUrl = imageUrl;
        this.context = context;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public AuthorItemAdapter.AuthorItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list, parent, false);

        return new AuthorItemAdapter.AuthorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorItemAdapter.AuthorItemViewHolder holder, int position) {
        Author author = authors.get(position);
        holder.authorName.setText(author.getFullName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.bookListRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        holder.bookListRecyclerView.setLayoutManager(layoutManager);
        holder.bookListRecyclerView.setRecycledViewPool(viewPool);
        BookItemAdapter bookItemAdapter = new BookItemAdapter(imageUrl, context, false);
        holder.bookListRecyclerView.setAdapter(bookItemAdapter);
        new GetBooksTask(bookItemAdapter, context).execute(bookUrl + author.getId());

    }

    @Override
    public int getItemCount() {
        return authors == null ? 0 : authors.size();
    }

    class AuthorItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorName;
        private final RecyclerView bookListRecyclerView;

        AuthorItemViewHolder(final View itemView) {
            super(itemView);

            authorName = itemView.findViewById(R.id.groupName);
            bookListRecyclerView = itemView.findViewById(R.id.bookList);
        }
    }
}
