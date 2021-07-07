package com.example.tvshows.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshows.databinding.ItemContainerSliderImageBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private String[] sliderImages;

    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(ItemContainerSliderImageBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.ImageSliderViewHolder holder, int position) {
        String curr = sliderImages[position];
        ImageView imageView = holder.itemContainerSliderImageBinding.imgSlider;
        imageView.setAlpha(0f);
        Picasso.get().load(curr).noFade().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                imageView.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerSliderImageBinding itemContainerSliderImageBinding;

        public ImageSliderViewHolder(ItemContainerSliderImageBinding itemContainerSliderImageBinding) {
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;
        }
    }
}
