package com.example.aldrin.fileupload.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aldrin.fileupload.R;
import com.example.aldrin.fileupload.database.LocalImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aldrin on 01-11-2016.
 * Adapter used to display image on screen.
 */

public class AdapterImageDisplay extends RecyclerView.Adapter<AdapterImageDisplay.ImageHolder>{

    private List<LocalImage> images;
    private Context context;

    public AdapterImageDisplay(Context context, List<LocalImage> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent,
                false);
        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.tvImageTitle.setText(images.get(position).getImage_name());
        if (images.get(position).getImage() != null)
            Picasso.with(context)
                    .load(new File(images.get(position).getImage()))
                    .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_image_title)
        TextView tvImageTitle;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
