package com.example.tvshows.ui.detail;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tvshows.R;
import com.example.tvshows.adapter.EpisodesAdapter;
import com.example.tvshows.adapter.ImageSliderAdapter;
import com.example.tvshows.data.model.TVShow;
import com.example.tvshows.databinding.ActivityTvshowDetailsBinding;
import com.example.tvshows.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tvshows.utilities.TempDataHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding binding;
    private TVShowDetailsViewModel viewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTvshowDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableInWatchlist = true;
                    binding.imgWatchList.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        binding.pbDetailLoading.setVisibility(View.VISIBLE);
        String tvShowId = String.valueOf(tvShow.getId());
        viewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            binding.pbDetailLoading.setVisibility(View.GONE);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                binding.imgTVShow.setAlpha(0f);
                Picasso.get().load(tvShowDetailsResponse.getTvShowDetails().getImagePath()).noFade().into(binding.imgTVShow, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.imgTVShow.animate().setDuration(300).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                binding.imgTVShow.setVisibility(View.VISIBLE);
                binding.tvDescription.setText(
                        String.valueOf(HtmlCompat.fromHtml(
                                tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                        ))
                );
                binding.tvDescription.setVisibility(View.VISIBLE);
                binding.tvReadMore.setVisibility(View.VISIBLE);
                binding.tvReadMore.setOnClickListener(v -> {
                    if (binding.tvReadMore.getText().toString().equals("Read More")) {
                        binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                        binding.tvDescription.setEllipsize(null);
                        binding.tvReadMore.setText(R.string.read_less);
                    } else {
                        binding.tvDescription.setMaxLines(4);
                        binding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                        binding.tvReadMore.setText(R.string.read_more);
                    }
                });
                binding.tvRating.setText(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                        )
                );
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    binding.tvGenre.setText(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    binding.tvGenre.setText("N/A");
                }
                binding.tvRuntime.setText(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                binding.viewDivider1.setVisibility(View.VISIBLE);
                binding.llMisc.setVisibility(View.VISIBLE);
                binding.viewDivider2.setVisibility(View.VISIBLE);
                binding.btnWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                binding.btnWebsite.setVisibility(View.VISIBLE);
                binding.btnEpisodes.setVisibility(View.VISIBLE);
                binding.btnEpisodes.setOnClickListener(v -> {
                    if (episodeBottomSheetDialog == null) {
                        episodeBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                        layoutEpisodesBottomSheetBinding = LayoutEpisodesBottomSheetBinding.inflate(
                                getLayoutInflater(),
                                (ViewGroup) getLayoutInflater().inflate(R.layout.layout_episodes_bottom_sheet, (ViewGroup) null),
                                false
                        );
                        episodeBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.rvEpisodes.setAdapter(
                                new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );
                        layoutEpisodesBottomSheetBinding.tvTitle.setText(
                                String.format("Episodes | %s", tvShow.getName())
                        );
                        layoutEpisodesBottomSheetBinding.imgClose.setOnClickListener(v1 -> episodeBottomSheetDialog.dismiss());
                    }
                    FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                            com.google.android.material.R.id.design_bottom_sheet
                    );
                    if (frameLayout != null) {
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    episodeBottomSheetDialog.show();
                });
                binding.imgWatchList.setOnClickListener(v -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTVShowAvailableInWatchlist) {
                        compositeDisposable.add(viewModel.removeTVShowFromWatchlist(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTVShowAvailableInWatchlist = false;
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    binding.imgWatchList.setImageResource(R.drawable.ic_watchlist);
                                    Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    } else {
                        compositeDisposable.add(viewModel.addToWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTVShowAvailableInWatchlist = true;
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    binding.imgWatchList.setImageResource(R.drawable.ic_added);
                                    Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                })
                        );
                    }
                });
                binding.imgWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.vpSlider.setOffscreenPageLimit(1);
        binding.vpSlider.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.vpSlider.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        binding.vpSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            binding.llSliderIndicators.addView(indicators[i]);
        }
        binding.llSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = binding.llSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.llSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails() {
        binding.tvName.setText(tvShow.getName());
        binding.tvNetworkCountry.setText(tvShow.getNetwork() + " (" + tvShow.getCountry() + ")");
        binding.tvStatus.setText(tvShow.getStatus());
        binding.tvStarted.setText("Started on: " + tvShow.getStartDate());
        binding.tvName.setVisibility(View.VISIBLE);
        binding.tvNetworkCountry.setVisibility(View.VISIBLE);
        binding.tvStatus.setVisibility(View.VISIBLE);
        binding.tvStarted.setVisibility(View.VISIBLE);
    }
}