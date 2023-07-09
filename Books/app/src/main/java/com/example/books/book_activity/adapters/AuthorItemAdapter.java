package com.example.books.book_activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.R;
import com.example.books.entities.Author;
import com.example.books.utils.GlideUrlBuilder;

import java.util.List;

public class AuthorItemAdapter extends RecyclerView.Adapter<AuthorItemAdapter.AuthorViewHolder> {

    private List<Author> authors;
    private final String photoUrl;

    public AuthorItemAdapter(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.author_item, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authors.get(position);
        holder.authorName.setText(author.getFullName());
        String imageUrl = photoUrl + author.getId();
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(imageUrl, holder.itemView.getContext());
        Glide.with(holder.itemView).load(glideUrl)
                .circleCrop().into(holder.authorPhoto);
    }

    @Override
    public int getItemCount() {
        return authors == null ? 0 : authors.size();
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final ImageView authorPhoto;
        private final TextView authorName;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            authorPhoto = itemView.findViewById(R.id.authorPhoto);
            authorName = itemView.findViewById(R.id.authorName);
        }
    }
}
