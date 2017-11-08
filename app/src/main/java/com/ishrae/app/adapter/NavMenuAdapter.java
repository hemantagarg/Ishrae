package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishrae.app.R;

/**
 * Created by Nss Solutions on 17-11-2016.
 */

public class NavMenuAdapter extends RecyclerView.Adapter<NavMenuAdapter.DrawerViewHolder> {

    private String[] drawerMenuList;
    private Context mContext;

    public NavMenuAdapter(Context mContext, String[] drawerMenuList) {
        this.mContext = mContext;
        this.drawerMenuList = drawerMenuList;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_menu_item, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        String menuNmae = drawerMenuList[position];
        holder.txtNavItemTitle.setText(menuNmae);
        if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.my_profile))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_menu_profile);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.my_ishrae))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_my_society);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.HQ_Details))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_hq_details);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.news_events))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_news_events);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.notification))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_notif_side_menu);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.group_notification))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_notif_side_menu);
        }  else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Social))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_social_1);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Logout))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_logout);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.home))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_menu_home);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.post_event))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_menu_uploads);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Mails))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_menu_mails);
        }
        else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Poll_Of_Day))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_nav_poll_rersult );
        }
        else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Poll_Result))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_vote);
        }
        else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Feedback))) {
            holder.imvNavItemIcon.setImageResource(R.mipmap.ic_nav_feedback);
        }
    }

    @Override
    public int getItemCount() {
        return drawerMenuList.length;
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView txtNavItemTitle;
        ImageView imvNavItemIcon;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            txtNavItemTitle = (TextView) itemView.findViewById(R.id.txtNavItemTitle);
            imvNavItemIcon = (ImageView) itemView.findViewById(R.id.imvNavItemIcon);
        }
    }
}
