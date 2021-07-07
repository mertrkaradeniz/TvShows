package com.example.tvshows.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshows.data.model.TVShowDetailsResponse;
import com.example.tvshows.data.repository.TVShowDetailsRepository;

public class TVShowDetailsViewModel extends ViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;

    public TVShowDetailsViewModel() {
        tvShowDetailsRepository = new TVShowDetailsRepository();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
}
