package com.example.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshows.data.model.TVShow;
import com.example.tvshows.databinding.ItemContainerTvShowBinding;
import com.example.tvshows.listeners.TVShowsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder> {

    private List<TVShow> tvShows;
    private TVShowsListener tvShowsListener;

    public TVShowsAdapter(List<TVShow> tvShows, TVShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener = tvShowsListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TVShowViewHolder(ItemContainerTvShowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowsAdapter.TVShowViewHolder holder, int position) {
        TVShow tvShow = tvShows.get(position);
        RoundedImageView imageView = holder.itemContainerTvShowBinding.imgTVShow;
        imageView.setAlpha(0f);
        Picasso.get().load(tvShow.getImageThumbnailPath()).noFade().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                imageView.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.itemContainerTvShowBinding.tvName.setText(tvShow.getName());
        holder.itemContainerTvShowBinding.tvNetwork.setText(tvShow.getNetwork() + " (" + tvShow.getCountry() + ")");
        holder.itemContainerTvShowBinding.tvStarted.setText("Started on: " + tvShow.getStartDate());
        holder.itemContainerTvShowBinding.tvStatus.setText(tvShow.getStatus());

        holder.itemContainerTvShowBinding.getRoot().setOnClickListener(v -> tvShowsListener.onTVShowClicked(tvShow));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    static class TVShowViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }
    }
}
