package com.example.tvshows.ui.watchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tvshows.databinding.ActivityWatchlistBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity {

    private ActivityWatchlistBinding binding;
    private WatchlistViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        binding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadWatchlist() {

        binding.pbWatchlist.setVisibility(View.VISIBLE);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    binding.pbWatchlist.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Watchlist: " + tvShows.size(), Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }
}