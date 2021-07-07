package com.example.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshows.data.model.TVShow;
import com.example.tvshows.databinding.ItemContainerTvShowBinding;
import com.example.tvshows.listener.WatchlistListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.TVShowViewHolder> {

    private List<TVShow> tvShows;
    private WatchlistListener watchlistListener;

    public WatchListAdapter(List<TVShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
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
    public void onBindViewHolder(@NonNull WatchListAdapter.TVShowViewHolder holder, int position) {
        TVShow tvShow = tvShows.get(position);
        RoundedImageView imageView = holder.itemContainerTvShowBinding.imgTVShow;
        imageView.setAlpha(0f);
        Picasso.get().load(tvShow.getThumbnail()).noFade().into(imageView, new Callback() {
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
        holder.itemContainerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
        holder.itemContainerTvShowBinding.imgDelete.setOnClickListener(v -> watchlistListener.removeTVShowFromWatchlist(tvShow, position));
        holder.itemContainerTvShowBinding.imgDelete.setVisibility(View.VISIBLE);
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
