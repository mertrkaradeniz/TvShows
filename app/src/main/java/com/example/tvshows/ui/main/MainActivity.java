package com.example.tvshows.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tvshows.adapter.TVShowsAdapter;
import com.example.tvshows.data.model.TVShow;
import com.example.tvshows.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        doInitialization();
    }

    private void doInitialization() {
        binding.rvTvShows.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows);
        binding.rvTvShows.setAdapter(tvShowsAdapter);
        getMostPopularTVShows();
//        Log.d("SAAS", "" + tvShows.size());
    }

    private void getMostPopularTVShows() {
        binding.pbLoading.setVisibility(View.VISIBLE);
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponse -> {
            binding.pbLoading.setVisibility(View.GONE);
            if (mostPopularTVShowsResponse != null) {
                if (mostPopularTVShowsResponse.getTvShows() != null) {
                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
//                    Log.d("SAAS", "" + tvShows.size());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}