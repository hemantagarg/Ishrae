package com.ishrae.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by raj on 4/16/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;

   public ArrayList<Uri> imagesList;
public View.OnClickListener onClickListener;
    public ImageAdapter(Context mContext,ArrayList<Uri> imagesList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.imagesList = imagesList;
        this.onClickListener = onClickListener;


    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(imagesList.get(position))
                .into( holder.imvUploaded);
        holder.imgRemvoePhotos.setTag(position);
        holder.imgRemvoePhotos.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public ImageView imvUploaded;
      public ImageView imgRemvoePhotos;
        public ViewHolder(View itemView) {
            super(itemView);
            imvUploaded = (ImageView) itemView.findViewById(R.id.imvUploaded);
            imgRemvoePhotos = (ImageView) itemView.findViewById(R.id.imgRemvoePhotos);
        }
    }
}

