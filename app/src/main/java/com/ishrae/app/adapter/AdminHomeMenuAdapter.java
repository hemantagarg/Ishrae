package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.model.HomeMenuModel;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class AdminHomeMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<HomeMenuModel> list;

    public AdminHomeMenuAdapter(Context mContext, ArrayList<HomeMenuModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_admin_menu_row, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof ViewHolder) {

            AdminHomeMenuAdapter.ViewHolder holder = (ViewHolder) holder1;
            final HomeMenuModel homeMenuModel = list.get(position);
            holder.imgMenu.setImageResource(homeMenuModel.iconId);
            holder.txtMenuTitle.setText(homeMenuModel.menuName);
            if(homeMenuModel.count>0)
            {
                holder.txtMenuCounter.setVisibility(View.VISIBLE);
                holder.txtMenuCounter.setText(""+homeMenuModel.count);
            }
            else
                holder.txtMenuCounter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMenuTitle;
        TextView txtMenuCounter;
        LinearLayout llMenuContainer;
        ImageView imgMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMenuTitle = (TextView) itemView.findViewById(R.id.txtMenuTitle);
            txtMenuCounter = (TextView) itemView.findViewById(R.id.txtMenuCounter);
            llMenuContainer = (LinearLayout) itemView.findViewById(R.id.llMenuContainer);
            imgMenu = (ImageView) itemView.findViewById(R.id.imgMenu);

        }
    }


}
