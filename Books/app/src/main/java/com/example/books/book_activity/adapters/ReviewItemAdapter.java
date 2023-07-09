package com.example.books.book_activity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.books.R;
import com.example.books.entities.Review;
import com.example.books.utils.GlideUrlBuilder;

import java.util.List;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private final String avatarUrl;

    public ReviewItemAdapter(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        String imageUrl = avatarUrl + review.getUsername();
        GlideUrl glideUrl = GlideUrlBuilder.getGlideUrl(imageUrl, holder.itemView.getContext());
        Glide.with(holder.itemView).load(glideUrl)
                .circleCrop().into(holder.avatar);
        holder.username.setText(review.getUsername());
        holder.ratingBar.setRating(review.getRating());
        holder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView username;
        private final RatingBar ratingBar;
        private final TextView review;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.reviewAvatar);
            username = itemView.findViewById(R.id.reviewUsername);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            review = itemView.findViewById(R.id.reviewText);
        }
    }
}
