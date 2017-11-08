package com.ishrae.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.activity.NewsDetails;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.NewsEvents;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<NewsEvents> list;

    OnLoadMoreListener onLoadMoreListener;
    private static final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;


    public NewsAdapter(Context mContext, ArrayList<NewsEvents> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
        this.mContext = mContext;
        this.list = list;

        this.onLoadMoreListener = onLoadMoreListener1;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = VIEW_PROG;
        if (list != null && list.size() > 0 && list.get(position) != null) {
            viewType = VIEW_ITEM;
        }
        return viewType;
    }

    public void setLoaded() {
        loading = false;
    }

    public boolean isLoaded() {
        return loading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            ProgressViewHolder vh = new ProgressViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            final NewsEvents newsEvents = list.get(position);
            holder.txtNewsTitle.setText("" + newsEvents.Title);
            holder.txtNewsDesc.setText("" + Html.fromHtml(newsEvents.Description));
            Util.loadImageFromUrl(mContext,holder.imvNewsImage,newsEvents.Image,R.mipmap.ic_place_holder);
//            Glide.with(mContext)
//                    .load(newsEvents.Image)
//                    .placeholder(R.mipmap.ic_place_holder)
//                    .priority(Priority.IMMEDIATE)
//                    .error(R.mipmap.ic_place_holder)
//                    .fallback(R.mipmap.ic_place_holder)
//                    .into(holder.imvNewsImage);

            holder.llNewsMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, NewsDetails.class);
                    in.putExtra("news", newsEvents);
                    ((Activity) mContext).startActivity(in);
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNewsTitle;
        TextView txtNewsDesc;

        ImageView imvNewsImage;
        LinearLayout llNewsMain;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNewsTitle = (TextView) itemView.findViewById(R.id.txtNewsTitle);
            txtNewsDesc = (TextView) itemView.findViewById(R.id.txtNewsDesc);
            imvNewsImage = (ImageView) itemView.findViewById(R.id.imvNewsImage);
            llNewsMain = (LinearLayout) itemView.findViewById(R.id.llNewsMain);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
