package com.ishrae.app.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.AddSpeakerModel;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by raj on 4/16/2017.
 */

public class AddSpeakerAdapter extends RecyclerView.Adapter<AddSpeakerAdapter.ViewHolder> {
    private Context mContext;

    ArrayList<AddSpeakerModel> addSpeakerModelArrayList;
    private View.OnClickListener onClickListener;

    public AddSpeakerAdapter(Context mContext, ArrayList<AddSpeakerModel> addSpeakerModelArrayList,View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.addSpeakerModelArrayList = addSpeakerModelArrayList;
        this.onClickListener = onClickListener;

       
    }

    @Override
    public AddSpeakerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_speaker, parent, false);
        return new AddSpeakerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddSpeakerAdapter.ViewHolder holder, final int position) {
       holder.txtSpeakerName.setText(addSpeakerModelArrayList.get(position).speakerName);
        holder.imgRemvoeSpeaker.setOnClickListener(onClickListener);
        holder.imgRemvoeSpeaker.setTag(position);
    }

    @Override
    public int getItemCount() {
        return addSpeakerModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public   TextView txtSpeakerName;
     public ImageView imgRemvoeSpeaker;



        public ViewHolder(View itemView) {
            super(itemView);
            txtSpeakerName = (TextView) itemView.findViewById(R.id.txtSpeakerName);
            imgRemvoeSpeaker = (ImageView) itemView.findViewById(R.id.imgRemvoeSpeaker);

        }
    }
}

