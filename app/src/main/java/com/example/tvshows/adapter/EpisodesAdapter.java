package com.example.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshows.data.model.Episode;
import com.example.tvshows.databinding.ItemContainerEpisodeBinding;
import com.example.tvshows.databinding.ItemContainerSliderImageBinding;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<Episode> episodes;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodeViewHolder(ItemContainerEpisodeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesAdapter.EpisodeViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        String title = "S";
        String season = episode.getSeason();
        if (season.length() == 1) {
            season = "0".concat(season);
        }
        String episodeNumber = episode.getEpisode();
        if (episodeNumber.length() == 1) {
            episodeNumber = "0".concat(episodeNumber);
        }
        episodeNumber = "E".concat(episodeNumber);
        title = title.concat(season).concat(episodeNumber);
        holder.itemContainerEpisodeBinding.tvTitle.setText(title);
        holder.itemContainerEpisodeBinding.tvName.setText(episode.getName());
        holder.itemContainerEpisodeBinding.tvAirDate.setText("Air Date: " + episode.getAirDate());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }
    }
}
