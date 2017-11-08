package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.tempModel.TmpSocialModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by raj on 4/16/2017.
 */

public class SocialItemListAdapter extends RecyclerView.Adapter<SocialItemListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<TmpSocialModel.Databean> list;

    public SocialItemListAdapter(Context mContext, ArrayList<TmpSocialModel.Databean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public SocialItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_items_row, parent, false);
        return new SocialItemListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SocialItemListAdapter.ViewHolder holder, final int position) {
        TmpSocialModel.Databean databean = list.get(position);

        holder.txtSocialName.setText("" + databean.Name);
        if (!TextUtils.isEmpty(databean.ImagePath))
            Glide.with(mContext)
                    .load(databean.ImagePath)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_email_circle1)
                    .fallback(R.mipmap.ic_email_circle1)
                    .into(holder.imgSocial);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (list != null)
            count = list.size();
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSocialName;

        ImageView imgSocial;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSocialName = (TextView) itemView.findViewById(R.id.txtSocialName);
            imgSocial = (ImageView) itemView.findViewById(R.id.imgSocial);

        }
    }
}

