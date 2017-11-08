package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.model.SelectorModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by raj on 4/16/2017.
 */

public class RollSelectorAdapter extends RecyclerView.Adapter<RollSelectorAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SelectorModel> list;

    public RollSelectorAdapter(Context mContext, ArrayList<SelectorModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RollSelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selector, parent, false);
        return new RollSelectorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RollSelectorAdapter.ViewHolder holder, final int position) {
        SelectorModel emailModel = list.get(position);

        holder.tvSelectRoll.setText("" + emailModel.name);
       
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSelectRoll;


        public ViewHolder(View itemView) {
            super(itemView);
            tvSelectRoll = (TextView) itemView.findViewById(R.id.tvSelectRoll);

        }
    }
}

