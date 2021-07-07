package com.example.tvshows.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshows.data.model.TVShowsResponse;
import com.example.tvshows.data.repository.SearchTVShowRepository;

public class SearchViewModel extends ViewModel {

    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowsResponse> searchTVShow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query, page);
    }

}
