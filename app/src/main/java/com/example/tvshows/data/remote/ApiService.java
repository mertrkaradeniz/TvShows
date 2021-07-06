package com.example.tvshows.data.remote;

import com.example.tvshows.data.model.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTvShows(@Query("page") int page);
}
